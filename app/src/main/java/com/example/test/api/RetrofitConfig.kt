package com.example.test.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitConfig {
    private fun getRetrofitInstance() : Retrofit {    // return instance of retrofit
        return Retrofit.Builder().baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    fun getServiceInstance():NewsApiService{
        return getRetrofitInstance().create<NewsApiService>() // using this to return reference from interface(NewsApiService) -> to allow me call news function
    }
}