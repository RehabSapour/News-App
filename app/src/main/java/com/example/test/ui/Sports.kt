package com.example.test.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.test.R
import com.example.test.adapters.NewsPagerAdapter
import com.example.test.api.NewsViewModel
import com.example.test.api.datamodel.Source
import com.example.test.api.utils.Constants
import com.example.test.secondactivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class sports : Fragment()  { // the fragment that contain tab layout and view pager

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private val newsViewModel: NewsViewModel by viewModels()
   lateinit var  adapter : NewsPagerAdapter
   lateinit var searchView: SearchView
   lateinit var tv_pick:TextView
    private var recyclerViewState: Parcelable? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("sportsFragment", "sportsFragmetDone:")
        val view = inflater.inflate(R.layout.fragment_sports, container, false)
///////////////////////////////////// search view visibility //////////////////////////////////////
         searchView = activity?.findViewById(R.id.searchView)!!
         tv_pick = activity?.findViewById(R.id.tv_pick_in_toolbar_constraints)!!
        // Make the SearchView visible
        tv_pick.visibility=View.GONE
        searchView.visibility = View.VISIBLE

/////////////////////////////////////////////////////////////////////////////////////////////

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)

/////////////////////////// set title of toolbar as category selected///////////////////////////
        val category = arguments?.getString("category") ?:""
        (activity as? secondactivity)?.updateToolbarTitle(category)

        /////////////////////////////////////////////////////////

        Log.d("category","category: $category ")
       // call the view Model
        newsViewModel.getSources(category,Constants.API_KEY)




//        if (category != null) {
//            RetrofitConfig.getServiceInstance().getSources( category,Constants.API_KEY).enqueue(object: Callback<sourceResponce> {
//                @SuppressLint("NotifyDataSetChanged")
//                override fun onResponse(call: Call<sourceResponce>, response: Response<sourceResponce>) {
//    //                Log.d("NewsFragment", "Response code: ${response.code()}")
//    //                Log.d("NewsFragment", "Response message: ${response.message()}")
//                    Toast.makeText(requireContext(),"offfffff",Toast.LENGTH_SHORT).show()
//                    when (response.code()) {
//                        403 -> {
//                            Log.e("NewsFragment", "Access forbidden: ${response.message()}")
//                            // Handle access forbidden error
//                        }
//                        429 -> {
//                            Log.e("NewsFragment", "Rate limit exceeded: ${response.message()}")
//                            // Handle rate limit exceeded error
//                        }
//                        else -> {
//                            if (response.isSuccessful ) {
//                                Log.e("successful", "successful: ${response.message()}")
//                                sources.clear()
//                                response.body()?.sources?.let { sources.addAll(it) }
//                                Toast.makeText(requireContext(),"${sources.size}",Toast.LENGTH_SHORT).show()
//                                val adapter = NewsPagerAdapter(requireActivity(), sources, category)
//                                viewPager.adapter = adapter
//                                viewPager.adapter?.notifyDataSetChanged()
//                                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//                                    tab.text = sources[position].name
//                                }.attach()
//                            } else {
//                                Log.e("notSuccessful", "Error: ${response.errorBody()?.string()}")
//                            }
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<sourceResponce>, t: Throwable) {
//                    Log.d("onFailuresources","source error")
//                }
//
//            })
//        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/////////// in it/////
//        newsViewModel.sources.observe(viewLifecycleOwner, Observer { source->
//
//            if (source != null) {
//                val sources = source.sources
//                setupViewPager(sources) // Initialize ViewPager and restore position
//            }

//            if (source != null) {
//               val sources = source.sources
//                Log.d("Listsources", "onCreateView:${sources.size} ")
//                adapter = NewsPagerAdapter(this, sources)
//                viewPager.adapter = adapter
//               /// viewPager.adapter?.notifyDataSetChanged()
//                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//                    tab.text = sources[position].name
//                }.attach()
//            }else{
//                Log.d("null sources", "null done")
//            }
//        })


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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
        // Hide the SearchView when the fragment is destroyed
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