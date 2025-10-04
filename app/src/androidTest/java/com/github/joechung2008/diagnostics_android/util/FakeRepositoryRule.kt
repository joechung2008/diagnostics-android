package com.github.joechung2008.diagnostics_android.util

import com.github.joechung2008.diagnostics_android.ServiceLocator
import com.github.joechung2008.diagnostics_android.data.FakeDiagnosticsRepository
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Ensures the app uses the FakeDiagnosticsRepository before the Activity is launched.
 */
class FakeRepositoryRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                // Install fake repo before activity launch
                ServiceLocator.diagnosticsRepository = FakeDiagnosticsRepository()
                try {
                    base.evaluate()
                } finally {
                    // No-op cleanup for now; could reset ServiceLocator if needed
                }
            }
        }
    }
}
