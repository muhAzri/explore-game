package com.zrifapps.exploregame.core.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.zrifapps.exploregame.core.common.manager.ConfigManager
import com.zrifapps.exploregame.core.data.database.AppDatabase
import com.zrifapps.exploregame.core.data.database.SecureDatabaseFactory
import com.zrifapps.exploregame.core.data.database.dao.FavouriteGameDao
import com.zrifapps.exploregame.core.data.repository.FavouriteGameRepositoryImpl
import com.zrifapps.exploregame.core.domain.repository.FavouriteGameRepository
import com.zrifapps.exploregame.core.network.ApiKeyInterceptor
import com.zrifapps.exploregame.core.network.CertificatePinningInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


    @Provides
    @Singleton
    fun provideLoggingInterceptor(config: ConfigManager): HttpLoggingInterceptor {
        val loggingInterceptor = if (config.debug) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(config: ConfigManager): ApiKeyInterceptor {
        return ApiKeyInterceptor(configManager = config)
    }
    
    @Provides
    @Singleton
    fun provideCertificatePinningInterceptor(@ApplicationContext context: Context): CertificatePinningInterceptor {
        return CertificatePinningInterceptor(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        apiKeyInterceptor: ApiKeyInterceptor,
        certificatePinningInterceptor: CertificatePinningInterceptor,
    ): OkHttpClient {
        val baseClient = OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
            
        return certificatePinningInterceptor.createSecureOkHttpClient(baseClient)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        config: ConfigManager,
        moshi: Moshi,
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(config.baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideSecureDatabaseFactory(@ApplicationContext context: Context): SecureDatabaseFactory {
        return SecureDatabaseFactory(context)
    }
    
    @Provides
    @Singleton
    fun provideAppDatabase(secureDatabaseFactory: SecureDatabaseFactory): AppDatabase {
        return secureDatabaseFactory.createAppDatabase()
    }

    @Provides
    fun provideFavouriteGameDao(database: AppDatabase): FavouriteGameDao {
        return database.favouriteGameDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Binds
    @Singleton
    abstract fun bindFavouriteGameRepository(
        favouriteGameRepositoryImpl: FavouriteGameRepositoryImpl
    ): FavouriteGameRepository
}
