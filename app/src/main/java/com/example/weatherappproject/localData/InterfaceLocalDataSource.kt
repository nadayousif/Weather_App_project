package com.example.weatherappproject.localData

import androidx.lifecycle.LiveData
import com.example.weatherappproject.model.Alert
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData
import kotlinx.coroutines.flow.Flow


interface InterfaceLocalDataSource {
    //Weather
     fun getWeatherDataFromDB(): Flow<WeatherData?>
    suspend fun insertOrUpdateWeatherData(weatherData: WeatherData)

    //Favorites
    fun getAllFavoriteAddresses(): Flow<List<FavoriteAddress>>
    suspend fun insertFavoriteAddress(address: FavoriteAddress)
    suspend fun deleteFavoriteAddress(address: FavoriteAddress)

    fun getAlerts(): Flow<List<Alert>>
    suspend fun insertAlert(alert: Alert)
    suspend fun deleteAlert(alert: Alert)

}