package com.example.test.api

import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.test.api.datamodel.newsItem
import com.example.test.api.datamodel.Source
import com.example.test.api.datamodel.newsResponse
import com.example.test.api.datamodel.sourceResponce
import com.example.test.api.utils.Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import kotlin.math.log

class NewsViewModel: ViewModel() {
    var currentTabPosition: Int = 0
    private val _sourceId = MutableLiveData<String>()
    val sourceId: LiveData<String> get() = _sourceId
    // LiveData for sources
    private val _sources = MutableLiveData<sourceResponce?>()
    val sources: LiveData<sourceResponce?> get() = _sources

    // LiveData for articles
    private val _articles = MutableLiveData<List<newsItem>>()
    val articles: LiveData<List<newsItem>> get() = _articles

    private val searchQuery =  MutableLiveData<String>()
  //  private val searchQuery =  MutableLiveData<String>()


  fun setsearchQuery(query:String){
       searchQuery.value=query
  }
    fun getsearchQuery():LiveData<String>{
        return searchQuery
    }

    // Store the RecyclerView scroll position
    var recyclerViewState: Parcelable? = null
    private var cachesArticles:List<newsItem>?=null
///////////////////////////////////////////////////////////
//    private var _recyclerState: Parcelable? = null
//    val recyclerState get() = _recyclerState
//
//    fun saveRecyclerState(state: Parcelable) {
//        _recyclerState = state
//    }
//
//    // Cache API data
//    fun setCachedArticles(articles: List<newsItem>) {
//        if (_articles.value != articles) {
//            _articles.value = articles
//        }
//    }
//////////////////////////////////////////////////////////

    // LiveData for errors
    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> get() = _error


    fun getArticles(sourceId: String?, apiKey: String,q:String) {
        if (sourceId.isNullOrEmpty()) {
            _error.value = "Source ID is null or empty"
            return
        }
        if(cachesArticles !=null){
            _articles.postValue(cachesArticles!!)
            return
        }
        RetrofitConfig.getServiceInstance().getArticles(sourceId, apiKey,q)
            .enqueue(object : Callback<newsResponse> {
                override fun onResponse(call: Call<newsResponse>, response: Response<newsResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                          //  cachesArticles =it.articles
                            _articles.postValue(it.articles)
                        }
                    } else {
                        _error.value = "Failed to load sources"
                    }
                }
                override fun onFailure(call: Call<newsResponse>, t: Throwable) {
                    _error.value = t.message
                }
            })
    }



    fun getSources(category: String?, apiKey: String) {
        RetrofitConfig.getServiceInstance().getSources(category.toString(), apiKey)
            .enqueue(object : Callback<sourceResponce> {
                override fun onResponse(call: Call<sourceResponce>, response: Response<sourceResponce>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _sources.postValue(it)
                        }
                    } else {
                        _error.value = "Failed to load articles"
                    }
                }
                override fun onFailure(call: Call<sourceResponce>, t: Throwable) {
                    _error.value = t.message
                }
            })
    }

}