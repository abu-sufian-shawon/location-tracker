package com.sufian.locationtracker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import com.sufian.locationtracker.Permission;
import com.sufian.locationtracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!Permission.checkPermission(applicationContext)){
            Permission.requestPermission(this)
        }


        binding.startService.setOnClickListener{
            ContextCompat.startForegroundService(this, Intent(this, LocationServices::class.java))
        }

        binding.stopService.setOnClickListener{
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }
}