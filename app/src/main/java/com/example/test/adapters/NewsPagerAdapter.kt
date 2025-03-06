package com.example.test.adapters
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.test.api.datamodel.Source
import com.example.test.ui.ListFragment

// ViewPager adapter
class NewsPagerAdapter(fragment: Fragment, private val sources: List<Source>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return sources.size
    }

    override fun createFragment(position: Int): Fragment {
        Log.d("viewPagerAdapter", "${sources[position].id}: ")
        return ListFragment.newInstance(sources[position].id)
    }

}