package com.example.cryptotrackerapp.apiservice

import com.example.cryptotrackerapp.model.Coin
import com.example.cryptotrackerapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(Constants.end_point_url)
    suspend fun getCoinData(
        @Query("data")data:String,
        @Query("key")key:String,
        @Query("symbol")symbol:String
    ):Response<Coin>

}