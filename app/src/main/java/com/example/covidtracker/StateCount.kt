@file:Suppress("DEPRECATION")

package com.example.covidtracker

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covidtracker.ApiData.Statewise
import com.example.covidtracker.Networking.Client
import com.example.covidtracker.RecyclerView.CustomAdapter
import com.example.covidtracker.databinding.ActivityStateCountBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StateCount : AppCompatActivity() {
    private lateinit var binding: ActivityStateCountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_state_count)

        //adapter object
        binding.StateRecyclerView.layoutManager = LinearLayoutManager(this)


        //retrofit object ----

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected)
        {
            createClient()
        }
        else
        {
            showDialog()
        }

        //Refreshing ----
        binding.swipeLayoutId.setOnRefreshListener {
            if(binding.swipeLayoutId.isRefreshing)
            {
                createClient()
                binding.swipeLayoutId.isRefreshing = false
            }
            else
            {
                binding.swipeLayoutId.isRefreshing = false
            }
        }

    }

//    private fun updateTime(time: )
//    {
//
//    }
//

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

    private fun createClient()
    {
        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO){
                Client.api.getTotalData()
            }
            withContext(Dispatchers.IO){
                Client.api.getTodaysData()
            }

            if(response.isSuccessful)
            {
                response.let {
                    launch(Dispatchers.Main) {
                        val stateAdapter = CustomAdapter()
                        val stateWiseDataList: MutableList<Statewise> = it.body()!!.statewise as MutableList<Statewise>
                        stateWiseDataList.removeAt(0)
                        stateAdapter.stateDataList = stateWiseDataList
                        binding.StateRecyclerView.adapter = stateAdapter

                        // set date and time ----
                        val list = response.body()!!.statewise as MutableList
                        val firstPos = list[0]
                        val date = firstPos.lastupdatedtime.substring(1..9)
                        val time = firstPos.lastupdatedtime.substring(11..18)

                        binding.ActualDate.text = date
                        binding.ActualTime.text = time
                    }
                }
            }



        }
    }
}