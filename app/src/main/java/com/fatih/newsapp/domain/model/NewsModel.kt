package com.fatih.newsapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fatih.newsapp.data.remote.dto.NewsDto
import com.google.gson.annotations.SerializedName

@Entity
data class NewsModel(
    @ColumnInfo(name = "articles")
    val articles: List<NewsDto.Article?>?,

    @ColumnInfo(name = "status")
    val status: String?,

    @ColumnInfo(name = "totalResults")
    val totalResults: Int?,

    ){


    @PrimaryKey(autoGenerate = true)
    var uuid : Int =0
}
