package com.pft.tracker.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.pft.tracker.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.net.URL

class UpdateManager(private val context: Context) {

    private val _status = MutableStateFlow<UpdateStatus>(UpdateStatus.Idle)
    val status: StateFlow<UpdateStatus> = _status

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun checkForUpdates(configUrl: String) {
        _status.value = UpdateStatus.Checking
        withContext(Dispatchers.IO) {
            try {
                val response = URL(configUrl).readText()
                val info = json.decodeFromString<UpdateInfo>(response)
                
                if (info.latestVersionCode > BuildConfig.VERSION_CODE) {
                    _status.value = UpdateStatus.NewVersionAvailable(info)
                } else {
                    _status.value = UpdateStatus.UpToDate
                }
            } catch (e: Exception) {
                Log.e("UpdateManager", "Check failed", e)
                _status.value = UpdateStatus.Error("ไม่สามารถตรวจสอบการอัปเดตได้")
            }
        }
    }

    suspend fun downloadAndInstall(info: UpdateInfo) {
        _status.value = UpdateStatus.Downloading(0f)
        withContext(Dispatchers.IO) {
            try {
                val destinationFile = File(context.cacheDir, "update.apk")
                if (destinationFile.exists()) destinationFile.delete()

                val url = URL(info.downloadUrl)
                val connection = url.openConnection()
                connection.connect()

                val fileLength = connection.contentLength
                val inputStream = url.openStream()
                val outputStream = destinationFile.outputStream()

                val buffer = ByteArray(4096)
                var totalBytesRead = 0L
                var bytesRead: Int

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    totalBytesRead += bytesRead
                    outputStream.write(buffer, 0, bytesRead)
                    if (fileLength > 0) {
                        _status.value = UpdateStatus.Downloading(totalBytesRead.toFloat() / fileLength)
                    }
                }

                outputStream.close()
                inputStream.close()

                installApk(destinationFile)
                _status.value = UpdateStatus.Idle
            } catch (e: Exception) {
                Log.e("UpdateManager", "Download failed", e)
                _status.value = UpdateStatus.Error("การดาวน์โหลดล้มเหลว")
            }
        }
    }

    private fun installApk(file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
