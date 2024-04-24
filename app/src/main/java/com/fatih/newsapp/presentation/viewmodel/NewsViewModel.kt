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
class NewsViewModel @Inject constructor(val repository: NewsRepositoryImpl, application: Application) : BaseViewModel(application){



    constructor(application: Application) : this(NewsRepositoryImpl(
        ApiClient().getClient().create(
            ApiServices::class.java)), application)

    val newsFromDb = MutableLiveData<List<NewsModel>>()

    val newDb = MutableLiveData<List<NewsModel>>()


    private val _news = MutableLiveData<NewsModel>()
    val currentWeather: LiveData<NewsModel> = _news


    // SharedPreferences anahtarları
    private val PREFS_NAME = "NewsPrefs"
    private val KEY_LAST_SYNC_DATE = "lastSyncDate"
    private val KEY_TIME = "savedTime"

    // SharedPreferences referansı
    private val prefs: SharedPreferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Sistem tarihini ve saati SharedPreferences'e kaydetme
    private fun saveLastSyncDateTime() {
        val currentSaat = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        prefs.edit().putInt(KEY_TIME,currentSaat).apply()
    }
    // SharedPreferences'den son senkronizasyon tarihini ve saati alma
    private fun getLastSyncDateTime(): Int? {

        return prefs.getInt(KEY_TIME,0)
    }


    fun executeloadNews(country : String, apiKey : String, category : String) {


        repository.getNews(country, apiKey,category).enqueue(object : Callback<NewsDto> {
            override fun onResponse(call: Call<NewsDto>, response: Response<NewsDto>) {
                if (response.isSuccessful) {
                    val newsModel = response.body()?.toNewsModel()
                    _news.value = newsModel!!
                    storeInSQLite(response)
                } else {
                    // Handle error
                }
            }
            override fun onFailure(call: Call<NewsDto>, t: Throwable) {
                // Handle failure
            }
        })
    }

     fun showNews(response: Response<NewsDto>){
        val newsModel = response.body()?.toNewsModel()
        _news.value = newsModel!!

    }
    private fun showCountries(countryList: List<NewsModel>) {
        newDb.value = countryList
    }


    private fun storeInSQLite(response: Response<NewsDto>){
        val newsModel = response.body()?.toNewsModel()
        _news.value = newsModel!!

        val newsList: MutableList<NewsModel> = mutableListOf()
        response.body()?.toNewsModel()?.let { newsList.add(it) }


        saveLastSyncDateTime()
        //println(getLastSyncDateTime())



        launch {
            val dao = NewsDatabase(getApplication()).newsDao()
            dao.deleteAllNews()


            val listLong = dao.insertAll(*newsList.toTypedArray())
            var i = 0
            while (i <newsList.size){
                newsList[i].uuid = listLong[i].toInt()
            }

            showNews(response)
        }
    }


    fun refreshData(country: String,apiKey: String,category: String){
        viewModelScope.launch {
            executeloadNews(country, apiKey, category)

        }
    }

    fun fetchNews(country: String, apiKey: String , category: String) {
        viewModelScope.launch {

            val newsql = NewsDatabase(getApplication()).newsDao().getAllNews()

            val prefsaat = getLastSyncDateTime()!!
            val currentSaat = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val snc = Math.abs(currentSaat-prefsaat)


            if (newsql.isEmpty() || snc>=2){
                executeloadNews(country, apiKey,category)
            }
            else{
                showCountries(newsql)
            }

        }

    }

}