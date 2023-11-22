package com.sparklead.newsnow.ui.home

import com.sparklead.newsnow.model.Article
import com.sparklead.newsnow.service.NewsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsListRepositoryImp @Inject constructor(private val service: NewsService) :
    NewsListRepository {

    override suspend fun getAllNewsArticle(): Flow<List<Article>> {
        return flow {
            emit(service.getAllNewsArticle())
        }
    }
}