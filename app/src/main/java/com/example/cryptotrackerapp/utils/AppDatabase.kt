package com.example.cryptotrackerapp.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cryptotrackerapp.model.Data
import com.example.cryptotrackerapp.model.DataDao
import com.example.cryptotrackerapp.model.TimeDao
import com.example.cryptotrackerapp.model.TimeSery

@Database(entities = [Data::class,TimeSery::class],version = 1, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {
    abstract fun timeDao():TimeDao
    abstract fun dataDao():DataDao
}