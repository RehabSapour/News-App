package com.example.test.api
import com.example.test.api.datamodel.newsResponse
import com.example.test.api.datamodel.sourceResponce
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("everything") //End Point // sources
    fun getArticles(@Query("sources")source:String?,@Query("apiKey")apiKey:String,@Query("q")searchQuery:String):retrofit2.Call<newsResponse>
    @GET("sources")
    fun getSources(@Query("category")category:String,@Query("apiKey")apiKey:String,@Query("language")language:String="en"):retrofit2.Call<sourceResponce>
}
//    fun getArticles(@Query("source") sources:String,@Query("q") query: String="news",@Query("apikey") apiKey:String= APIKEY):Call<NewsResponse>