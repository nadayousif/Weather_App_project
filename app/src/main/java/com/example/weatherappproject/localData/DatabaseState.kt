package com.example.weatherappproject.localData

import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData


sealed class DatabaseState{
    class Success (val data: List<FavoriteAddress>): DatabaseState()
    class Failure(val msg:Throwable): DatabaseState()
    object Loading: DatabaseState()
}