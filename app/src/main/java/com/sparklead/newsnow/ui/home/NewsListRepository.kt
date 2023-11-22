package com.sparklead.newsnow.ui.home

import com.sparklead.newsnow.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsListRepository {

    suspend fun getAllNewsArticle() : Flow<List<Article>>

}