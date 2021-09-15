package com.example.cryptotrackerapp.repository

import androidx.lifecycle.LiveData
import com.example.cryptotrackerapp.apiservice.ApiService
import com.example.cryptotrackerapp.model.*
import com.example.cryptotrackerapp.utils.Constants
import com.example.cryptotrackerapp.utils.Resource
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(private val apiService: ApiService,private val timeDao: TimeDao,private val dataDao: DataDao) : MainRepository{

    override suspend fun getCoinDataFromRetrofit(): Resource<Coin> {
        return try {
            val response = apiService.getCoinData("assets",Constants.api_key,"BTC")
            val result = response.body()
            if(response.isSuccessful&&result!=null){
               Resource.Success(result)
            }else Resource.Error("Unknown error occurred")
        }catch (e:Exception){
            Resource.Error("Connection problems..")
        }
    }

    override suspend fun insertIntoDb(coin: Coin) {
        dataDao.insertData(coin.data[0])
        for(time in coin.data[0].timeSeries){
            timeDao.insertTimeData(time)
        }
    }

    override fun getCoinData(): LiveData<Data> {
        return dataDao.getAllData(1)
    }

    override fun getCoinTimeData(): LiveData<List<TimeSery>> {
        return timeDao.getAllData()
    }


}