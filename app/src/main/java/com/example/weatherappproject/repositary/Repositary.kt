package com.example.weatherappproject.repositary

import com.example.weatherappproject.localData.InterfaceLocalDataSource
import com.example.weatherappproject.localData.LocalDataSource
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData
import com.example.weatherappproject.remoteData.RemoteDataSource
import retrofit2.Response

class Repositary (var remoteSource: RemoteDataSource,private val localDataSource: LocalDataSource): RepositaryInterface{


    companion object{
        @Volatile
        private var INSTANCE: Repositary? = null
        fun getInstance (
            remoteSource: RemoteDataSource,
            localDataSource: LocalDataSource
        ): Repositary{
            return INSTANCE ?: synchronized(this) {
                val temp = Repositary(
                    remoteSource,localDataSource)
                INSTANCE = temp
                temp }
        }
    }

    override suspend fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,
        language: String,
        unit:String

    ): Response<WeatherData> {
        return remoteSource.getWeatherDataOnline(lat,lon,language,unit)
    }

    //Weather
    override suspend fun getWeatherDataFromDB(): WeatherData?{
        return localDataSource.getWeatherDataFromDB()
    }
    override suspend fun insertOrUpdateWeatherData(weatherData: WeatherData) {
        localDataSource.insertOrUpdateWeatherData(weatherData)
    }

    //Favorites
    override fun getAllFavoriteAddresses(): List<FavoriteAddress> {
        return localDataSource.getAllFavoriteAddresses()
    }
    override suspend fun insertFavoriteAddress(address: FavoriteAddress){
        localDataSource.insertFavoriteAddress(address)
    }
    override suspend fun deleteFavoriteAddress(address: FavoriteAddress){
        localDataSource.deleteFavoriteAddress(address)
    }

}