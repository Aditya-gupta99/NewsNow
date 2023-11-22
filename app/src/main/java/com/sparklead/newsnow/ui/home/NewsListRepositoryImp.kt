package com.sparklead.newsnow.ui.home

import com.sparklead.newsnow.model.Article
import com.sparklead.newsnow.model.PushNotification
import com.sparklead.newsnow.service.NewsService
import com.sparklead.newsnow.service.NotificationService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsListRepositoryImp @Inject constructor(private val service: NewsService,private val notificationService: NotificationService) :
    NewsListRepository {

    override suspend fun getAllNewsArticle(): Flow<List<Article>> {
        return flow {
            emit(service.getAllNewsArticle())
        }
    }

    override suspend fun sendNotification(notification: PushNotification) {
        notificationService.pushNotification(notification)
    }

}