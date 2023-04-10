package com.example.weatherappproject.localData

import com.example.weatherappproject.model.FavoriteAddress

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert

import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavoriteDataDAOTest {





    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    lateinit var db:MyDatabase
    lateinit var dao:FavoriteDataDAO

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MyDatabase::class.java
        ).
        allowMainThreadQueries().build()

        dao = db.getFavoriteAddressDAO()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
   fun  getAllFavoriteAddresses() = runBlockingTest  {
        val address1 = FavoriteAddress("",0.0,0.0,
            "",0.0,"",0,"")

        dao.insertFavoriteAddress(address1)
        val address2 = FavoriteAddress("",0.0,0.0,
            "1",0.0,"",0,"")

        dao.insertFavoriteAddress(address2)


        val address3 = FavoriteAddress("",0.0,0.0,
            "2",0.0,"",0,"")

        dao.insertFavoriteAddress(address3)
        // When
        val results = dao.getAllFavoriteAddresses().first()
        // Then
        MatcherAssert.assertThat(results.size,`is`(3) )


    }


    @Test
    fun insertFavoriteAddress() = runBlockingTest {
        // Given
        val address = FavoriteAddress("",0.0,0.0,
        "",0.0,"",0,"")

        // When
        dao.insertFavoriteAddress(address)

        // Then
        val results = dao.getAllFavoriteAddresses().first()
        MatcherAssert.assertThat(results[0], IsNull.notNullValue())

    }

    @Test
    fun deleteFavoriteAddress() = runBlockingTest  {
        val address1 = FavoriteAddress("",0.0,0.0,
            "",0.0,"",0,"")

        dao.insertFavoriteAddress(address1)
        dao.deleteFavoriteAddress(address1)

        // When
        val results = dao.getAllFavoriteAddresses().first()
        // Then
        MatcherAssert.assertThat(results.size,`is`(0) )

    }
}



