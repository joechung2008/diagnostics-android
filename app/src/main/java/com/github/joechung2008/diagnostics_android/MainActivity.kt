package com.github.joechung2008.diagnostics_android

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.github.joechung2008.diagnostics_android.model.Environment
import com.github.joechung2008.diagnostics_android.ui.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.Factory(ServiceLocator.provideDiagnosticsRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)

        viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Extensions"
                1 -> "Build Information"
                2 -> "Server Information"
                else -> null
            }
        }.attach()

        lifecycleScope.launch {
            viewModel.diagnostics.collect { diagnostics ->
                if (diagnostics != null) {
                    Log.d("MainActivity", "Diagnostics data loaded for ${diagnostics.buildInfo.buildVersion}")
                }
            }
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val inDetailView = supportFragmentManager.backStackEntryCount > 0
            supportActionBar?.setDisplayHomeAsUpEnabled(inDetailView)
            if (!inDetailView) {
                supportActionBar?.title = getString(R.string.app_name)
            }
            tabLayout.visibility = if (inDetailView) View.GONE else View.VISIBLE
            viewPager.visibility = if (inDetailView) View.GONE else View.VISIBLE
        }

        // Sync the UI state after a configuration change
        val inDetailView = supportFragmentManager.backStackEntryCount > 0
        tabLayout.visibility = if (inDetailView) View.GONE else View.VISIBLE
        viewPager.visibility = if (inDetailView) View.GONE else View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val environment = when (item.itemId) {
            R.id.menu_env_public -> Environment.PUBLIC
            R.id.menu_env_fairfax -> Environment.FAIRFAX
            R.id.menu_env_mooncake -> Environment.MOONCAKE
            else -> return super.onOptionsItemSelected(item)
        }
        viewModel.setEnvironment(environment)
        return true
    }

    private inner class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ExtensionsFragment()
                1 -> BuildInfoFragment()
                2 -> ServerInfoFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }
}