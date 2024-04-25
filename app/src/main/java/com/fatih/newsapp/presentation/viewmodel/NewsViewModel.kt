package com.fatih.newsapp.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fatih.newsapp.data.remote.dto.NewsDto
import com.fatih.newsapp.data.remote.dto.toNewsModel
import com.fatih.newsapp.data.local.NewsDatabase
import com.fatih.newsapp.data.repository.NewsRepositoryImpl
import com.fatih.newsapp.data.remote.ApiClient
import com.fatih.newsapp.data.remote.ApiServices
import com.fatih.newsapp.domain.model.NewsModel
import com.fatih.newsapp.domain.use_case.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(val getNewsUseCase : GetNewsUseCase, application: Application) : BaseViewModel(application){
    constructor(application: Application) : this(GetNewsUseCase(), application)

    val newDb = MutableLiveData<List<NewsModel>>()
    private val _news = MutableLiveData<NewsModel>()
    val currentWeather: LiveData<NewsModel> = _news
    private val prefsName = "NewsPrefs"
    private val keyTime = "savedTime"
    private val prefs: SharedPreferences = application.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    private fun saveLastSyncDateTime() {
        val currentSaat = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        prefs.edit().putInt(keyTime,currentSaat).apply()
    }
    private fun getLastSyncDateTime(): Int? {
        return prefs.getInt(keyTime,0)
    }

    fun loadNews(country : String, apiKey : String, category : String){
        getNewsUseCase.executeloadNews(country, apiKey, category){ newsState ->
            when(newsState){
                is NewsState.Success -> {
                    _news.value = newsState.newsModel
                    storeInSQLite(newsState.newsModel)
                }
                is NewsState.Error -> {
                    //Handle Fail
                }
            }
        }
    }

    fun showNews(newsModel: NewsModel){
        _news.value = newsModel
    }
    private fun showNewsDb(newsList: List<NewsModel>) {
        newDb.value = newsList
    }
    private fun storeInSQLite(newsModel: NewsModel){
        _news.value = newsModel
        val newsList: MutableList<NewsModel> = mutableListOf()
        newsList.add(newsModel)
        saveLastSyncDateTime()

        launch {
            val dao = NewsDatabase(getApplication()).newsDao()
            dao.deleteAllNews()

            val listLong = dao.insertAll(*newsList.toTypedArray())
            var i = 0
            while (i <newsList.size){
                newsList[i].uuid = listLong[i].toInt()
            }
            showNews(newsModel)
        }
    }

    fun refreshData(country: String,apiKey: String,category: String){
        viewModelScope.launch {
            loadNews(country, apiKey, category)
        }
    }

    fun fetchNews(country: String, apiKey: String , category: String) {
        viewModelScope.launch {

            val newsql = NewsDatabase(getApplication()).newsDao().getAllNews()
            val prefsaat = getLastSyncDateTime()!!
            val currentSaat = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val snc = Math.abs(currentSaat-prefsaat)

            if (newsql.isEmpty() || snc>=2){
                loadNews(country, apiKey,category)
            }
            else{
                showNewsDb(newsql)
            }

        }

    }

}
