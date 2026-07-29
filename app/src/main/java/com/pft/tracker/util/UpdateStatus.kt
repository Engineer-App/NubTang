package com.pft.tracker.util

import kotlinx.serialization.Serializable

@Serializable
data class UpdateInfo(
    val latestVersionCode: Int,
    val latestVersionName: String,
    val downloadUrl: String,
    val releaseNotes: String = ""
)

sealed class UpdateStatus {
    object Idle : UpdateStatus()
    object Checking : UpdateStatus()
    data class NewVersionAvailable(val info: UpdateInfo) : UpdateStatus()
    object UpToDate : UpdateStatus()
    data class Downloading(val progress: Float) : UpdateStatus()
    data class Error(val message: String) : UpdateStatus()
}
