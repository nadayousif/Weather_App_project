package com.example.weatherappproject.repositary

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import com.example.weatherappproject.MainDispatcherRule
import com.example.weatherappproject.database.FakeLocalDataSource
import com.example.weatherappproject.model.Current
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData
import com.example.weatherappproject.remoteDataSource.FakeRemoteDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.core.Is
import org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Before
import org.junit.Rule



@RunWith(JUnit4::class)
class RepositaryTest  {
    @get:Rule
    val mainDispatcherRule= MainDispatcherRule()
    val address1 = WeatherData(
        current = Current(
            clouds = 7714,
            humidity = 3054,
            pressure = 8600,
            dt = 3906,
            temp = 40.41,
            wind_speed = 42.43,
            weather = listOf()
        ), daily = listOf(), hourly = listOf(), lat = 44.45, lon = 46.47, i = 2292, timezone = null
    )

    val address2 = WeatherData(
        current = Current(
            clouds = 7169,
            humidity = 4607,
            pressure = 2204,
            dt = 7259,
            temp = 32.33,
            wind_speed = 34.35,
            weather = listOf()
        ), daily = listOf(), hourly = listOf(), lat = 36.37, lon = 38.39, i = 8015, timezone = null
    )

    val address3 = WeatherData(
        current = Current(
            clouds = 3129,
            humidity = 4775,
            pressure = 7210,
            dt = 8553,
            temp = 24.25,
            wind_speed = 26.27,
            weather = listOf()
        ), daily = listOf(), hourly = listOf(), lat = 128.129, lon = 130.131, i = 4594, timezone = null
    )

    var favoritesList: MutableList<FavoriteAddress> = mutableListOf<FavoriteAddress>(

        FavoriteAddress("Egypt",31.2000917,29.9187383,"1",0.0,"",0,""),

        FavoriteAddress("Italy",544.2000917,33.9187383,"2",0.0,"",0,""),
        FavoriteAddress("Germany",31.2000917,19.9187383,"3",0.0,"",0,""),
        FavoriteAddress("America",131.2000917,229.9187383,"3",0.0,"",0,""),

    )

    val localFavs= listOf(address1,address2)

    lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    lateinit var fakeLocalDataSource: FakeLocalDataSource
    lateinit var sharedPreferences: SharedPreferences
    val testContext: Application = ApplicationProvider.getApplicationContext()

    lateinit var repo:Repositary

    @Before
    fun setUp(){
        fakeLocalDataSource= FakeLocalDataSource(localFavs.toMutableList())
        fakeRemoteDataSource=FakeRemoteDataSource(address3)
        sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(testContext)


        repo= Repositary(
            fakeRemoteDataSource,
            fakeLocalDataSource,
            sharedPreferences
            )

    }


    @Test
    fun getWeatherOverNetwork()  =mainDispatcherRule.runBlockingTest {
        val results = repo.getWeatherOverNetwork(
            128.129, 130.131,"en","unit"
        ).first()
        results.body().apply {
            this?.lat= "128.129".toDouble()
            this?.lon="130.131".toDouble()
        }

        // Then: response is a same of fake WeatherResponse
        assertThat(results.body(), CoreMatchers.`is`(address3))
    }
    @Test
    fun getWeatherDataFromDB() {

    }



    @Test
    fun insertOrUpdateWeatherData() {
    }

    @Test
    fun getAllFavoriteAddresses() = runBlocking {


    }

    @Test
    fun insertFavoriteAddress() {
    }

    @Test
    fun deleteFavoriteAddress() {
    }


}