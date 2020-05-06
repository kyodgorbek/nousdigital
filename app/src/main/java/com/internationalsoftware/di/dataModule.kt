package com.internationalsoftware.di

import com.core.converter.AppMoshiConverter
import com.core.converter.MoshiConverter
import com.data.BuildConfig
import com.data.features.item.ItemRepositoryImpl
import com.data.features.item.remote.ItemRemoteSource
import com.data.helpers.BuildInfoHelper
import com.data.helpers.ServiceFactory
import com.data.helpers.WebService
import com.domain.features.item.ItemRepository
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val KOIN_WEB_API_URL = "webApiUrl"
const val KOIN_IS_DEBUG = "isDebug"
const val KOIN_MOSHI = "koinMoshi"

const val KOIN_OKHTTP_ISOFTWARE = "okHttpISoftware"
const val KOIN_RETROFIT_ISOFTWARE = "retrofitISoftware"
const val KOIN_OKHTTP_TIMEOUT_SECONDS = "okHttpTimeoutSeconds"

val dataModule = applicationContext {

    // ========= GENERAL SETTINGS

    bean(KOIN_IS_DEBUG) {
        get<BuildInfoHelper>().isDebug
    }

    bean(KOIN_WEB_API_URL) {
        get<BuildInfoHelper>().webApiUrl
    }

    bean (KOIN_MOSHI){
        Moshi.Builder().build()
    }

    bean {
        WebService
    }

    bean {
        AppMoshiConverter(get(KOIN_MOSHI)) as MoshiConverter
    }

    bean {
        BuildInfoHelper()
    }

    bean {
        get<BuildConfig>()
    }

    bean {
        GsonBuilder()
    }

    bean {
        GsonConverterFactory.create(get<GsonBuilder>().serializeNulls().create())
    }

    bean {
        MoshiConverterFactory.create()
    }

    bean(KOIN_OKHTTP_TIMEOUT_SECONDS) {
        30L // seconds
    }

    // ========= RETROFIT (FIREBASE)

    bean {
        StethoInterceptor()
    }

    bean {
        CoroutineCallAdapterFactory()
    }

    bean {
        HttpLoggingInterceptor().apply {
            level =
                if (get(KOIN_IS_DEBUG)) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
        }
    }

    bean(KOIN_OKHTTP_ISOFTWARE) {
        OkHttpClient
            .Builder()
            .addNetworkInterceptor(get<StethoInterceptor>())
            .addNetworkInterceptor(get<HttpLoggingInterceptor>())
            .readTimeout(get(KOIN_OKHTTP_TIMEOUT_SECONDS), TimeUnit.SECONDS)
            .writeTimeout(get(KOIN_OKHTTP_TIMEOUT_SECONDS), TimeUnit.SECONDS)
            .connectTimeout(get(KOIN_OKHTTP_TIMEOUT_SECONDS), TimeUnit.SECONDS)
            .build()
    }

    bean(KOIN_RETROFIT_ISOFTWARE) {
        Retrofit
            .Builder()
            .baseUrl(get<String>(KOIN_WEB_API_URL))
            .addCallAdapterFactory(get<CoroutineCallAdapterFactory>())
            .addConverterFactory(get<GsonConverterFactory>())
            .client(get(KOIN_OKHTTP_ISOFTWARE))
            .build()
    }

//     ========= ITEM

    bean {
        ItemRepositoryImpl(
            get<ItemRemoteSource>(),
            get<WebService>(),
            get<MoshiConverter>()
        ) as ItemRepository
    }

    bean {
        ServiceFactory.createService(
            get(KOIN_RETROFIT_ISOFTWARE),
            ItemRemoteSource::class.java
        )
    }
}