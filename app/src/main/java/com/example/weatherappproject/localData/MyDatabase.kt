package com.example.weatherappproject.localData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherappproject.model.Alert
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData
import com.example.weatherappproject.util.MyConverters


@Database(entities = [WeatherData::class , FavoriteAddress::class, Alert::class], exportSchema = false, version = 3)
@TypeConverters(MyConverters::class)
abstract class MyDatabase : RoomDatabase() {

    abstract fun getWeatherDataDAO(): WeatherDataDAO
    abstract fun getFavoriteAddressDAO(): FavoriteDataDAO
    abstract fun getAlertDao(): AlertDataDao


    companion object {
        private var instance: MyDatabase? = null
        @Synchronized
        fun getInstance(context: Context): MyDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java, "WeatherDataDatabase"
                ).fallbackToDestructiveMigration().build()
            }
            return instance!!
        }
    }
}