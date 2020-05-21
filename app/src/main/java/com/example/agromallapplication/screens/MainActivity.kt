package com.example.agromallapplication.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.agromallapplication.R
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

class MainActivity : AppCompatActivity() {

    private var mPlacesClient: PlacesClient? = null
    private  val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATIONS = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getLocationPermission()
        getCameraPermission()
        Places.initialize(applicationContext, getString(R.string.google_maps_key))
        mPlacesClient = Places.createClient(this)


    }

    private fun getLocationPermission() {

        if (ContextCompat.checkSelfPermission(applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATIONS
            )
        }
    }

    private fun getCameraPermission() {

        if (ContextCompat.checkSelfPermission(applicationContext,
                Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATIONS
            )
        }
    }



}
