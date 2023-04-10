package com.example.weatherappproject.repositary

import com.example.weatherappproject.model.Alert
import com.example.weatherappproject.model.AlertSettings
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FakeRepositary(var favs: MutableList<FavoriteAddress>? = mutableListOf<FavoriteAddress>()) :RepositaryInterface{
    override fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,
        language: String,
        unit: String
    ): Flow<Response<WeatherData>> {
        TODO("Not yet implemented")
    }

    override fun getWeatherDataFromDB(): Flow<WeatherData?> {
        TODO("Not yet implemented")
    }

    override suspend fun insertOrUpdateWeatherData(weatherData: WeatherData) {
        TODO("Not yet implemented")
    }

    override fun getAllFavoriteAddresses(): Flow<List<FavoriteAddress>> {

        return favs?.let { flow{emit(it)} }!!
        TODO("Not yet implemented")
    }

    override suspend fun insertFavoriteAddress(address: FavoriteAddress) {
        favs?.add(address)
    }

    override suspend fun deleteFavoriteAddress(address: FavoriteAddress) {
        favs?.remove(address)
        TODO("Not yet implemented")
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

    override fun saveAlertSettings(alertSettings: AlertSettings) {
        TODO("Not yet implemented")
    }

    override fun getAlertSettings(): AlertSettings? {
        TODO("Not yet implemented")
    }
}