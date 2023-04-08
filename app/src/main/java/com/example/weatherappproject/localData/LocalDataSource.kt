package com.example.weatherappproject.localData

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData
import kotlinx.coroutines.flow.Flow


class LocalDataSource(context: Context) : InterfaceLocalDataSource {
     var weatherDataDAO: WeatherDataDAO
     var favoriteAddressDAO: FavoriteDataDAO

    init {
        val myDatabase = MyDatabase.getInstance(context.applicationContext)
        weatherDataDAO = myDatabase.getWeatherDataDAO()
        favoriteAddressDAO = myDatabase.getFavoriteAddressDAO()

    }
    companion object{
        @Volatile
       var localDataSourceInstance: LocalDataSource? = null
        @Synchronized
        fun getInstance(context: Context): LocalDataSource {
            if(localDataSourceInstance == null){
                localDataSourceInstance = LocalDataSource(context)
            }
            return localDataSourceInstance!!
        }
    }
    //Weather
    override  fun getWeatherDataFromDB(): Flow<WeatherData?>{
        return weatherDataDAO.getWeatherDataFromDB()
    }
    override suspend fun insertOrUpdateWeatherData(weatherData: WeatherData){
        weatherDataDAO.insertOrUpdateWeatherData(weatherData)
    }
    //Favorites
    override fun getAllFavoriteAddresses(): Flow<List<FavoriteAddress>> {
        return favoriteAddressDAO.getAllFavoriteAddresses()
    }
    override suspend fun insertFavoriteAddress(address: FavoriteAddress){
        favoriteAddressDAO.insertFavoriteAddress(address)
    }
    override suspend fun deleteFavoriteAddress(address: FavoriteAddress){
        favoriteAddressDAO.deleteFavoriteAddress(address)
    }

}