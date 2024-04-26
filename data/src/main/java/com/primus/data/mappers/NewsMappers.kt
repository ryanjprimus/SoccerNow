package com.primus.data.mappers

import com.primus.data.models.news.ArticleDTO
import com.primus.data.models.news.NewsDTO
import com.primus.data.models.news.SourceDTO
import com.primus.domain.models.Article
import com.primus.domain.models.News
import com.primus.domain.models.Source

fun NewsDTO.toNews(): News {
    return News(
        articles = articles?.map { it.toArticle() } ?: emptyList(),
        status = status ?: "",
        totalResults = totalResults ?: -1
    )
}

fun ArticleDTO.toArticle(): Article {
    return Article(
        author = author ?: "",
        content = content ?: "",
        description = description ?: "",
        publishedAt = publishedAt ?: "",
        source = this.source.toSource(),
        title = title ?: "",
        url = url ?: "",
        urlToImage = urlToImage ?: ""
    )
}

fun SourceDTO?.toSource(): Source {
    return Source(
        id = this?.id ?: "",
        name = this?.name ?: ""
    )
}