package com.example.test.api.datamodel

data class newsResponse(
    val status: String,
    val totalResults: Int,
    val articles: ArrayList<newsItem> = arrayListOf()
)