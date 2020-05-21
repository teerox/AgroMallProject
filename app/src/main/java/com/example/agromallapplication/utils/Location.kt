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


object Location{
     const val TAG = "Response"
     const val DEFAULT_ZOOM = 15
     const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
     const val KEY_CAMERA_POSITION = "camera_position"
     const val KEY_LOCATION = "location"
     var mLocationPermissionGranted = false
     var mLastKnownLocation: Location? = null

    fun getLocationPermission(context: Context,activity: Activity) {

        if (ContextCompat.checkSelfPermission(context.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

     fun updateLocationUI(mMap:GoogleMap,context: Context,activity: Activity) {
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
                getLocationPermission(context,activity)
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message.toString())
        }
    }

}
interface GetResult{
    fun onSuccess(result: String)
    fun onFailure(failed:String)
}

