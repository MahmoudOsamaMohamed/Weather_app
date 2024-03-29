package com.mahmoud.weatherapp.alaram

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.plcoding.alarmmanagerguide.AlarmItem
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insert(item: AlarmItem)
   @Query("SELECT * FROM alarm_table")
   fun getAllItems(): Flow<List<AlarmItem>>
   @Query("DELETE FROM alarm_table where id = :id")
   suspend fun delete(id: Int)
}