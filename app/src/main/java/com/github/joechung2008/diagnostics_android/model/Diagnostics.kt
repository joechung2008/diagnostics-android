package com.github.joechung2008.diagnostics_android.model

import kotlinx.serialization.Serializable

@Serializable
data class Diagnostics(
    val buildInfo: BuildInfo,
    val extensions: Map<String, Extension?>,
    val serverInfo: ServerInfo
)