package com.github.joechung2008.diagnostics_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.widget.ViewPager2
import com.github.joechung2008.diagnostics_android.model.Extension
import com.github.joechung2008.diagnostics_android.model.ExtensionError
import com.github.joechung2008.diagnostics_android.model.ExtensionInfo
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.serialization.json.Json

class ExtensionDetailFragment : Fragment() {

    private var extension: Extension? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val extensionJson = it.getString(ARG_EXTENSION)
            extension = Json.decodeFromString<Extension>(extensionJson!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_extension_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        parentFragmentManager.popBackStack()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val extensionNameText = view.findViewById<TextView>(R.id.extension_name_text)
        val errorContainer = view.findViewById<LinearLayout>(R.id.error_container)
        val errorMessageText = view.findViewById<TextView>(R.id.error_message_text)
        val errorTimeText = view.findViewById<TextView>(R.id.error_time_text)
        val tabLayout = view.findViewById<TabLayout>(R.id.extension_detail_tab_layout)
        val viewPager = view.findViewById<ViewPager2>(R.id.extension_detail_view_pager)

        when (val ext = extension) {
            is ExtensionInfo -> {
                errorContainer.isVisible = false
                extensionNameText.text = ext.extensionName

                val adapter = ExtensionDetailPagerAdapter(this)
                ext.config?.let {
                    adapter.addFragment("Config") { KeyValueFragment.newInstance(it) }
                }
                ext.stageDefinition?.let {
                    val stageData = it.mapValues { entry -> entry.value.joinToString() }
                    adapter.addFragment("Stage Definition") { KeyValueFragment.newInstance(stageData) }
                }

                viewPager.adapter = adapter
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    tab.text = (viewPager.adapter as ExtensionDetailPagerAdapter).getPageTitle(position)
                }.attach()

                tabLayout.isVisible = adapter.itemCount > 1
            }
            is ExtensionError -> {
                extensionNameText.isVisible = false
                tabLayout.isVisible = false
                viewPager.isVisible = false
                errorContainer.isVisible = true
                errorMessageText.text = ext.lastError.errorMessage
                errorTimeText.text = ext.lastError.time
            }
            null -> {
                // Handle null case
            }
        }
    }

    companion object {
        private const val ARG_EXTENSION = "extension"

        fun newInstance(extension: Extension) =
            ExtensionDetailFragment().apply {
                arguments = Bundle().apply {
                    val extensionJson = Json.encodeToString(Extension.serializer(), extension)
                    putString(ARG_EXTENSION, extensionJson)
                }
            }
    }
}