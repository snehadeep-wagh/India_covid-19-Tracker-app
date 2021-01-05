package com.example.covidtracker.ApiData

data class CovidData(
    val cases_time_series: List<CasesTimeSery>,
    val statewise: List<Statewise>,
)