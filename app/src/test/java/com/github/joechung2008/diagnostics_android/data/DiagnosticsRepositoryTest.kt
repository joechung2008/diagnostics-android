package com.github.joechung2008.diagnostics_android.data

import com.github.joechung2008.diagnostics_android.model.BuildInfo
import com.github.joechung2008.diagnostics_android.model.Diagnostics
import com.github.joechung2008.diagnostics_android.model.Environment
import com.github.joechung2008.diagnostics_android.model.ExtensionInfo
import com.github.joechung2008.diagnostics_android.model.ExtensionSync
import com.github.joechung2008.diagnostics_android.model.ServerInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class DiagnosticsRepositoryTest {

    @Mock
    private lateinit var apiService: DiagnosticsApiService

    private lateinit var repository: DiagnosticsRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = DiagnosticsRepository(apiService)
    }

    private fun createMockDiagnostics(): Diagnostics {
        val buildInfo = BuildInfo("1.0")
        val extensions = mapOf("ext1" to ExtensionInfo("test"))
        val serverInfo = ServerInfo("dep1", ExtensionSync(1), "host1", null, "serv1", null)
        return Diagnostics(buildInfo, extensions, serverInfo)
    }

    @Test
    fun `test getDiagnostics for all environments`() = runTest {
        val mockDiagnostics = createMockDiagnostics()

        for (environment in Environment.entries) {
            val expectedUrl = DiagnosticsApi.getUrlForEnvironment(environment)
            `when`(apiService.getDiagnostics(expectedUrl)).thenReturn(mockDiagnostics)

            val result = repository.getDiagnostics(environment)

            verify(apiService).getDiagnostics(expectedUrl)
            assertEquals(mockDiagnostics, result)
        }
    }
}