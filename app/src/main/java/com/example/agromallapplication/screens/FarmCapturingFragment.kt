package com.example.agromallapplication.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.media.audiofx.BassBoost
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.agromallapplication.BuildConfig

import com.example.agromallapplication.R
import com.example.agromallapplication.databinding.FragmentFarmCapturingBinding




import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

/**
 * A simple [Fragment] subclass.
 */
//class FarmCapturingFragment : Fragment(), OnMapReadyCallback{
//
//    lateinit var binding: FragmentFarmCapturingBinding
//    private  val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
//    var mLocationPermissionGranted = false
//    private var mLastKnownLocation: android.location.Location? = null
//    private var mMap: GoogleMap? = null
//    private val mDefaultLocation = LatLng(-33.8523341, 151.2106085)
//    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
//    private lateinit var longitude:String
//    private lateinit var latitude:String
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
////        if (savedInstanceState != null) {
////            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
////            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
////        }
//        binding =
//            DataBindingUtil.inflate(inflater, R.layout.fragment_farm_capturing, container, false)
//
//
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireContext())
//
//        //val args: FarmCapturingFragmentArgs by navArgs()
//       // Log.e("Result", args.farmerInfo.toString())
//        binding.firstCoordinateID.isEnabled = false
//        binding.secondCoordinateID.isEnabled = false
//        binding.thirdCoordinateID.isEnabled = false
//        binding.fourthCoordinateID.isEnabled = false
//
//
//
//
//        binding.firstCoordinateButton.setOnClickListener {
//            getLocationPermission()
//            deviceLocation
//        }
//
//
//
//
//        return binding.root
//    }
//
//
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        if (mMap != null) {
//            outState.putParcelable(KEY_CAMERA_POSITION, mMap!!.cameraPosition)
//            outState.putParcelable(KEY_LOCATION, mLastKnownLocation)
//            super.onSaveInstanceState(outState)
//        }
//    }
//
//    override fun onMapReady(map: GoogleMap) {
//        mMap = map
//        // Prompt the user for permission.
//        getLocationPermission()
//        updateLocationUI()
//        // deviceLocation
//    }
//
//    //DEVICE LOCATION
//    private val deviceLocation: Unit
//        get() {
//            try {
//                Log.e("Longitude",checkPermissions().toString())
//                if (checkPermissions()) {
//                    val locationResult = mFusedLocationProviderClient!!.lastLocation
//                    locationResult.addOnCompleteListener(this.requireActivity()) { task ->
//                        if (task.isSuccessful) { // Set the map's camera position to the current location of the device.
//                            mLastKnownLocation = task.result
//                            if (mLastKnownLocation != null) {
//                                Log.e("Latitude", mLastKnownLocation!!.latitude.toString())
//                                Log.e("Longitude",mLastKnownLocation!!.longitude.toString())
//
//
//                                latitude = mLastKnownLocation!!.latitude.toString()
//                                longitude = mLastKnownLocation!!.longitude.toString()
//                                binding.firstCoordinateID.setText("$longitude/$latitude")
//                                binding.loc.text = "$longitude/$latitude"
//
//                                //SUPPOSED TO ADD TO DB HERE FOR START AND STOP
////                                location_result.text = latitude
////                                updated_on.text = longitude
//
//                            }
//                        } else {
//                            Log.d(TAG, "Current location is null. Using defaults.")
//                            Log.e(TAG, "Exception: %s", task.exception)
//                        }
//                    }
//                }
//            } catch (e: SecurityException) {
//                Log.e("Exception: %s", e.message.toString())
//            }
//        }
//
//    private fun getLocationPermission() {
//
//        if (ContextCompat.checkSelfPermission(requireContext().applicationContext,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//            == PackageManager.PERMISSION_GRANTED) {
//            mLocationPermissionGranted = true
//        } else {
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
//        }
//    }
//    override fun onRequestPermissionsResult(requestCode: Int,
//                                            permissions: Array<String>,
//                                            grantResults: IntArray) {
//        mLocationPermissionGranted = false
//        when (requestCode) {
//            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.isNotEmpty()
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    mLocationPermissionGranted = true
//                }
//            }
//        }
//        updateLocationUI()
//    }
//
//    private fun updateLocationUI() {
//        if (mMap == null) {
//            return
//        }
//        try {
//            if (checkPermissions()) {
//                mMap!!.isMyLocationEnabled = true
//                mMap!!.uiSettings.isMyLocationButtonEnabled = true
//            } else {
//                mMap!!.isMyLocationEnabled = false
//                mMap!!.uiSettings.isMyLocationButtonEnabled = false
//                mLastKnownLocation = null
//                getLocationPermission()
//            }
//        } catch (e: SecurityException) {
//            Log.e("Exception: %s", e.message.toString())
//        }
//    }
//
//    private fun checkPermissions(): Boolean {
//        val permissionState = ActivityCompat.checkSelfPermission(requireContext(),
//            Manifest.permission.ACCESS_COARSE_LOCATION)
//        return permissionState == PackageManager.PERMISSION_GRANTED
//    }
//
//    companion object {
//        //private val TAG = MapLocation::class.java.simpleName
//        private const val DEFAULT_ZOOM = 15
//        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
//        private const val KEY_CAMERA_POSITION = "camera_position"
//        private const val KEY_LOCATION = "location"
//
//    }
//}





