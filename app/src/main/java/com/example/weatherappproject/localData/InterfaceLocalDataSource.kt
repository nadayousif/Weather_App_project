package com.example.weatherappproject.localData

import androidx.lifecycle.LiveData
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData


interface InterfaceLocalDataSource {
    //Weather
    suspend fun getWeatherDataFromDB(): WeatherData?
    suspend fun insertOrUpdateWeatherData(weatherData: WeatherData)

    //Favorites
    fun getAllFavoriteAddresses(): List<FavoriteAddress>
    suspend fun insertFavoriteAddress(address: FavoriteAddress)
    suspend fun deleteFavoriteAddress(address: FavoriteAddress)

}