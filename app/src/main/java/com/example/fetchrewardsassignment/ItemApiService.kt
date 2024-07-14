package com.example.fetchrewardsassignment

import retrofit2.http.GET

interface ItemApiService {
    @GET("hiring.json")
    suspend fun getItems(): List<ItemData>
}
