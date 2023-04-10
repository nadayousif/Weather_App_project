package com.example.weatherappproject.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weatherappproject.util.MyConverters
import java.io.Serializable

@Entity(tableName = "WeatherDataTable", primaryKeys = ["i"])
data class WeatherData(
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Current>,
    var lat: Double=1.0,
    var lon: Double=1.0,
    val i: Int=1,
   // val alerts: List<Alert>?,
    val timezone: String?
)

@Entity(tableName = "FavoriteDataTable", primaryKeys = ["latlngString"])
data class FavoriteAddress(
    var address: String,
    @NonNull
    var latitude: Double,
    @NonNull
    var longitude: Double,
    var latlngString: String,
    var currentTemp: Double,
    var currentDescription: String,
    val lastCheckedTime: Long,
    val icon: String
)
data class Current(
    val clouds: Int,
    val humidity: Int,
    val pressure: Int,
    val dt: Int,
    val temp: Double,
    val wind_speed: Double,
    val weather: List<Weather>

)

@Entity(tableName = "Alert")
data class Alert(
    var startDay: Long,
    var endDay: Long,
    var lat: Double,
    var lon: Double,
    @PrimaryKey
    var AlertCityName :String
) : Serializable


data class AlertSettings (
    var lat:Double=36.4761,
    var lon:Double=-119.4432,
    var isALarm:Boolean=true,
    var isNotification:Boolean=false
)


data class Daily(
    val dt: Int,
    val temp: Temp,
    val weather: List<Weather>

)

data class Temp(
    val max: Double,
    val min: Double

)

data class Weather(
    val description: String,
    val icon: String,
    val id: Int

)
data class Hourly(
    val dt: Int,
    val weather: List<Weather>,
    val temp: Double

)