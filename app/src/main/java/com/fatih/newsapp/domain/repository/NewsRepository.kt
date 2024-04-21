package com.fatih.newsapp.domain.repository

import androidx.lifecycle.LiveData
import com.fatih.newsapp.data.remote.dto.NewsDto
import com.fatih.newsapp.domain.model.NewsModel
import retrofit2.Call

interface NewsRepository {

    fun getNews(country : String, apiKey: String, category: String): Call<NewsDto>

}