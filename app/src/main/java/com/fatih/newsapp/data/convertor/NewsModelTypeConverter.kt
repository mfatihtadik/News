package com.fatih.newsapp.data.convertor

import androidx.room.TypeConverter
import com.fatih.newsapp.data.remote.dto.NewsDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NewsModelTypeConverter {
    @TypeConverter
    fun fromString(value: String?): List<NewsDto.Article?>? {
        val listType = object : TypeToken<List<NewsDto.Article?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<NewsDto.Article?>?): String? {
        return Gson().toJson(list)
    }
}