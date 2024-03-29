package com.mahmoud.weatherapp.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavourateDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favourate: Favourate)
    @Delete
    suspend fun delete(favourate: Favourate)
    @Query("SELECT * FROM favourate")
    fun getAllFavourate(): Flow<List<Favourate>>
}