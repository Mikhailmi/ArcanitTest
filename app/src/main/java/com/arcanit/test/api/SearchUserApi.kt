package com.arcanit.test.api

import com.arcanit.test.data.ResponseUserData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object RetrofitUserServices {
    private const val BASE_URL = "https://api.github.com"
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit
        .Builder()
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().also {
                    it.level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        )
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val searchUsersApi: SearchUsersApi = retrofit.create(
        SearchUsersApi::class.java
    )
}

interface SearchUsersApi {
    @GET("/search/users?")
    suspend fun getResponseData(
        @Query("q") request: String,
        @Query("order") order: String,
        @Query("per_page") per_page: Int,
        @Query("page") page: Int
    ): ResponseUserData
}