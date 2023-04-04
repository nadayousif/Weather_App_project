package com.example.weatherappproject.util

import androidx.room.TypeConverter
import com.example.weatherappproject.model.Current
import com.example.weatherappproject.model.Daily
import com.example.weatherappproject.model.Hourly
import com.example.weatherappproject.model.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MyConverters {


    @TypeConverter
    fun stringToSomeObjectListWeathar(data: String?): List<Weather>? {
        if (data == null) {
            return null
        }
        val type = object : TypeToken<List<Weather?>?>() {}.type
        return Gson().fromJson<List<Weather>>(data,type )
    }

    @TypeConverter
    fun someObjectListToStringWeather(someObjects: List<Weather?>?): String? {
        if (someObjects == null) {
            return null
        }
        val type= object :
            TypeToken<List<Weather?>?>() {}.type
        return Gson().toJson(someObjects,type)
    }


    @TypeConverter
    fun stringToWeather(value: String?): Weather? {
        return Gson().fromJson(value, Weather::class.java)
    }

    @TypeConverter
    fun weatherToString(value: Weather): String? {
        return Gson().toJson(value)
    }


    @TypeConverter
    fun stringToCurrent(value: String?): Current? {
        return Gson().fromJson(value, Current::class.java)
    }

    @TypeConverter
    fun currentToString(value: Current): String? {
        return Gson().toJson(value)
    }
    @TypeConverter
    fun stringToDaily(value: String?): Daily? {
        return Gson().fromJson(value, Daily::class.java)
    }

    @TypeConverter
    fun DailyToString(value: Daily): String? {
        return Gson().toJson(value)
    }
    @TypeConverter
    fun stringToHourly(value: String?): Hourly? {
        return Gson().fromJson(value, Hourly::class.java)
    }

    @TypeConverter
    fun hourlyToString(value: Hourly): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromCuurentList(list: List<Current?>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type= object :
            TypeToken<List<Current?>?>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toCurrentList(item: String?): List<Current>? {
        if (item == null) {
            return null
        }
        val gson = Gson()
        val type = object :
            TypeToken<List<Current?>?>() {}.type
        return gson.fromJson<List<Current>>(item, type)
    }


    @TypeConverter
    fun fromDailyList(list: List<Daily?>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type= object :
            TypeToken<List<Daily?>?>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toDailyList(item: String?): List<Daily>? {
        if (item == null) {
            return null
        }
        val gson = Gson()
        val type = object :
            TypeToken<List<Daily?>?>() {}.type
        return gson.fromJson<List<Daily>>(item, type)
    }

    @TypeConverter
    fun fromHourlyList(list: List<Hourly?>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type= object :
            TypeToken<List<Hourly?>?>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toHourlyList(item: String?): List<Hourly>? {
        if (item == null) {
            return null
        }
        val gson = Gson()
        val type = object :
            TypeToken<List<Hourly?>?>() {}.type
        return gson.fromJson<List<Hourly>>(item, type)
    }

}