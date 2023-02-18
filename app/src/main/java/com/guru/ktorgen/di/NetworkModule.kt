package com.guru.ktorgen.di

import com.guru.ktorgen.data.remote.KtorFactory
import com.guru.ktorgen.data.remote.PlaceHolderService
import com.guru.ktorgen.data.remote.PlaceHolderServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun providePlaceHolderService(): PlaceHolderService {
        return PlaceHolderServiceImpl(
            client = KtorFactory.create()
        )
    }
}