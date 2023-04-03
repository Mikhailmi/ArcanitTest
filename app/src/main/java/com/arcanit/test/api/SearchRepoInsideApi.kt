package com.arcanit.test.api

import com.arcanit.test.data.DirectoryData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object RetrofitDirectoryServices {

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

    val searchDirInside: SearchDirInside = retrofit.create(
        SearchDirInside::class.java
    )

    val searchDirInsideDir: SearchDirInsideDir = retrofit.create(
        SearchDirInsideDir::class.java
    )
}

interface SearchDirInside {
    @GET("/repos/{string1}/{string2}/{string3}")
    suspend fun getResponseData(
        @Path("string1") string1: String,
        @Path("string2") string2: String,
        @Path("string3") string3: String
    ): List<DirectoryData>
}

interface SearchDirInsideDir {
    @GET("/repos/{string1}/{string2}/{string3}/{string4}/{string5}/{string6}/{string7}/{string8}")
    suspend fun getResponseData(
        @Path("string1") string1: String,
        @Path("string2") string2: String,
        @Path("string3") string3: String,
        @Path("string4") string4: String,
        @Path("string5") string5: String,
        @Path("string6") string6: String,
        @Path("string7") string7: String,
        @Path("string8") string8: String,
        @Query("ref") string9: String,
    ): List<DirectoryData>
}


