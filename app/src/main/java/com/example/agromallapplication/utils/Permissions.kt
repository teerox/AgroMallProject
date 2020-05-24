package com.example.agromallapplication.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap


object Permissions{

     const val TAG = "Response"
     const val DEFAULT_ZOOM = 15
     const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
     const val KEY_CAMERA_POSITION = "camera_position"
     const val KEY_LOCATION = "location"
     const val REQUEST_CODE = 1
     const val REQUEST_TAKE_PHOTO = 2
     var mLocationPermissionGranted = false
     var mLastKnownLocation: Location? = null
    private const val PERMISSIONS_REQUEST = 1

    fun getLocationPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST
            )
        }
    }


     fun updateLocationUI(mMap:GoogleMap,activity: Activity) {
        if (mMap == null) {
            return
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                mLastKnownLocation = null
                getLocationPermission(activity)
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message.toString())
        }
    }


     fun getGalleryPermission(activity: Activity):Boolean{
         return if (ContextCompat.checkSelfPermission(activity.applicationContext,
                 Manifest.permission.READ_EXTERNAL_STORAGE)
             == PackageManager.PERMISSION_GRANTED) {
             true
         } else {
             ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                 PERMISSIONS_REQUEST
             )
             false
         }
    }

     fun getCameraPermission(activity: Activity):Boolean {

         return if (ContextCompat.checkSelfPermission(activity.applicationContext,
                 Manifest.permission.CAMERA)
             == PackageManager.PERMISSION_GRANTED) {
             true
         } else {
             ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA),
                 PERMISSIONS_REQUEST
             )
             false
         }
    }


}


