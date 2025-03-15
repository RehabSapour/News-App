package com.example.test.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.example.test.R
import com.example.test.adapters.NewsPagerAdapter
import com.example.test.api.NewsViewModel
import com.example.test.api.datamodel.Source
import com.example.test.api.utils.Constants
import com.example.test.secondactivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class sports : Fragment()  { // the fragment that contain tab layout and view pager

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private val newsViewModel: NewsViewModel by viewModels()
    lateinit var  adapter : NewsPagerAdapter
    lateinit var searchView: SearchView
    lateinit var tv_pick:TextView

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("sportsFragment", "sportsFragmetDone:")
        val view = inflater.inflate(R.layout.fragment_sports, container, false)
///////////////////////////////////// search view visibility //////////////////////////////////////
        searchVisibility()

 /////////////////////////////////////////////////////////////////////////////////////////////

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)

/////////////////////////// set title of toolbar as category selected///////////////////////////

        val category = arguments?.getString("category")?:""
        // using to change the first char
        val capitalizedCategory = category.replaceFirstChar {
            if (it.isLowerCase()) it.uppercase() else it.toString()
        }
        (activity as? secondactivity)?.updateToolbarTitle(capitalizedCategory)

/////////////////////////////////////////////////////////////////////////////////////////

        Log.d("category","category: $category ")
       // call the view Model
        newsViewModel.getSources(category,Constants.API_KEY)

        return view
    }

    private fun searchVisibility() {
        searchView = activity?.findViewById(R.id.searchView)!!
        tv_pick = activity?.findViewById(R.id.tv_pick_in_toolbar_constraints)!!
        // Make the SearchView visible
        tv_pick.visibility = View.GONE
        searchView.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsViewModel.isNoRequest.observe(viewLifecycleOwner) { isNoRequest ->
           val  lottieAnimation = view.findViewById<LottieAnimationView>(R.id.wrong)
            if(isNoRequest){
                lottieAnimation.visibility = View.VISIBLE
                viewPager.visibility=View.GONE
            }else{
                lottieAnimation.visibility = View.GONE
                viewPager.visibility=View.VISIBLE
            }

        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                newsViewModel.currentTabPosition = position
            }
        })

        // Observe data and setup ViewPager
        newsViewModel.sources.observe(viewLifecycleOwner) { source ->
            if (source != null) {
                setupViewPager(source.sources)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.visibility = View.GONE
        tv_pick.visibility = View.VISIBLE
    }

    private fun setupViewPager(sources: List<Source>) {
        adapter = NewsPagerAdapter(this, sources)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = sources[position].name
        }.attach()
        // Restore saved tab position
        viewPager.setCurrentItem(newsViewModel.currentTabPosition, false)
    }


}