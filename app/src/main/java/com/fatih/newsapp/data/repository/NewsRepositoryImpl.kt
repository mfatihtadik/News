package com.fatih.newsapp.data.repository

import com.fatih.newsapp.data.remote.dto.NewsDto
import com.fatih.newsapp.data.remote.ApiServices
import com.fatih.newsapp.domain.repository.NewsRepository
import com.fatih.newsapp.util.Constants.API_KEY
import retrofit2.Call
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val api : ApiServices) : NewsRepository {

    override fun getNews(country: String, apiKey: String,category :String): Call<NewsDto> {
        return api.getNews(country=country, apiKey=API_KEY , category = category)

    }

}