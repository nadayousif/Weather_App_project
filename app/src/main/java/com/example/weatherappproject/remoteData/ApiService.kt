package com.example.weatherappproject.remoteData

import com.example.weatherappproject.model.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    companion object{

        const val BASE_URL_WEATHER = "https://api.openweathermap.org/"
    }
    @GET("data/2.5/onecall")
    suspend fun getWeatherDataOnline(
        @Query("appid") apiKey: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") language: String,
        @Query("units") unit: String

    ): Response<WeatherData>

}