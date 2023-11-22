package com.sparklead.newsnow.di

import com.sparklead.newsnow.service.NewsService
import com.sparklead.newsnow.serviceImp.NewsServiceImp
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
}