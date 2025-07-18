package com.zrifapps.exploregame.di

import android.content.Context
import com.zrifapps.exploregame.BuildConfig
import com.zrifapps.exploregame.core.common.manager.ConfigManager
import com.zrifapps.exploregame.features.DynamicModuleManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConfigProviderModule {

    @Provides
    @Singleton
    fun provideConfigManager(): ConfigManager {
        return ConfigManager(
            debug = BuildConfig.DEBUG,
            baseUrl = BuildConfig.BASE_URL,
            apiKey = BuildConfig.API_KEY
        )
    }

    @Provides
    @Singleton
    fun provideDynamicModuleManager(@ApplicationContext context: Context): DynamicModuleManager {
        return DynamicModuleManager(context)
    }
}
