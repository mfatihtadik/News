package com.fatih.newsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fatih.newsapp.domain.model.NewsModel

@Dao
interface NewsDao {

    @Insert
    suspend fun insertAll(vararg news : NewsModel) : List<Long>

    @Query("SELECT * FROM newsmodel")
    suspend fun getAllNews(): List<NewsModel>

    @Query("SELECT * FROM newsmodel WHERE uuid = :newsId")
    suspend fun getNews(newsId : Int) : NewsModel

    @Query("DELETE FROM newsmodel")
    suspend fun deleteAllNews()

}