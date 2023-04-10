package com.example.weatherappproject.ui.favorite

import org.junit.Assert.*

import org.junit.Test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherappproject.MainDispatcherRule
import com.example.weatherappproject.localData.DatabaseState
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.repositary.FakeRepositary
import com.example.weatherappproject.repositary.RepositaryInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule


@ExperimentalCoroutinesApi
class FavoriteViewModelTest {


    @ExperimentalCoroutinesApi
    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    val address1= FavoriteAddress("Egypt",31.2000917,29.9187383,"1",0.0,"",0,"")

    val address2=FavoriteAddress("Italy",544.2000917,33.9187383,"2",0.0,"",0,"")
    val address3= FavoriteAddress("Germany",31.2000917,19.9187383,"3",0.0,"",0,"")


    val localFavs= listOf(address1,address2)

    lateinit var repo: RepositaryInterface
    lateinit var myViewModel: FavoriteViewModel



    @Before
    fun setUp() {
        repo = FakeRepositary(localFavs.toMutableList())
        myViewModel = FavoriteViewModel(repo)
    }
    @Test
    fun getAllFavorite() =mainDispatcherRule.runBlockingTest {

        myViewModel.getAllFavorite()
        delay(1000)
        var favs=myViewModel.favoriteWeather.first()
        favs=myViewModel.favoriteWeather.first()
        favs=myViewModel.favoriteWeather.first()
        assertThat((favs as DatabaseState.Success).data.size, CoreMatchers.`is`(2))
    }

    @Test
    fun deleteFavorite() {

    }

    @Test
    fun insertFavorite() =mainDispatcherRule.runBlockingTest {
        /*myViewModel.insertFavorite(address3)
        myViewModel.getAllFavorite()
        delay(1000)
        var favs=myViewModel.favoriteWeather.first()
        favs=myViewModel.favoriteWeather.first()
        favs=myViewModel.favoriteWeather.first()

        // Then: data of fake favorite list  increase in size than fake data
        assertThat((favs as DatabaseState.Success).data.size,CoreMatchers.`is`(2))*/

    }
}