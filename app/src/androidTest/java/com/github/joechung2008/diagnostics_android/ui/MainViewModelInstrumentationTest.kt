package com.github.joechung2008.diagnostics_android.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isSelected
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.joechung2008.diagnostics_android.MainActivity
import com.github.joechung2008.diagnostics_android.R
import com.github.joechung2008.diagnostics_android.util.FakeRepositoryRule
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewModelInstrumentationTest {

    private val fakeRepoRule = FakeRepositoryRule()
    private val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val chain: TestRule = RuleChain.outerRule(fakeRepoRule).around(activityRule)

    @Test
    fun testActivityLaunch() {
        onView(withId(R.id.view_pager)).check(matches(isDisplayed()))
        onView(withId(R.id.tab_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun testInitialTabIsSelected() {
        onView(allOf(withText("Extensions"), isDisplayed())).check(matches(isSelected()))
    }

    @Test
    fun testExtensionsAreDisplayed() {
        onView(withText("Fake Extension 1")).check(matches(isDisplayed()))
        onView(withText("This is a fake error")).check(matches(isDisplayed()))
    }

    @Test
    fun testBuildInfoIsDisplayed() {
        onView(withText("Build Information")).perform(click())
        onView(withId(R.id.build_version_text)).check(matches(withText("1.0.0-fake")))
    }

    @Test
    fun testServerInfoIsDisplayed() {
        onView(withText("Server Information")).perform(click())
        onView(withId(R.id.deployment_id_text)).check(matches(withText("fake-deployment-id")))
        onView(withId(R.id.hostname_text)).check(matches(withText("fake-hostname")))
        onView(withId(R.id.server_id_text)).check(matches(withText("fake-server-id")))
        onView(withId(R.id.total_sync_all_count_text)).check(matches(withText("42")))
        onView(withId(R.id.node_versions_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.uptime_text)).check(matches(not(isDisplayed())))
    }
}
