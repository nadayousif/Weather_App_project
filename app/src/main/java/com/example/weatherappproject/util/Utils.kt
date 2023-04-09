package com.example.weatherappproject.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.SyncStateContract
import com.example.weatherappproject.R
import com.example.weatherappproject.ui.alert.AlertFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

object Utils {
    fun getIconUrl(iconCode:String):String{
        return  "https://openweathermap.org/img/wn/" + iconCode + "@4x.png";
    }
    @SuppressLint("SimpleDateFormat")
    fun formatTime(dateObject: Long): String {
        val date = Date(dateObject * 1000L)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)
    }
    fun formatTimeArabic(dateObject: Long): String {
        val date = Date(dateObject * 1000L)
        val sdf = SimpleDateFormat("HH:mm", Locale("ar"))
        return sdf.format(date)
    }
    @SuppressLint("SimpleDateFormat")
    fun formatDate(dt:Long):String{
        val date= Date(dt * 1000L)
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(date)

    }
    @SuppressLint("SimpleDateFormat")
    fun formatDateAlert(dt:Long):String{
        val date= Date(dt)
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(date)

    }
    @SuppressLint("SimpleDateFormat")
    fun formatTimeAlert(dt:Long):String{
        val date = Date(dt)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)

    }

    fun formatDateArabic(dt:Long):String {
        val date= Date(dt * 1000L)
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale("ar"))
        return sdf.format(date)
    }

    fun formatday(dt:Long):String{
        val date= Date(dt * 1000L)
        val sdf = SimpleDateFormat("EEEE")
        return sdf.format(date)

    }
    fun formatdayArabic(dt:Long):String{
        val date= Date(dt * 1000L)
        val sdf = SimpleDateFormat("EEEE", Locale("ar"))
        return sdf.format(date)

    }
    fun englishNumberToArabicNumber(number: String): String {
        val arabicNumber = mutableListOf<String>()
        for (element in number.toString()) {
            when (element) {
                '1' -> arabicNumber.add("١")
                '2' -> arabicNumber.add("٢")
                '3' -> arabicNumber.add("٣")
                '4' -> arabicNumber.add("٤")
                '5' -> arabicNumber.add("٥")
                '6' -> arabicNumber.add("٦")
                '7' -> arabicNumber.add("٧")
                '8' -> arabicNumber.add("٨")
                '9' -> arabicNumber.add("٩")
                '0' ->arabicNumber.add("٠")
                '.'->arabicNumber.add(".")
                '-'->arabicNumber.add("-")
                else -> arabicNumber.add(".")
            }
        }
        return arabicNumber.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", "")
            .replace(" ", "")
    }
    fun getAddressEnglish(context: Context, lat: Double?, lon: Double?):String{
        var address:MutableList<Address>?=null
        val geocoder= Geocoder(context)
        address =geocoder.getFromLocation(lat!!,lon!!,1)
        if (address?.isEmpty()==true)
        {
            return "Unkown location"
        }
        else if (address?.get(0)?.countryName.isNullOrEmpty())
        {
            return "Unkown Country"
        }
        else if (address?.get(0)?.adminArea.isNullOrEmpty())
        {
            return address?.get(0)?.countryName.toString()

        }        else
            return address?.get(0)?.countryName.toString()+" , "+address?.get(0)?.adminArea
    }
    fun getAddressArabic(context: Context, lat:Double, lon:Double):String{
        var address:MutableList<Address>?=null

        val geocoder= Geocoder(context, Locale("ar"))
        address =geocoder.getFromLocation(lat,lon,1)

        if (address?.isEmpty()==true)
        {
            return "Unkown location"
        }
        else if (address?.get(0)?.countryName.isNullOrEmpty())
        {
            return "Unkown Country"
        }
        else if (address?.get(0)?.adminArea.isNullOrEmpty())
        {
            return address?.get(0)?.countryName.toString()

        }
        else
            return address?.get(0)?.countryName.toString()+" , "+address?.get(0)?.adminArea

    }
    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String {
        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(currentTime)
    }
    @SuppressLint("SimpleDateFormat")
    fun getCurrentTime(): Pair<String,Long> {
        val calender= Calendar.getInstance()
        val currentTime = calender.time
        val sdf = SimpleDateFormat("HH:mm")
        val alert=calender.timeInMillis
        return Pair( sdf.format(currentTime),alert)

    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTime2(): Pair<String,Long> {
        val calender= Calendar.getInstance()
        val currentTime = calender.time
        val sdf = SimpleDateFormat("HH:mm")
        val alert=calender.timeInMillis
        return Pair( sdf.format(currentTime),alert)

    }
    @SuppressLint("SimpleDateFormat")
    fun getCurrentDatePlusOne(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(tomorrow)
    }
    @SuppressLint("SimpleDateFormat")
    fun pickedDateFormatDate(dt: Date): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(dt)
    }
    @SuppressLint("SimpleDateFormat")
    fun pickedDateFormatTime(dt: Date): String {
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(dt)
    }

    fun generateRandomNumber():Int{
        return Random.nextInt()
    }

    fun canelAlarm(context: Context, alert:String?, requestCode:Int) {
        var alarmMgr: AlarmManager? = null
        lateinit var alarmIntent: PendingIntent

        alarmMgr = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context.applicationContext, AlertFragment::class.java).putExtra(
            Constants.Alert,alert).let { intent ->
            PendingIntent.getBroadcast(
                context.applicationContext, requestCode, intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
        alarmMgr.cancel(alarmIntent)

    }
    fun isDaily(startTime: Long,endTime:Long):Boolean{
        return endTime-startTime >= 86400000
    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }
    fun setLanguageEnglish(activity: Activity) {
        val languageToLoad = "en"
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        activity.baseContext.getResources().updateConfiguration(
            config,
            activity.baseContext.getResources().getDisplayMetrics()
        )
    }
    fun setLanguageArabic(activity: Activity) {
        val languageToLoad = "ar"
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        activity.baseContext.getResources().updateConfiguration(
            config,
            activity.baseContext.getResources().getDisplayMetrics()
        )

    }
    fun updateResources(context: Context, language: String): Boolean {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.getResources()
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)

        return true
    }







    fun convertToDate(dt: Long,lang :String): String {
        val date = Date(dt * 1000)
        val format = SimpleDateFormat("d MMM, yyyy", Locale(lang))
        return format.format(date)
    }

    fun getseconds(year: Int, month: Int, day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day)
        return calendar.timeInMillis / 1000
    }

    fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }






}