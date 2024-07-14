package com.example.fetchrewardsassignment

import retrofit2.http.GET

interface ItemApiService {
    // Please NOTE the JSON list isn't defined with a label and so retrieval is done on by ItemData
    @GET("hiring.json")
    suspend fun getItems(): List<ItemData>
}
