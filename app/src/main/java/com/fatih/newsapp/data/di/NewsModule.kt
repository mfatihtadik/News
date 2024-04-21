package com.fatih.newsapp.data.di

import android.app.Application
import com.fatih.newsapp.data.repository.NewsRepositoryImpl
import com.fatih.newsapp.data.remote.ApiServices
import com.fatih.newsapp.domain.repository.NewsRepository
import com.fatih.newsapp.presentation.viewmodel.NewsViewModel
import com.fatih.newsapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsModule {


    @Provides
    @Singleton
    fun provideNewsApi() : ApiServices {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices::class.java)
    }


    @Provides
    @Singleton
    fun provideNewsRepository(api : ApiServices) : NewsRepository{
        return NewsRepositoryImpl(api = api)
    }

    @Provides
    @Singleton
    fun provideNewsViewModel(repository: NewsRepositoryImpl,application: Application): NewsViewModel {
        return NewsViewModel(repository,application)
    }


}