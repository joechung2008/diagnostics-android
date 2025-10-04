package com.github.joechung2008.diagnostics_android

import com.github.joechung2008.diagnostics_android.data.DiagnosticsApi
import com.github.joechung2008.diagnostics_android.data.DiagnosticsRepository
import com.github.joechung2008.diagnostics_android.data.IDiagnosticsRepository

object ServiceLocator {

    @Volatile
    var diagnosticsRepository: IDiagnosticsRepository? = null

    fun provideDiagnosticsRepository(): IDiagnosticsRepository {
        synchronized(this) {
            return diagnosticsRepository ?: createDiagnosticsRepository()
        }
    }

    private fun createDiagnosticsRepository(): IDiagnosticsRepository {
        val newRepo = DiagnosticsRepository(DiagnosticsApi.service)
        diagnosticsRepository = newRepo
        return newRepo
    }
}