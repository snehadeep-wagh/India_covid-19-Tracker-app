@file:Suppress("DEPRECATION")

package com.example.covidtracker

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.covidtracker.ApiData.CasesTimeSery
import com.example.covidtracker.ApiData.Statewise
import com.example.covidtracker.Networking.Client
import com.example.covidtracker.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_CovidTracker)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected)
        {
            createClient()
        }
        else
        {
           showDialog()
        }


    }

    // To move to stateCount activity ----
    fun stateCount(view: View) {
        Intent(view.context, StateCount::class.java).also {
            startActivity(it)
        }
    }


    // function for data update ----
    private fun createClient(){

        GlobalScope.launch(Dispatchers.Main) {
            val responseFromApi = withContext(Dispatchers.IO) { Client.api.getTotalData() }
            if (responseFromApi.isSuccessful) {
                responseFromApi.let {
                    val size = it.body()!!.cases_time_series.size - 1
                    val yesterday = it.body()!!.cases_time_series[size]
                    val todayTotal = it.body()!!.statewise[1]
                    launch(Dispatchers.Main) {
                       updateTodaysData(yesterday, todayTotal)

                    }

                }
            }

        }

    }

    private fun updateTodaysData(present: CasesTimeSery, todayTotal: Statewise) {

        binding.TotalCon.text = todayTotal.confirmed
        binding.TotalRec.text = todayTotal.recovered
        binding.TotalDec.text = todayTotal.deaths

        binding.TodayCon.text = present.dailyconfirmed
        binding.TodayRec.text = present.dailyrecovered
        binding.TodayDec.text = present.dailydeceased
        Log.i("State", todayTotal.lastupdatedtime)

    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_view)
        val yesBtn = dialog.findViewById(R.id.button) as Button
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}