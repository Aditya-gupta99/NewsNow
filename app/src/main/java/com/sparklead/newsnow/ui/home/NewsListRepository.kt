package com.sparklead.newsnow.ui.home

import com.sparklead.newsnow.model.Article
import com.sparklead.newsnow.model.PushNotification
import kotlinx.coroutines.flow.Flow

interface NewsListRepository {

    suspend fun getAllNewsArticle() : Flow<List<Article>>

    suspend fun sendNotification(notification: PushNotification)

}