package com.cedacri.arete.di

import com.cedacri.arete.data.remote.api.AuthApi
import com.cedacri.arete.data.remote.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        AuthInterceptor(get())
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("http://10.24.48.70/AreteAPI/api/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<AuthApi> {
        get<Retrofit>().create(AuthApi::class.java)
    }
}