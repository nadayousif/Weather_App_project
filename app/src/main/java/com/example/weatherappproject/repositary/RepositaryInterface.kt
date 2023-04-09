package com.example.weatherappproject.repositary

import com.example.weatherappproject.model.Alert
import com.example.weatherappproject.model.AlertSettings
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepositaryInterface {
     fun getWeatherOverNetwork(lat:Double,lon:Double,language:String,unit:String): Flow<Response<WeatherData>>

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
    fun saveAlertSettings(alertSettings: AlertSettings)
    fun getAlertSettings(): AlertSettings?
}