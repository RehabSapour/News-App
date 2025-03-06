package com.example.test.api

import com.example.test.api.datamodel.newsResponse
import com.example.test.api.datamodel.sourceResponce
import com.example.test.ui.sports

import retrofit2.http.GET
import retrofit2.http.Query
interface apiInterface {
        @GET("sources")
        fun getSources(@Query("category")category:String,@Query("apiKey")apiKey:String,@Query("language")language:String="en"):retrofit2.Call<sourceResponce>
        @GET("everything")
        fun getArticles(@Query("sources")category:String?,@Query("apiKey")apiKey:String):retrofit2.Call<newsResponse>

       // @GET("everything")
       // fun getResultsOfSearch(@Query("apiKey")apiKey:String,@Query("sortBy")sortBy:String,@Query("q")q:String):retrofit2.Call<PostDataClass>
}