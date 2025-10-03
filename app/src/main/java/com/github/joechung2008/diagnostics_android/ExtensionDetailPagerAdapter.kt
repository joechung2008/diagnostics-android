package com.github.joechung2008.diagnostics_android

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ExtensionDetailPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentCreators: MutableList<() -> Fragment> = mutableListOf()
    private val fragmentTitles: MutableList<String> = mutableListOf()

    fun addFragment(title: String, creator: () -> Fragment) {
        fragmentTitles.add(title)
        fragmentCreators.add(creator)
    }

    override fun getItemCount(): Int = fragmentCreators.size

    override fun createFragment(position: Int): Fragment {
        return fragmentCreators[position]()
    }

    fun getPageTitle(position: Int): CharSequence {
        return fragmentTitles[position]
    }
}
