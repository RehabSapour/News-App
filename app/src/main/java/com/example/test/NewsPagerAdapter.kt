package com.example.test

import android.app.Fragment
import android.app.FragmentManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

// ViewPager adapter
class NewsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        // Return a Fragment based on the tab position
        return NewsChannelFragment.newInstance(position)
    }

    override fun getCount(): Int {
        // Return the total number of tabs
        return NUM_TABS
    }

    override fun getPageTitle(position: Int): CharSequence? {
        // Return the title for each tab
        return tabTitles[position]
    }
    companion object {
        private const val NUM_TABS = 3
        private val tabTitles = arrayOf("Channel 1", "Channel 2", "Channel 3")
    }
}