package com.example.weatherappproject.localData

import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData
import retrofit2.Response

sealed class ApiState {
    class Success (val data: Response<WeatherData>): ApiState()
    class Failure(val msg:Throwable): ApiState()
    object Loading: ApiState()
}