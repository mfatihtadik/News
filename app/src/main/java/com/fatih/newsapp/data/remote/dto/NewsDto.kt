package com.fatih.newsapp.data.remote.dto


import com.fatih.newsapp.domain.model.NewsModel
import com.google.gson.annotations.SerializedName

data class NewsDto(
    @SerializedName("articles")
    val articles: List<Article?>?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("totalResults")
    val totalResults: Int?,

    ) {
    data class Article(
        @SerializedName("author")
        val author: String?,
        @SerializedName("content")
        val content: Any?,
        @SerializedName("description")
        val description: Any?,
        @SerializedName("publishedAt")
        val publishedAt: String?,
        @SerializedName("source")
        val source: Source?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("url")
        val url: String?,
        @SerializedName("urlToImage")
        val urlToImage: Any?
    ) {
        data class Source(
            @SerializedName("id")
            val id: String?,
            @SerializedName("name")
            val name: String?
        )
    }
}


fun NewsDto.toNewsModel(): NewsModel{

    return NewsModel(
        articles = this.articles,
        status = this.status,
        totalResults = this.totalResults
    )
}

