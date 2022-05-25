package com.sufian.locationtracker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import com.sufian.locationtracker.Permission;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!Permission.checkPermission(applicationContext)){
            Permission.requestPermission(this)
        }else {
            ContextCompat.startForegroundService(this, Intent(this, LocationServices::class.java)
            )
        }
    }
}