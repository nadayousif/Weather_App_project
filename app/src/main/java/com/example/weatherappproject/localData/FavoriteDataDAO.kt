package com.example.weatherappproject.localData

import androidx.room.*
import com.example.weatherappproject.model.FavoriteAddress


@Dao
interface FavoriteDataDAO {
    @Query("SELECT * FROM FavoriteDataTable")
    fun getAllFavoriteAddresses(): List<FavoriteAddress>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteAddress(address: FavoriteAddress)

    @Delete
    suspend fun deleteFavoriteAddress(address: FavoriteAddress)

}