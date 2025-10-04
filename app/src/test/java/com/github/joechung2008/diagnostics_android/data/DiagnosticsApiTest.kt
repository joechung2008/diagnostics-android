package com.github.joechung2008.diagnostics_android.data

import com.github.joechung2008.diagnostics_android.model.Environment
import org.junit.Assert.assertEquals
import org.junit.Test

class DiagnosticsApiTest {

    @Test
    fun `getUrlForEnvironment should return correct URL for each environment`() {
        val expectedUrls = mapOf(
            Environment.PUBLIC to "https://hosting.portal.azure.net/api/diagnostics",
            Environment.FAIRFAX to "https://hosting.azureportal.usgovcloudapi.net/api/diagnostics",
            Environment.MOONCAKE to "https://hosting.azureportal.chinacloudapi.cn/api/diagnostics"
        )

        for (environment in Environment.entries) {
            val actualUrl = DiagnosticsApi.getUrlForEnvironment(environment)
            assertEquals(expectedUrls[environment], actualUrl)
        }
    }
}
