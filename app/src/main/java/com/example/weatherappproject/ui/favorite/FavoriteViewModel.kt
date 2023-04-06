package com.example.weatherappproject.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData
import com.example.weatherappproject.repositary.RepositaryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: RepositaryInterface) : ViewModel() {

    private var _favoriteWeather: MutableLiveData<List<FavoriteAddress>> =
        MutableLiveData<List<FavoriteAddress>>()
    val favoriteWeather: LiveData<List<FavoriteAddress>> = _favoriteWeather
    private var _currentWeather = MutableLiveData<WeatherData>()
    var currentWeather: LiveData<WeatherData> = _currentWeather
    init {
        getAllFavorite()
    }


    fun deleteFavorite(fav: FavoriteAddress) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavoriteAddress(fav)
            getAllFavorite()
        }
    }

    fun getAllFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteWeather.postValue(repository.getAllFavoriteAddresses())

        }
    }

    fun insertFavorite(fav: FavoriteAddress) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavoriteAddress(fav)
            getAllFavorite()
        }

    }

    fun getWeatherFromApi(lat: Double, lon: Double, language: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _currentWeather.postValue(repository.getWeatherOverNetwork(lat, lon,language).body())
        }
    }

}