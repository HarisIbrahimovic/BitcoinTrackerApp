package com.example.cryptotrackerapp.viewmodel

import androidx.lifecycle.*
import com.example.cryptotrackerapp.model.Coin
import com.example.cryptotrackerapp.model.Data
import com.example.cryptotrackerapp.model.TimeSery
import com.example.cryptotrackerapp.repository.MainRepository
import com.example.cryptotrackerapp.utils.Resource
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(private val mainRepository: MainRepository) :ViewModel() {

    private val coinValue = MutableLiveData<IncomingCoinData>()
    val liveDataCoinValue : LiveData<IncomingCoinData> = coinValue

    var liveDataDataValue : LiveData<Data>? =null
    private lateinit var liveDataTimeValue : LiveData<List<TimeSery>>
    
    val mediatorLineChartMin = MediatorLiveData<LineData>()
    val mediatorLineChartMax = MediatorLiveData<LineData>()

    init {
        if(coinValue.value==null){
            getCoinData()
            makeApiCall()
            getMinValue()
            getMaxValue()
        }
    }

    private fun makeApiCall() = viewModelScope.launch(Dispatchers.IO) {
        coinValue.postValue(IncomingCoinData.Loading)
        when(val response = mainRepository.getCoinDataFromRetrofit()){
            is Resource.Success -> coinValue.postValue(IncomingCoinData.Success(response.data!!))
            is Resource.Error -> coinValue.postValue(IncomingCoinData.Failure(response.message!!))
        }
    }

    private fun getCoinData() {
        liveDataDataValue = mainRepository.getCoinData()
        liveDataTimeValue = mainRepository.getCoinTimeData()
    }

    fun insertIntoDb(coin:Coin)=viewModelScope.launch(Dispatchers.IO) {
        mainRepository.insertIntoDb(coin)
    }

    private fun getMaxValue() {
        mediatorLineChartMax.addSource(liveDataTimeValue) {list->
            val maxEntries: ArrayList<Entry> = ArrayList()
            var i = 1
            for (time in list) {
                maxEntries.add(Entry(i.toFloat(), time.high.toFloat()))
                i++
            }
            val set = LineDataSet(maxEntries, "High")
            val dataSets: ArrayList<ILineDataSet> = ArrayList()
            set.fillAlpha = 100
            dataSets.add(set)
            val data = LineData(dataSets)
            mediatorLineChartMax.value = data
            mediatorLineChartMax.removeSource(liveDataTimeValue)
        }

    }

    private fun getMinValue() {
        mediatorLineChartMin.addSource(liveDataTimeValue) { list ->
            if (list != null) {
                val minEntries: ArrayList<Entry> = ArrayList()
                var i = 1
                for (time in list){
                    minEntries.add(Entry(i.toFloat(), time.low.toFloat()))
                    i++
                }
                val set = LineDataSet(minEntries, "Low")
                val dataSets: ArrayList<ILineDataSet> = ArrayList()
                set.fillAlpha = 100
                dataSets.add(set)
                val data = LineData(dataSets)
                mediatorLineChartMin.value=data
                mediatorLineChartMin.removeSource(liveDataTimeValue)
            }
        }
    }

    sealed class IncomingCoinData{
        class Success(val coin: Coin):IncomingCoinData()
        class Failure(val errorMessage: String):IncomingCoinData()
        object Loading : IncomingCoinData()
    }

}