package com.example.cryptotrackerapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.cryptotrackerapp.R
import com.example.cryptotrackerapp.databinding.ActivityMainBinding
import com.example.cryptotrackerapp.viewmodel.MainViewModel
import com.github.mikephil.charting.charts.LineChart
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        obeserveApiCall()
        observeLocalDb()
    }

    private fun observeLocalDb() {
        viewModel.liveDataDataValue?.observe(this,{ data->
            val currentPrice = "$"+data.price.toInt().toString()
            binding.coinValue.text = currentPrice
        })
        viewModel.mediatorLineChartMax.observe(this, { list ->
            if(list!=null && binding.lineChartMax.data == null){
                binding.lineChartMax.invalidate()
                binding.lineChartMax.data = list
                setUpChart( binding.lineChartMax)
            }
        })

        viewModel.mediatorLineChartMin.observe(this,{data->
            if(data!=null && binding.lineChartMin.data == null){
                binding.lineChartMin.invalidate()
                binding.lineChartMin.data = data
                setUpChart(binding.lineChartMin)
            }
        })
    }

    private fun obeserveApiCall() {
        viewModel.liveDataCoinValue.observe(this,{response->
            when(response){
                is MainViewModel.IncomingCoinData.Success-> {
                    binding.progressBar.visibility = View.GONE
                    viewModel.insertIntoDb(response.coin)
                }
                is MainViewModel.IncomingCoinData.Failure-> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext,response.errorMessage,Toast.LENGTH_SHORT).show()
                }
                is MainViewModel.IncomingCoinData.Loading-> binding.progressBar.visibility = View.GONE
                else->Unit
            }
        })
    }

    private fun setUpChart(chart: LineChart) {
        chart.isDragEnabled = true
        chart.setScaleEnabled(false)
        chart.legend.textColor = ContextCompat.getColor(applicationContext, R.color.white)
        chart.axisLeft.textColor = ContextCompat.getColor(applicationContext, R.color.white)
        chart.xAxis.textColor = ContextCompat.getColor(applicationContext, R.color.white)
        chart.axisRight.setDrawLabels(false)
        chart.lineData.setValueTextSize(0f)
        chart.description.text = ""
    }


}
