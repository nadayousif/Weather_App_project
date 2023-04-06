package com.example.weatherappproject.repositary

import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData
import retrofit2.Response

interface RepositaryInterface {
    suspend fun getWeatherOverNetwork(lat:Double,lon:Double,language:String): Response<WeatherData>

    //Weather
    suspend fun getWeatherDataFromDB(): WeatherData?
    suspend fun insertOrUpdateWeatherData(weatherData: WeatherData)
    //Favorites
    fun getAllFavoriteAddresses(): List<FavoriteAddress>

    suspend fun insertFavoriteAddress(address: FavoriteAddress)
    suspend fun deleteFavoriteAddress(address: FavoriteAddress)
}