package com.example.weatherappproject.remoteData

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    @Synchronized
    fun getRetrofitInstance(baseURLString : String) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURLString)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}