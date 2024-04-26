package com.primus.data.repository

import com.primus.data.api.NewsApi
import com.primus.data.mappers.toNews
import com.primus.data.retrofit_errors_handler.RetrofitErrorsHandler
import com.primus.domain.models.News
import com.primus.domain.repository.NewsRepository
import com.primus.utils.Resource

class NewsRepositoryImpl(
    private val newsApi: NewsApi,
    private val retrofitErrorsHandler: RetrofitErrorsHandler
) : NewsRepository {

    override suspend fun getNews(q: String): Resource<News> {
        return retrofitErrorsHandler.executeSafely {
            val response = newsApi.getAllArticlesByQuery(query = q)
            if (response.isSuccessful && response.code() == 200) {
                Resource.Success(response.body()?.toNews() ?: News())
            } else {
                retrofitErrorsHandler.responseFailureHandler(response)
            }
        }
    }
}