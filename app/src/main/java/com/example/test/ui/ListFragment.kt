package com.example.test.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.test.R
import com.example.test.adapters.NewsAdapter
import com.example.test.api.NewsViewModel
import com.example.test.api.utils.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ListFragment : Fragment() {
   lateinit var recyclerView: RecyclerView
    private val viewModel: NewsViewModel by viewModels()
    private val RecyclerviewModel: NewsViewModel by activityViewModels()
    private lateinit var adapter: NewsAdapter
    private lateinit var fabScrollTop: FloatingActionButton
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var sourceId: String
    lateinit var lottieAnimationView: LottieAnimationView
    private var isSearchOperation = false // Flag to track search state

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_list, container, false)
        Log.d("mainListFragment", "onCreateView: ")
        recyclerView = view.findViewById(R.id.RecyclerView)
        layoutManager=LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        adapter = NewsAdapter(requireContext())
        recyclerView.adapter = adapter
        fabScrollTop = view.findViewById(R.id.fab_scroll_top)

         SrollFloatActionButton()

        // Fetch data based on the tab position
         sourceId = arguments?.getString("sourceID") ?:""

        // set Up Adapter
        adapter = NewsAdapter(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        setUpViewModel(sourceId)


        return view
    }

    private fun SrollFloatActionButton() {

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 10) {
                    fabScrollTop.show()
                } else if (dy < -10) {
                    fabScrollTop.hide()
                }
            }
        })

        // عند الضغط على الزر، العودة إلى أعلى القائمة
        fabScrollTop.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }
    }

    companion object {
        fun newInstance(channelId: String): ListFragment {
            val fragment = ListFragment()
            val args = Bundle()
            args.putString("sourceID", channelId)
            fragment.arguments = args
            return fragment
        }
    }

    fun setUpViewModel(sourceId:String){
//        adapter = NewsAdapter(requireContext())
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(context)

        var q = ""  // المتغير الخاص بالنص المدخل من المستخدم

        val searchView = requireActivity().findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchQuery = query?.takeIf { it.isNotEmpty() } ?: ""  // إذا كان النص فارغ، خلي القيمة فارغة
                isSearchOperation = searchQuery.isNotEmpty() // Set flag based on input
                q = searchQuery
                // في حالة الsubmit، نجيب المقالات بناءً على النص
                viewModel.getArticles(sourceId, Constants.API_KEY, q)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                // في حالة تغير النص، نحدثه في الـ ViewModel
                q = query?.takeIf { it.isNotEmpty() } ?: ""  // إذا كان النص فارغ، خلي القيمة فارغة
                isSearchOperation = q.isNotEmpty() // Set flag based on input

                // لو النص فاضي، رجعي المقالات الافتراضية
                if (q.isEmpty()) {
                    viewModel.getArticles(sourceId, Constants.API_KEY, "")
                } else {
                    // لو فيه نص، جلب المقالات بناءً عليه
                    viewModel.getArticles(sourceId, Constants.API_KEY, q)
                }
                return true
            }
        })


        if (viewModel.articles.value.isNullOrEmpty()) {
            viewModel.getArticles(sourceId, Constants.API_KEY, "")
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            lottieAnimationView = view?.findViewById(R.id.loading)!!
            if (isLoading == true) {
                lottieAnimationView?.visibility = View.VISIBLE // Show the loading animation
                recyclerView.visibility = View.GONE  // Hide the RecyclerView during loading
            } else {
                lottieAnimationView?.visibility = View.GONE // Hide the loading animation
                recyclerView.visibility = View.VISIBLE // Show the RecyclerView once data is loaded
            }
        }

        viewModel.articles.observe(viewLifecycleOwner, Observer { articles ->
            articles?.let {
                Log.d("ArticlesObserved", "Observing ${articles.size} articles")

                    if (articles.isNullOrEmpty()) {
                        if (!isSearchOperation) { // Show "No Request" animation only if not searching
                            lottieAnimationView?.apply {
                                setAnimation(R.raw.nodata) // Replace with your Lottie animation file
                                visibility = View.VISIBLE
                            }
                        }

                } else {
                        lottieAnimationView?.visibility = View.GONE // Hide animation if articles exist
                    // تحديث الـ Adapter
                    if (adapter.newsList != articles) {
                        adapter.updateArticles(articles)

                        // Post to ensure the RecyclerView is laid out
                        recyclerView.post {
                            // Restore the state after data is loaded
                            val savedState = RecyclerviewModel.recyclerViewStates[sourceId]
                            recyclerView.layoutManager?.onRestoreInstanceState(savedState)
                        }

                    }
                }

            }
        })

    }

    override fun onPause() {
        super.onPause()
        // Save the current RecyclerView state using sourceId as the key
        val state = recyclerView.layoutManager?.onSaveInstanceState()
        RecyclerviewModel.recyclerViewStates[sourceId] = state
    }

    override fun onResume() {
        super.onResume()
        // Restore the state specific to this sourceId
        val savedState = RecyclerviewModel.recyclerViewStates[sourceId]
        recyclerView.layoutManager?.onRestoreInstanceState(savedState)
    }
}