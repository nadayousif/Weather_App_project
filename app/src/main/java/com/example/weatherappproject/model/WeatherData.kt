package com.example.weatherappproject.model

data class WeatherData(
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Current>,
    val lat: Double,
    val lon: Double,
    val alerts: List<Alert>?,
    val timezone: String?
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
data class Alert(
    val description: String,
    val start: Int,
    val end: Int,
    val event: String,
    val sender_name: String,
    val tags: List<String>
)

data class AlertItem(
    val address: String,
    val longitudeString: String,
    val latitudeString: String,
    val startString: String,
    val endString: String,
    val startDT: Int,
    val endDT: Int,
    val idHashLongFromLonLatStartStringEndStringAlertType: Long,
    val alertType: String,
    val timeAdded: Long
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