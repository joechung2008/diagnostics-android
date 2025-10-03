package com.github.joechung2008.diagnostics_android.model

import kotlinx.serialization.Serializable

@Serializable
data class ServerInfo(
    val deploymentId: String,
    val extensionSync: ExtensionSync,
    val hostname: String,
    val nodeVersions: String? = null,
    val serverId: String,
    val uptime: Long? = null
)

@Serializable
data class ExtensionSync(
    val totalSyncAllCount: Int
)