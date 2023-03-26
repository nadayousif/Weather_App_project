package com.example.weatherappproject.repositary

import com.example.weatherappproject.model.WeatherData
import retrofit2.Response

interface RepositaryInterface {
    suspend fun getWeatherOverNetwork(lat:Double,lon:Double,language:String): Response<WeatherData>
}