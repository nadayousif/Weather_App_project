package com.example.weatherappproject.remoteData

import android.content.Context
import com.example.weatherappproject.model.WeatherData
import retrofit2.Response

class RemoteDataSource() : InterfaceRemoteDataSource {


    private val weatherApiKey = "4a059725f93489b95183bbcb8c6829b9"


    companion object {
        @Volatile
        private var remoteDataSourceInstance: RemoteDataSource? = null

        @Synchronized
        fun getInstance(context: Context): RemoteDataSource {
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