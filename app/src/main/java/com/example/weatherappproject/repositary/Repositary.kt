package com.example.weatherappproject.repositary

import com.example.weatherappproject.model.WeatherData
import com.example.weatherappproject.remoteData.RemoteDataSource
import retrofit2.Response

class Repositary (var remoteSource: RemoteDataSource): RepositaryInterface{


    companion object{
        @Volatile
        private var INSTANCE: Repositary? = null
        fun getInstance (
            remoteSource: RemoteDataSource
        ): Repositary{
            return INSTANCE ?: synchronized(this) {
                val temp = Repositary(
                    remoteSource)
                INSTANCE = temp
                temp }
        }
    }

    override suspend fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,
        language: String,

    ): Response<WeatherData> {
        return remoteSource.getWeatherDataOnline(lat,lon,language)
    }

}