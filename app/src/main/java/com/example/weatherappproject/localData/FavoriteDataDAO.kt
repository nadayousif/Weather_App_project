package com.example.weatherappproject.localData

import androidx.room.*
import com.example.weatherappproject.model.FavoriteAddress
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteDataDAO {
    @Query("SELECT * FROM FavoriteDataTable")
    fun getAllFavoriteAddresses(): Flow<List<FavoriteAddress>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteAddress(address: FavoriteAddress)

    @Delete
    suspend fun deleteFavoriteAddress(address: FavoriteAddress)

}