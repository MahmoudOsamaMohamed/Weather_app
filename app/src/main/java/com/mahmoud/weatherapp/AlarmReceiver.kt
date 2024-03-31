package com.mahmoud.weatherapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat


class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return

        val title = intent?.getStringExtra("EXTRA_CITY") ?: return
        val isService = intent?.getBooleanExtra("EXTRA_IS_SERVICE", false) ?: return
        Log.d("lllllllllllllllll", "onReceive: $title")

       // showNotification(context!!.applicationContext, title, message)
        if(isService){
        val serviceIntent = Intent(context, MusicService::class.java)
        serviceIntent.putExtra("EXTRA_MESSAGE", message)
        serviceIntent.putExtra("EXTRA_CITY", title)
        context?.startService(serviceIntent)
    }
    else{
        showNotification(context!!, title, message)
    }}

    fun showNotification(context: Context, title: String, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "default_channel_id"
        val channelName = "Default Channel"
        val importance = NotificationManager.IMPORTANCE_HIGH

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
             .setSmallIcon(R.drawable.add_allarm) // Set your notification icon here
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(1, notificationBuilder.build())
    }
}