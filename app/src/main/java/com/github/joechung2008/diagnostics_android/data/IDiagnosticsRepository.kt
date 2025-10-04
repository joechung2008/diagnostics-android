package com.github.joechung2008.diagnostics_android.data

import com.github.joechung2008.diagnostics_android.model.Diagnostics
import com.github.joechung2008.diagnostics_android.model.Environment

interface IDiagnosticsRepository {
    suspend fun getDiagnostics(environment: Environment): Diagnostics
}
