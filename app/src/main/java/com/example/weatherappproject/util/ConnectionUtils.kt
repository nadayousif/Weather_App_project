package com.example.weatherappproject.util

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object ConnectionUtils {

    private var connectivityManager: ConnectivityManager? = null

    fun initialize(connectivityManager: ConnectivityManager) {
        this.connectivityManager = connectivityManager
    }

    fun checkConnection(): Boolean {
        val activeNetwork =
            connectivityManager?.getNetworkCapabilities(connectivityManager!!.activeNetwork)
                ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}