package com.example.test.api
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.api.datamodel.newsItem
import com.example.test.api.datamodel.newsResponse
import com.example.test.api.datamodel.sourceResponce
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class NewsViewModel: ViewModel() {
    //using to Save pos of tab
    var currentTabPosition: Int = 0

    // LiveData for sources
    private val _sources = MutableLiveData<sourceResponce?>()
    val sources: LiveData<sourceResponce?> get() = _sources

    // LiveData for articles
    private val _articles = MutableLiveData<List<newsItem>>()
    val articles: LiveData<List<newsItem>> get() = _articles

    private var cachesArticles:List<newsItem>?=null

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var _isNoRequest = MutableLiveData<Boolean>()
    val isNoRequest: LiveData<Boolean> get() = _isNoRequest

    // Map to store RecyclerView scroll states for each sourceId (tab)
   val recyclerViewStates: MutableMap<String, Parcelable?> = mutableMapOf()
//////////////////////////////////////////////////////////

    // LiveData for errors
    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> get() = _error


    fun getArticles(sourceId: String?, apiKey: String,q:String) {
        if (sourceId.isNullOrEmpty()) {
            _error.value = "Source ID is null or empty"
            return
        }
        _isLoading.postValue(true) // Start loading

        if(cachesArticles !=null){
            _articles.postValue(cachesArticles!!)
            _isLoading.postValue(false) // Stop loading
            return
        }

        RetrofitConfig.getServiceInstance().getArticles(sourceId, apiKey,q)
            .enqueue(object : Callback<newsResponse> {
                override fun onResponse(call: Call<newsResponse>, response: Response<newsResponse>) {
                    _isLoading.postValue(false) // Stop loading

                    if (response.isSuccessful) {

                        response.body()?.let {
                          //  cachesArticles =it.articles
                            _articles.postValue(it.articles)
                           // srollView.remove(sourceId)
                        }
                    } else {
                        _error.value = "Failed to load sources"
                    }
                }
                override fun onFailure(call: Call<newsResponse>, t: Throwable) {
                    _isLoading.postValue(false) // Stop loading
                    _error.value = t.message
                }
            })
    }

    fun getSources(category: String?, apiKey: String) {
        RetrofitConfig.getServiceInstance().getSources(category.toString(), apiKey)
            .enqueue(object : Callback<sourceResponce> {
                override fun onResponse(call: Call<sourceResponce>, response: Response<sourceResponce>) {
                    if(response.code()==429) _isNoRequest.postValue(true)
                    else _isNoRequest.postValue(false)

                    if (response.isSuccessful) {
                        response.body()?.let {
                            _sources.postValue(it)
                        }
                    } else {
                        _error.value = "Failed to load articles"
                    }
                }

                override fun onFailure(call: Call<sourceResponce>, t: Throwable) {
                    _isNoRequest.postValue(true) // Request failed
                    _error.value = t.message
                }
            })
    }

}