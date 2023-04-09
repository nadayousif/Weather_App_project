package com.example.weatherappproject.localData

import androidx.room.*
import com.example.weatherappproject.model.Alert
import kotlinx.coroutines.flow.Flow
@Dao
interface AlertDataDao {
    @Query("SELECT * From Alert")
    fun getAlerts(): Flow<List<Alert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alerts: Alert)

    @Delete
    suspend fun deleteAlert(alerts: Alert):Int
}