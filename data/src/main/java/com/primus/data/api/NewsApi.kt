package com.primus.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import com.primus.data.models.news.NewsDTO
import com.primus.utils.Constants

interface NewsApi {

    @GET("everything")
    @Headers("X-Api-Key: ${Constants.NEW_API_KEY}")
    suspend fun getAllArticlesByQuery(
        @Query("q") query: String,
        @Query("language") language: String = "en",
        @Query("sortBy") sortBy: String = "relevancy",
        @Query("pageSize") pageSize: Int = 10
    ): Response<NewsDTO>

}