package com.example.myikanhiascapstone.api

import com.example.myikanhiascapstone.data.FishResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("api/categories")
    suspend fun getDataFish(
        @Query("filters[name][\$eq]") name: String
    ): FishResponse
}