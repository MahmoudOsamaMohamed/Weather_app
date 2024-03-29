package com.mahmoud.weatherapp
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mahmoud.weatherapp.MainActivity
import com.mahmoud.weatherapp.R

class MusicService : Service() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "MusicServiceChannel"
        const val NOTIFICATION_ID = 1
    }

    private var isPlaying = false
    private var title: String = "Music Service"
    private var message: String = "Playing music..."
    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        title = intent?.getStringExtra("EXTRA_TITLE") ?: "Music Service"
        message = intent?.getStringExtra("EXTRA_MESSAGE") ?: "Playing music..."
        if (intent?.action == "ACTION_STOP") {
            stopSelf()
            return START_NOT_STICKY
        }
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.rain_sound) // Load music file from raw resources
            mediaPlayer?.isLooping = true // Set looping
        }

        // Start or pause playback based on current state
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        } else {
            mediaPlayer?.start()
        }

        // Start playing music (implement your own logic here)
        // For demonstration purposes, just toggling the playback state
        isPlaying = !isPlaying

        if (isPlaying) {
            // Start foreground service
            startForeground(NOTIFICATION_ID, createNotification())
        } else {
            stopForeground(true)
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onDestroy() {
        super.onDestroy()
        // Release media player resources
        mediaPlayer?.release()
    }
    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val stopIntent = Intent(this, MusicService::class.java).apply {
            action = "ACTION_STOP"
        }
        val stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.add_allarm)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.delete_ic, "Stop", stopPendingIntent)
            .build()

        return notification
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Music Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
