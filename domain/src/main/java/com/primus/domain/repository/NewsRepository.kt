package com.primus.domain.repository

import com.primus.domain.models.News
import com.primus.utils.Resource

interface NewsRepository {

    suspend fun getNews(q: String): Resource<News>
}