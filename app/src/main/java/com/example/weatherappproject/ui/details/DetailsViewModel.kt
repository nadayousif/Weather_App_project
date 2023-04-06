package com.example.weatherappproject.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappproject.model.WeatherData
import com.example.weatherappproject.repositary.RepositaryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class DetailsViewModel (private val repo: RepositaryInterface) : ViewModel() {

    private var _currentWeather = MutableLiveData<WeatherData>()
    var currentWeather: LiveData<WeatherData> = _currentWeather

    fun getWeatherFromApi(lat: Double, lon: Double, language: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _currentWeather.postValue(repo.getWeatherOverNetwork(lat, lon,language).body())
        }
    }
    fun getWeatherDataFromDB () {
        viewModelScope.launch(Dispatchers.IO) {
            _currentWeather.postValue(repo.getWeatherDataFromDB())
        }
    }
}