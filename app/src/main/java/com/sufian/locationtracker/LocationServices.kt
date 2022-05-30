package com.sufian.locationtracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LocationServices : Service() {
    private val TAG = "Coordinates"

    private val NOTIFICATION_CHANNEL_ID = "location_tracker_id"

    private val binder = LocationBinder()





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
                    Log.d(TAG, "onLocationChanged: is called...")
                    mLocation = location
                    mLocation?.let{
                        Log.d(TAG, "onLocationChanged: latitude = ${it.latitude} longitude ${it.longitude}")
                        val coOrdinates = CoOrdinates(longitude = it.longitude, latitude = it.latitude)
                        MyLocation.currentOrdinates.postValue(coOrdinates)




                        /*AppExecutors.instance?.networkIO()?.execute{
                            val apiClient = ApiClient.getInstance(this@LocationServices)
                                .create(ApiClient::class.java)
                            val response = apiClient.updateLocation()
                            response.enqueue(object: Callback<LocationResponse>{
                                override fun onResponse(
                                    call: Call<LocationResponse>,
                                    response: Response<LocationResponse>
                                ) {
                                    Log.d(TAG, "onResponse: latitude = ${it.latitude} longitude = ${it.longitude}")
                                    Log.d(TAG, "run: Running = Location update successful")
                                }

                                override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                                    Log.d(TAG, "run: Running = Location update failed")
                                }

                            })
                        }*/


                    }
                }
            }
        )
        return START_STICKY
    }


    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        isServiceStarted = false
        super.onDestroy()
    }

    companion object{
        var isServiceStarted = false
        var mLocation:Location? = null
    }

    inner class LocationBinder : Binder(){
        fun getService():LocationServices = this@LocationServices
    }
}

class BootDeviceReceivers : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            ContextCompat.startForegroundService(it, Intent(it, LocationServices::class.java))
        }
    }

}