package com.pft.tracker.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.InputStream
import java.io.OutputStream

object MediaHelper {
    private const val ALBUM_NAME = "NabTang"

    /**
     * Copies an image from a URI to the public Pictures/NabTang folder.
     * Returns the new URI of the copied file.
     */
    fun copyToNabTangAlbum(context: Context, sourceUri: Uri): Uri? {
        val resolver = context.contentResolver
        val fileName = "Slip_${System.currentTimeMillis()}.jpg"
        
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/$ALBUM_NAME")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val destinationUri = resolver.insert(collection, contentValues) ?: return null

        try {
            val inputStream: InputStream? = resolver.openInputStream(sourceUri)
            val outputStream: OutputStream? = resolver.openOutputStream(destinationUri)

            if (inputStream != null && outputStream != null) {
                inputStream.copyTo(outputStream)
                outputStream.flush()
            }
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(destinationUri, contentValues, null, null)
            }
            
            return destinationUri
        } catch (e: Exception) {
            resolver.delete(destinationUri, null, null)
            return null
        }
    }
}
