package com.example.weatherappproject.remoteData

import com.example.weatherappproject.model.WeatherData
import retrofit2.Response


interface InterfaceRemoteDataSource {
    suspend fun getWeatherDataOnline(lat: Double, lon: Double, language: String): Response<WeatherData>


}