package com.plcoding.alarmmanagerguide

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.mahmoud.weatherapp.AlarmReceiver
import java.time.ZoneId
import java.util.Calendar

class AndroidAlarmScheduler(
    private val context: Context
): AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun schedule(item: AlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", item.message)
            putExtra("EXTRA_CITY", item.city)
            putExtra("EXTRA_IS_SERVICE", item.isService)
        }
        Log.d("lllo",Thread.currentThread().name)
        Log.d("lllo",item.day.toString()+" "+item.hour+" "+item.minute)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calculateTimeDifference(item.day.toInt(), item.hour, item.minute),
            PendingIntent.getBroadcast(context,
                5,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        )
    }

    override fun cancel(item: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.id,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
    fun calculateTimeDifference(daysLeft: Int, hour: Int, minute: Int): Long {
        // Get current calendar instance
        val currentCalendar = Calendar.getInstance()

        // Add daysLeft to current date
        currentCalendar.add(Calendar.DAY_OF_YEAR, daysLeft)

        // Set hour and minute
        currentCalendar.set(Calendar.HOUR_OF_DAY, hour)
        currentCalendar.set(Calendar.MINUTE, minute)
        currentCalendar.set(Calendar.SECOND, 0)
        currentCalendar.set(Calendar.MILLISECOND, 0)

        // Get time in milliseconds for future date
        val futureTimeInMillis = currentCalendar.timeInMillis

        // Get current time in milliseconds
        val currentTimeInMillis = System.currentTimeMillis()

        // Calculate the difference
        val timeDifference = futureTimeInMillis - currentTimeInMillis
Log.d("llllllllllllllllllll","diffrence $timeDifference")
        return timeDifference
    }
}