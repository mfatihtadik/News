package com.fatih.newsapp.presentation.viewmodel


import com.fatih.newsapp.domain.model.NewsModel

sealed class NewsState {
    data class Success(val newsModel: NewsModel ) : NewsState()
    data class Error(val errorMessage : String) : NewsState()
}