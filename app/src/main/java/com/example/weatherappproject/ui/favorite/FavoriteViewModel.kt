package com.example.weatherappproject.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappproject.localData.DatabaseState
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData
import com.example.weatherappproject.repositary.RepositaryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(private val repository: RepositaryInterface) : ViewModel() {

    private var _favoriteWeather: MutableStateFlow<DatabaseState> =
        MutableStateFlow(DatabaseState.Loading)
    val favoriteWeather: StateFlow<DatabaseState> = _favoriteWeather
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
            val result = repository.getAllFavoriteAddresses()
            withContext(Dispatchers.Main){
                result.catch {
                    _favoriteWeather.value=DatabaseState.Failure(it) }
                    .collect{
                        _favoriteWeather.value=DatabaseState.Success(it)
                    }
            }
        }
    }

    fun insertFavorite(fav: FavoriteAddress) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavoriteAddress(fav)
            getAllFavorite()
        }

    }

    fun getWeatherFromApi(lat: Double, lon: Double, language: String,unit: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _currentWeather.postValue(repository.getWeatherOverNetwork(lat, lon,language,unit).body())
        }
    }

}