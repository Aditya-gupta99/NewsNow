package com.sparklead.newsnow.di

import com.sparklead.newsnow.service.NewsService
import com.sparklead.newsnow.service.NotificationService
import com.sparklead.newsnow.serviceImp.NewsServiceImp
import com.sparklead.newsnow.serviceImp.NotificationServiceImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApplicationModule {

    @Provides
    @Singleton
    fun providesNewsService() : NewsService = NewsServiceImp()

    @Provides
    @Singleton
    fun providesNotificationService() : NotificationService = NotificationServiceImp()
}