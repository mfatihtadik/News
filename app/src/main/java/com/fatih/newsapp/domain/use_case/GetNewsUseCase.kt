package com.fatih.newsapp.domain.use_case
import com.fatih.newsapp.data.remote.ApiClient
import com.fatih.newsapp.data.remote.ApiServices
import com.fatih.newsapp.data.remote.dto.NewsDto
import com.fatih.newsapp.data.remote.dto.toNewsModel
import com.fatih.newsapp.data.repository.NewsRepositoryImpl
import com.fatih.newsapp.presentation.viewmodel.NewsState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(val repository : NewsRepositoryImpl) {

    constructor() : this(NewsRepositoryImpl(ApiClient().getClient().create(ApiServices::class.java)))

    fun executeloadNews(country : String, apiKey : String, category : String, callback: (NewsState) -> Unit) {
        repository.getNews(country, apiKey,category).enqueue(object : Callback<NewsDto> {
            override fun onResponse(call: Call<NewsDto>, response: Response<NewsDto>) {
                if (response.isSuccessful) {
                    val newsModel = response.body()?.toNewsModel()
                    if (newsModel != null){
                        callback(NewsState.Success(newsModel))
                    } else {
                        callback(NewsState.Error("News data is null"))
                    }
                } else {
                    callback(NewsState.Error("Failed to fetch news data"))
                }
            }
            override fun onFailure(call: Call<NewsDto>, t: Throwable) {
               callback(NewsState.Error("Network request failed"))
            }
        })
    }
}