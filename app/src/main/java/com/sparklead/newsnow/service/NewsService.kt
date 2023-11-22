package com.sparklead.newsnow.service

import com.sparklead.newsnow.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsService {

    suspend fun getAllNewsArticle() : List<Article>

}