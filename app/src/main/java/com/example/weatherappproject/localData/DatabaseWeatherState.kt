package com.example.weatherappproject.localData

import com.example.weatherappproject.model.WeatherData

sealed class DatabaseWeatherState {

    class SuccessHome(val data: WeatherData?): DatabaseWeatherState()
    class Failure(val msg:Throwable): DatabaseWeatherState()
    object Loading: DatabaseWeatherState()
}