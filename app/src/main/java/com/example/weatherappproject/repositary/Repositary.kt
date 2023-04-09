package com.example.weatherappproject.repositary

import android.content.SharedPreferences
import com.example.weatherappproject.localData.InterfaceLocalDataSource
import com.example.weatherappproject.localData.LocalDataSource
import com.example.weatherappproject.model.Alert
import com.example.weatherappproject.model.AlertSettings
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData
import com.example.weatherappproject.remoteData.RemoteDataSource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class Repositary (var remoteSource: RemoteDataSource,private val localDataSource: LocalDataSource,var sharedPreferences: SharedPreferences): RepositaryInterface{

    private var sharedPreferencesedit = sharedPreferences.edit()
    val ALERTSETTINGS = "ALERTSETTINGS"
    companion object{
        @Volatile
        private var INSTANCE: Repositary? = null
        fun getInstance (
            remoteSource: RemoteDataSource,
            localDataSource: LocalDataSource,
            sharedPreferences: SharedPreferences
        ): Repositary{
            return INSTANCE ?: synchronized(this) {
                val temp = Repositary(
                    remoteSource,localDataSource,
                    sharedPreferences)
                INSTANCE = temp
                temp }
        }
    }

    override  fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,
        language: String,
        unit:String

    ): Flow<Response<WeatherData>> {
        return flow { emit(remoteSource.getWeatherDataOnline(lat,lon,language,unit))  }
    }

    //Weather
    override  fun getWeatherDataFromDB(): Flow<WeatherData?>{
        return localDataSource.getWeatherDataFromDB()
    }
    override suspend fun insertOrUpdateWeatherData(weatherData: WeatherData) {
        localDataSource.insertOrUpdateWeatherData(weatherData)
    }

    //Favorites
    override fun getAllFavoriteAddresses(): Flow<List<FavoriteAddress>> {
        return localDataSource.getAllFavoriteAddresses()
    }
    override suspend fun insertFavoriteAddress(address: FavoriteAddress){
        localDataSource.insertFavoriteAddress(address)
    }
    override suspend fun deleteFavoriteAddress(address: FavoriteAddress){
        localDataSource.deleteFavoriteAddress(address)
    }

    override fun getAlerts(): Flow<List<Alert>> {
        return localDataSource.getAlerts()
    }

    override suspend fun insertAlert(alert: Alert) {
        localDataSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        localDataSource.deleteAlert(alert)
    }

    override fun saveAlertSettings(alertSettings: AlertSettings) {
        sharedPreferencesedit = sharedPreferences.edit()
        sharedPreferencesedit.putString(ALERTSETTINGS, Gson().toJson(alertSettings))
        sharedPreferencesedit.commit()
    }

    override fun getAlertSettings(): AlertSettings? {
        val settingsStr = sharedPreferences.getString(ALERTSETTINGS, null)
        var alertSettings: AlertSettings? = AlertSettings()
        if (settingsStr != null) {
            alertSettings = Gson().fromJson(settingsStr, AlertSettings::class.java)
        }
        return alertSettings
    }

}