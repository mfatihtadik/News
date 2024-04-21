package com.fatih.newsapp.data.remote

import com.fatih.newsapp.data.remote.dto.NewsDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("v2/top-headlines")
    fun getNews(
        @Query("country") country : String,
        @Query("apiKey") apiKey : String,
        @Query("category") category : String
    ): Call<NewsDto>

}