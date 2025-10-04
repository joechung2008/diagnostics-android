package com.github.joechung2008.diagnostics_android.data

import com.github.joechung2008.diagnostics_android.model.Diagnostics
import com.github.joechung2008.diagnostics_android.model.Environment

class DiagnosticsRepository(private val apiService: DiagnosticsApiService) : IDiagnosticsRepository {

    override suspend fun getDiagnostics(environment: Environment): Diagnostics {
        val url = DiagnosticsApi.getUrlForEnvironment(environment)
        return apiService.getDiagnostics(url)
    }
}