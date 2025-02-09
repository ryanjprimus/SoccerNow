package com.primus.data.models.news

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsDTO(
    @Json(name = "articles") val articles: List<ArticleDTO>?,
    @Json(name = "status") val status: String?,
    @Json(name = "totalResults") val totalResults: Int?
)

@JsonClass(generateAdapter = true)
data class SourceDTO(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String?
)

@JsonClass(generateAdapter = true)
data class ArticleDTO(
    @Json(name = "author") val author: String?,
    @Json(name = "content") val content: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "publishedAt") val publishedAt: String?,
    @Json(name = "source") val source: SourceDTO?,
    @Json(name = "title") val title: String?,
    @Json(name = "url") val url: String?,
    @Json(name = "urlToImage") val urlToImage: String?
)