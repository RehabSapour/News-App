package com.example.test.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.adapters.NewsAdapter
import com.example.test.adapters.OnItemClickListener
import com.example.test.api.NewsViewModel

import com.example.test.api.utils.Constants
import com.example.test.sharedPreferences
import com.google.android.material.tabs.TabLayout

class ListFragment : Fragment() ,OnItemClickListener{
   lateinit var recyclerView: RecyclerView
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var adapter: NewsAdapter
    private var recyclerViewState: Parcelable? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_list, container, false)
        Log.d("mainListFragment", "onCreateView: ")
        recyclerView = view.findViewById(R.id.RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = NewsAdapter(requireContext(),this)
        recyclerView.adapter = adapter


        // Fetch data based on the tab position
        val sourceId = arguments?.getString("sourceID") ?:""
///////////////////////////////////////////////////////////////////////////////////////////////////////


//
  //     Log.d("sourceid","source id: $sourceId")
//        RetrofitConfig.getServiceInstance().getArticles(sourceId,Constants.API_KEY).enqueue(object :retrofit2.Callback<newsResponse>{
//            override fun onResponse(
//                call: retrofit2.Call<newsResponse>,
//                response: retrofit2.Response<newsResponse>
//            ) {
//                Toast.makeText(requireContext(),"offfffff2", Toast.LENGTH_SHORT).show()
//                Log.d("articles", "code:${response.code()} ")
//                if(response.isSuccessful) {
//                    articles.clear()
//                    response.body()?.articles?.let { articles.addAll(it) }
//                    adapter.updateArticles(articles)
//                }
//                else{
//                    Log.d("log2","error:${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: retrofit2.Call<newsResponse>, t: Throwable) {
//                Log.d("log2","source error")
//            }
//
//
//        })
//

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        adapter = NewsAdapter(requireContext(),this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        setUpViewModel(sourceId)

        return view
    }



    companion object {
        fun newInstance(channelId: String): ListFragment {
            val fragment = ListFragment()
            val args = Bundle()
            args.putString("sourceID", channelId)
           // args.putString("category",category)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(url: String) {
        Log.d("urlOnItemClick", "onItemClick:$url ")
        val fragment = webViewFragment.newInstance(url)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)  // Pass the instance with URL argument
            .addToBackStack(null)
            .commit()
    }

    fun setUpViewModel(sourceId:String){
        var q = ""  // المتغير الخاص بالنص المدخل من المستخدم

        val searchView = requireActivity().findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchQuery = query?.takeIf { it.isNotEmpty() } ?: ""  // إذا كان النص فارغ، خلي القيمة فارغة
                q = searchQuery
                // في حالة الsubmit، نجيب المقالات بناءً على النص
                viewModel.getArticles(sourceId, Constants.API_KEY, q)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                // في حالة تغير النص، نحدثه في الـ ViewModel
                q = query?.takeIf { it.isNotEmpty() } ?: ""  // إذا كان النص فارغ، خلي القيمة فارغة

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

        Log.d("search", "onCreateView: $q")

// جلب المقالات عند تحميل الـ Fragment للمرة الأولى باستخدام نص فارغ
        viewModel.getArticles(sourceId, Constants.API_KEY, "")

        viewModel.articles.observe(viewLifecycleOwner, Observer { articles ->
            articles?.let {
                Log.d("ArticlesObserved", "Observing ${articles.size} articles")

                // تحديث الـ Adapter
                if (adapter.newsList != articles) {
                    adapter.updateArticles(articles)
                }

                recyclerView.post {
                    viewModel.recyclerViewState?.let { savedState ->
                        recyclerView.layoutManager?.onRestoreInstanceState(savedState)
                    }
                }
            }
        })

    }



//    override fun onPause() {
//        super.onPause()
//        // Save RecyclerView scroll position
//        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//        val scrollState = layoutManager.onSaveInstanceState()
//        // Save the scroll position in the ViewModel or SharedPreferences
//        viewModel.recyclerViewState = scrollState
//    }


}