package com.example.weatherappproject.remoteDataSource

import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData
import com.example.weatherappproject.remoteData.InterfaceRemoteDataSource
import retrofit2.Response

class FakeRemoteDataSource(var respose: WeatherData) : InterfaceRemoteDataSource {
    override suspend fun getWeatherDataOnline(
        lat: Double,
        lon: Double,
        language: String,
        unit: String
    ): Response<WeatherData> {
        respose.apply {
            this.lat=lat
            this.lon=lon
        }
        return Response.success(respose)
    }
}