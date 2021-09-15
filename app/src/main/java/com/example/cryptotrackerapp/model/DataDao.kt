package com.example.cryptotrackerapp.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: Data)

    @Query("SELECT * FROM data_table WHERE id=:inputId")
    fun getAllData(inputId:Int):LiveData<Data>
}