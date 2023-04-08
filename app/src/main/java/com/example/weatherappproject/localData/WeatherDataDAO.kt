package com.example.weatherappproject.localData

import androidx.room.*
import com.example.weatherappproject.model.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


@Dao
interface WeatherDataDAO {

    @Query("SELECT * FROM WeatherDataTable LIMIT 1")
     fun getWeatherDataFromDB(): Flow<WeatherData?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherData(weatherData: WeatherData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateWeatherData(weatherData: WeatherData)

    @Delete
    suspend fun deleteWeatherData(weatherData: WeatherData?)
}


/*       GlobalScope.launch(Dispatchers.IO){
            val existingWeatherData = getWeatherDataFromDB()

            existingWeatherData?.let {
                GlobalScope.launch(Dispatchers.IO){
                    it.collect(){

                        if (it?.lat!=null && it != null){
                            GlobalScope.launch(Dispatchers.IO){
                                deleteWeatherData(it)
                            }
                        }

                    }
                }
            }
            insertWeatherData(weatherData)
        }*/