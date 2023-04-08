package com.example.weatherappproject.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappproject.localData.ApiState
import com.example.weatherappproject.localData.DatabaseState
import com.example.weatherappproject.localData.DatabaseWeatherState
import com.example.weatherappproject.model.WeatherData
import com.example.weatherappproject.repositary.RepositaryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel (private val repo: RepositaryInterface) : ViewModel() {

    private var _currentWeather: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    var currentWeather: StateFlow<ApiState> = _currentWeather

    private var _WeatherFromDb: MutableStateFlow<DatabaseWeatherState> =
        MutableStateFlow(DatabaseWeatherState.Loading)
    val WeatherFromDb: StateFlow<DatabaseWeatherState> = _WeatherFromDb

    fun getWeatherFromApi(lat: Double, lon: Double, language: String,unit:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getWeatherOverNetwork(lat, lon,language,unit)
            withContext(Dispatchers.Main) {
                response
                    .catch {
                        _currentWeather.value= ApiState.Failure(it)
                    }
                    .collect {
                        _currentWeather.value = ApiState.Success(it)

                    }
            }
        }
    }


    fun getWeatherDataFromDB () {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getWeatherDataFromDB()
            withContext(Dispatchers.Main){
                result.catch {
                    _WeatherFromDb.value=DatabaseWeatherState.Failure(it) }
                    .collect{
                        _WeatherFromDb.value=DatabaseWeatherState.SuccessHome(it)
                    }
            }
        }
    }
    fun insertWeatherData (weatherData: WeatherData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertOrUpdateWeatherData(weatherData)
            getWeatherDataFromDB ()
        }
    }


}
