package com.example.fetchrewardsassignment

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object ItemAPIProvider {
    private val json =
        Json {
            ignoreUnknownKeys = true
        }

    val client: ItemApiService by lazy {
        val okHttpClient =
            OkHttpClient
                .Builder()
                .build()

        val contentType = "application/json".toMediaType()
        // retrofit is a typesafe http client for android
        val retrofit =
            Retrofit
                .Builder()
                .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
                .addConverterFactory(json.asConverterFactory(contentType))
                .client(okHttpClient)
                .build()

        retrofit.create(ItemApiService::class.java)
    }
}
