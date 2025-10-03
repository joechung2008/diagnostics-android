package com.github.joechung2008.diagnostics_android.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.joechung2008.diagnostics_android.data.DiagnosticsRepository
import com.github.joechung2008.diagnostics_android.model.Diagnostics
import com.github.joechung2008.diagnostics_android.model.Environment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DiagnosticsRepository) : ViewModel() {

    private val _diagnostics = MutableStateFlow<Diagnostics?>(null)
    val diagnostics: StateFlow<Diagnostics?> = _diagnostics

    private var currentEnvironment = Environment.PUBLIC

    init {
        loadDiagnostics(currentEnvironment)
    }

    fun setEnvironment(environment: Environment) {
        currentEnvironment = environment
        loadDiagnostics(environment)
    }

    private fun loadDiagnostics(environment: Environment) {
        viewModelScope.launch {
            try {
                _diagnostics.value = repository.getDiagnostics(environment)
            } catch (e: Exception) {
                // Log the error to see what went wrong
                Log.e("MainViewModel", "Error loading diagnostics", e)
                _diagnostics.value = null
            }
        }
    }

    class Factory(private val repository: DiagnosticsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}