package com.example.apptemplate.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitModule {
    private fun okHttpClient(): OkHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    fun provideMoshiBuilder(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    fun provideRetrofitBuilder(url: String, moshi: Moshi): Retrofit.Builder = Retrofit.Builder()
        .client(okHttpClient())
        .baseUrl(url)
        .addConverterFactory(MoshiConverterFactory.create(moshi))

    inline fun <reified T> provideRemoteService(retrofit: Retrofit.Builder): T =
        retrofit.build().create(T::class.java)

}