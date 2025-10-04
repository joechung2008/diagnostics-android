package com.github.joechung2008.diagnostics_android.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.joechung2008.diagnostics_android.MainActivity
import com.github.joechung2008.diagnostics_android.R
import com.github.joechung2008.diagnostics_android.util.FakeRepositoryRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainMenuInstrumentationTest {

    private val fakeRepoRule = FakeRepositoryRule()
    private val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val chain: TestRule = RuleChain.outerRule(fakeRepoRule).around(activityRule)

    @Test
    fun overflowMenu_containsAllEnvironments() {
        val context: Context = ApplicationProvider.getApplicationContext()

        // Open the overflow (options) menu
        openActionBarOverflowOrOptionsMenu(context)

        // Verify the three environment options are present
        onView(withText(R.string.menu_env_public)).check(matches(isDisplayed()))
        onView(withText(R.string.menu_env_fairfax)).check(matches(isDisplayed()))
        onView(withText(R.string.menu_env_mooncake)).check(matches(isDisplayed()))
    }
}
