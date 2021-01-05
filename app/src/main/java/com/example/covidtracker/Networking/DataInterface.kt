package com.example.covidtracker.Networking

import com.example.covidtracker.ApiData.CasesTimeSery
import com.example.covidtracker.ApiData.CovidData
import retrofit2.Response
import retrofit2.http.GET

interface DataInterface {
    @GET("data.json")
    suspend fun getTotalData(): Response<CovidData>

    @GET("data.json")
    suspend fun getTodaysData(): Response<CasesTimeSery>

}