package com.github.joechung2008.diagnostics_android.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.joechung2008.diagnostics_android.data.DiagnosticsRepository
import com.github.joechung2008.diagnostics_android.model.BuildInfo
import com.github.joechung2008.diagnostics_android.model.Diagnostics
import com.github.joechung2008.diagnostics_android.model.Environment
import com.github.joechung2008.diagnostics_android.model.ExtensionInfo
import com.github.joechung2008.diagnostics_android.model.ExtensionSync
import com.github.joechung2008.diagnostics_android.model.ServerInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: DiagnosticsRepository

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createMockDiagnostics(version: String): Diagnostics {
        val buildInfo = BuildInfo("1.$version")
        val extensions = mapOf("ext1" to ExtensionInfo("test"))
        val serverInfo = ServerInfo("dep1", ExtensionSync(1), "host1", null, "serv1", null)
        return Diagnostics(buildInfo, extensions, serverInfo)
    }

    @Test
    fun `test initial load of diagnostics`() = runTest {
        val diagnostics = createMockDiagnostics("0")
        `when`(repository.getDiagnostics(Environment.PUBLIC)).thenReturn(diagnostics)

        viewModel = MainViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(repository).getDiagnostics(Environment.PUBLIC)
        val result = viewModel.diagnostics.value

        assertNotNull(result)
        assertEquals(diagnostics, result)
    }

    @Test
    fun `test setEnvironment loads new diagnostics`() = runTest {
        val publicDiagnostics = createMockDiagnostics("0")
        val fairfaxDiagnostics = createMockDiagnostics("1")
        `when`(repository.getDiagnostics(Environment.PUBLIC)).thenReturn(publicDiagnostics)
        `when`(repository.getDiagnostics(Environment.FAIRFAX)).thenReturn(fairfaxDiagnostics)

        viewModel = MainViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(publicDiagnostics, viewModel.diagnostics.value)

        viewModel.setEnvironment(Environment.FAIRFAX)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(repository).getDiagnostics(Environment.FAIRFAX)
        val result = viewModel.diagnostics.value

        assertNotNull(result)
        assertEquals(fairfaxDiagnostics, result)
    }

    @Test
    fun `test error loading diagnostics`() = runTest {
        val exception = RuntimeException("Failed to load")
        `when`(repository.getDiagnostics(Environment.PUBLIC)).thenThrow(exception)

        viewModel = MainViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(repository).getDiagnostics(Environment.PUBLIC)
        val result = viewModel.diagnostics.value

        assertNull(result)
    }
}
