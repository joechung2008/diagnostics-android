package com.github.joechung2008.diagnostics_android.data

import com.github.joechung2008.diagnostics_android.model.BuildInfo
import com.github.joechung2008.diagnostics_android.model.Diagnostics
import com.github.joechung2008.diagnostics_android.model.Environment
import com.github.joechung2008.diagnostics_android.model.ExtensionError
import com.github.joechung2008.diagnostics_android.model.ExtensionInfo
import com.github.joechung2008.diagnostics_android.model.ExtensionSync
import com.github.joechung2008.diagnostics_android.model.LastError
import com.github.joechung2008.diagnostics_android.model.ServerInfo

class FakeDiagnosticsRepository : IDiagnosticsRepository {

    private val fakeDiagnostics = Diagnostics(
        buildInfo = BuildInfo(buildVersion = "1.0.0-fake"),
        extensions = mapOf(
            "fakeExtension1" to ExtensionInfo(
                extensionName = "Fake Extension 1",
                config = mapOf("key1" to "value1")
            ),
            "fakeExtension2" to ExtensionError(
                lastError = LastError(
                    errorMessage = "This is a fake error",
                    time = "2024-01-01T00:00:00Z"
                )
            )
        ),
        serverInfo = ServerInfo(
            deploymentId = "fake-deployment-id",
            extensionSync = ExtensionSync(totalSyncAllCount = 42),
            hostname = "fake-hostname",
            serverId = "fake-server-id"
        )
    )

    override suspend fun getDiagnostics(environment: Environment): Diagnostics {
        return fakeDiagnostics
    }
}