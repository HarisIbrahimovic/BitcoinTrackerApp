package com.example.cryptotrackerapp.hilt

import android.content.Context
import androidx.room.Room
import com.example.cryptotrackerapp.apiservice.ApiService
import com.example.cryptotrackerapp.model.DataDao
import com.example.cryptotrackerapp.model.TimeDao
import com.example.cryptotrackerapp.repository.DefaultMainRepository
import com.example.cryptotrackerapp.repository.MainRepository
import com.example.cryptotrackerapp.utils.AppDatabase
import com.example.cryptotrackerapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance(): ApiService = Retrofit.Builder()
        .baseUrl(Constants.base_url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "AppDB"
        ).build()

    @Provides
    fun providesTimeDao(appDatabase: AppDatabase): TimeDao = appDatabase.timeDao()

    @Provides
    fun providesDataDao(appDatabase: AppDatabase): DataDao = appDatabase.dataDao()

    @Provides
    fun provideMainRepository(apiService: ApiService,timeDao: TimeDao,dataDao: DataDao):MainRepository = DefaultMainRepository(apiService,timeDao, dataDao)
}