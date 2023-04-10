package com.example.weatherappproject.database

import com.example.weatherappproject.localData.InterfaceLocalDataSource
import com.example.weatherappproject.model.Alert
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource(var weatherDataList: MutableList<WeatherData> = mutableListOf<WeatherData>()):
    InterfaceLocalDataSource {
    override fun getWeatherDataFromDB(): Flow<WeatherData?> {
        weatherDataList?.let {flow{emit(it)} }
        TODO("Not yet implemented")
    }

    override suspend fun insertOrUpdateWeatherData(weatherData: WeatherData) {
        weatherDataList.add(weatherData)
        TODO("Not yet implemented")
    }

    override fun getAllFavoriteAddresses(): Flow<List<FavoriteAddress>> {

        TODO("Not yet implemented")
    }

    override suspend fun insertFavoriteAddress(address: FavoriteAddress) {

    }

    override suspend fun deleteFavoriteAddress(address: FavoriteAddress) {

    }

    override fun getAlerts(): Flow<List<Alert>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(alert: Alert) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alert: Alert) {
        TODO("Not yet implemented")
    }
}