package com.sparklead.newsnow.di

import com.sparklead.newsnow.service.NewsService
import com.sparklead.newsnow.service.NotificationService
import com.sparklead.newsnow.ui.home.NewsListRepository
import com.sparklead.newsnow.ui.home.NewsListRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNewsListRepository(
        service: NewsService,
        notificationService: NotificationService
    ): NewsListRepository =
        NewsListRepositoryImp(service, notificationService)

}