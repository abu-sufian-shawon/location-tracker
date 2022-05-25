package com.sufian.locationtracker

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat

class LocationHelper(context: Context) {
    var LOCATION_REFRESH_TINE = 3000

    var LOCATION_REFRESH_DISTANCE = 0



    fun startListeningUserLocation(context:Context, myListener:MyLocationListener){
        val mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationListener:LocationListener = object : LocationListener{
            override fun onLocationChanged(location: Location) { location } // calling listener to inform that updated location is available

            override fun onProviderEnabled(provider: String) { }
            override fun onProviderDisabled(provider: String) { }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) { }
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