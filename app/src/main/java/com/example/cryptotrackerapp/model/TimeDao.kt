package com.example.cryptotrackerapp.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeData(data: TimeSery)

    @Query("SELECT * FROM time_table")
    fun getAllData(): LiveData<List<TimeSery>>

}