package com.sufian.locationtracker

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat

class LocationHelper {
    private val TAG = "Coordinates"
    var LOCATION_REFRESH_TINE = 1000

    var LOCATION_REFRESH_DISTANCE = 0



    fun startListeningUserLocation(context:Context, myListener:MyLocationListener){
        Log.d(TAG, "startListeningUserLocation: is called...")

        val mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationListener:LocationListener = object : LocationListener{
            override fun onLocationChanged(location: Location) {
                location
                Log.d(TAG, "onLocationChanged: (${location.longitude}, ${location.latitude})")
            } // calling listener to inform that updated location is available

            override fun onProviderEnabled(provider: String) {
                Log.d(TAG, "onProviderEnabled: provider enabled...")
            }
            override fun onProviderDisabled(provider: String) {
                Log.d(TAG, "onProviderDisabled: provider disabled...")
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                Log.d(TAG, "onStatusChanged: status changed..." )
            }
        }

        if (Permission.checkPermission(context)) {
            mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                LOCATION_REFRESH_TINE.toLong(),
                LOCATION_REFRESH_DISTANCE.toFloat(),
                locationListener
            )
        }

    }
}

interface MyLocationListener{
    fun onLocationChanged(location:Location?)
}