package com.example.cryptotrackerapp.repository

import androidx.lifecycle.LiveData
import com.example.cryptotrackerapp.model.Coin
import com.example.cryptotrackerapp.model.Data
import com.example.cryptotrackerapp.model.TimeSery
import com.example.cryptotrackerapp.utils.Resource

interface MainRepository {
    suspend fun getCoinDataFromRetrofit():Resource<Coin>
    suspend fun insertIntoDb(coin:Coin)
    fun getCoinData():LiveData<Data>
    fun getCoinTimeData():LiveData<List<TimeSery>>
}