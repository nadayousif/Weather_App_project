package com.example.weatherappproject.remoteData

import android.content.Context
import com.example.weatherappproject.model.WeatherData
import retrofit2.Response

class RemoteDataSource() : InterfaceRemoteDataSource {


    private val weatherApiKey = "6cd7f954ec06ef046ce2fefb293967ed"


    companion object {
        @Volatile
        private var remoteDataSourceInstance: RemoteDataSource? = null

        @Synchronized
        fun getInstance(): RemoteDataSource {
            if (remoteDataSourceInstance == null) {
                remoteDataSourceInstance = RemoteDataSource()
            }
            return remoteDataSourceInstance!!
        }
    }

    private val weatherRetrofit = RetrofitHelper.getRetrofitInstance(ApiService.BASE_URL_WEATHER)
    private val weatherApiService = weatherRetrofit.create(ApiService::class.java)

    override suspend fun getWeatherDataOnline(lat: Double, lon: Double, language: String): Response<WeatherData> {

        return weatherApiService.getWeatherDataOnline(weatherApiKey, lat, lon, language)

    }



}
