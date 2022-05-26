package com.sufian.locationtracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.util.*

class LocationServices : Service() {

    private val NOTIFICATION_CHANNEL_ID = "location_tracker_id"
    var isServiceStarted = false
    var mLocation:Location? = null

    override fun onCreate() {
        super.onCreate()

        isServiceStarted = true
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            this,
            NOTIFICATION_CHANNEL_ID
        ).setOngoing(false).setSmallIcon(R.drawable.ic_launcher_background)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationChannel.description = NOTIFICATION_CHANNEL_ID
            notificationChannel.setSound(null, null)
            notificationManager.createNotificationChannel(notificationChannel)
            startForeground(1, builder.build())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val timer = Timer()
        LocationHelper().startListeningUserLocation(
            this,
            object : MyLocationListener{
                override fun onLocationChanged(location: Location?) {
                    mLocation = location
                    mLocation?.let{

                    }
                }
            }
        )
        return START_STICKY
    }


    override fun onBind(intent: Intent?): IBinder? {

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}