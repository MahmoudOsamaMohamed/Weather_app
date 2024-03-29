package com.plcoding.alarmmanagerguide

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
@Entity(tableName = "alarm_table")
data class AlarmItem(
    @PrimaryKey  val id: Int,
    val day: Long,
    val hour : Int,
    val minute: Int,
    val message: String,
    val city: String
)
