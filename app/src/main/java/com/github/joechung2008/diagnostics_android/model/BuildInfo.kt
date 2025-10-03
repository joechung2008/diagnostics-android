package com.github.joechung2008.diagnostics_android.model

import kotlinx.serialization.Serializable

@Serializable
data class BuildInfo(
    val buildVersion: String
)