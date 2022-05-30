package com.sufian.locationtracker

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sufian.locationtracker.databinding.ActivityMapsBinding
import kotlin.math.log

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "MapsActivity"

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var locationServices: LocationServices
    private var bounded: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            bounded = true
            val binder = service as LocationServices.LocationBinder
            locationServices = binder.getService()
            onResume()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bounded = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()

        // Bind Location Service
        Intent(this, LocationServices::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)


        }
    }

    override fun onResume() {
        super.onResume()
        if (bounded) {
            Log.d(TAG, "onStart: bounded = true")
            
            MyLocation.currentOrdinates.observe(
                this
            ) {
                Log.d(TAG, "onResume: Live Data....")
                if (it != null) {
                    Log.d(TAG, "onResume: latitude = ${it.latitude} longitude ${it.longitude}")
                    val location = LatLng(it.latitude, it.longitude)
                    mMap.addMarker(
                        MarkerOptions().position(location)
                            .title("Marker ar ${it.latitude}, ${it.longitude}")
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                } else {
                    Log.d(TAG, "onResume: Coordinates is null")
                }
            }
        } else {
            Log.d(TAG, "onStart: bounded = false")
        }

    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        bounded = false
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}

object MyLocation{
    var currentOrdinates: MutableLiveData<CoOrdinates> = MutableLiveData()

}