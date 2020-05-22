package com.example.agromallapplication.screens.maplocation

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.agromallapplication.BaseApplication

import com.example.agromallapplication.R
import com.example.agromallapplication.databinding.FragmentMapLocationBinding
import com.example.agromallapplication.models.Farmer
import com.example.agromallapplication.screens.viewmodel.FarmerViewModel
import com.example.agromallapplication.utils.GetResult
import com.example.agromallapplication.utils.Permissions.DEFAULT_ZOOM
import com.example.agromallapplication.utils.Permissions.KEY_CAMERA_POSITION
import com.example.agromallapplication.utils.Permissions.KEY_LOCATION
import com.example.agromallapplication.utils.Permissions.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
import com.example.agromallapplication.utils.Permissions.TAG
import com.example.agromallapplication.utils.Permissions.getLocationPermission
import com.example.agromallapplication.utils.Permissions.mLastKnownLocation
import com.example.agromallapplication.utils.Permissions.mLocationPermissionGranted
import com.example.agromallapplication.utils.Permissions.updateLocationUI
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class MapLocationFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding:FragmentMapLocationBinding
    lateinit var mapView: MapView
    private  var longitude:Double = 0.0
    private var latitude:Double = 0.0
    private lateinit var mMap: GoogleMap
    private var mCameraPosition: CameraPosition? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    var mDefaultLocation = LatLng(-33.8523341, 151.2106085)
    var coordinateArray = mutableListOf<String>()
    var coordinateArray2 = mutableListOf<String>()
    var coordinateArray3 = mutableListOf<String>()
    var coordinateArray4 = mutableListOf<String>()
    var allCoordinateArray = mutableListOf<String>()



    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var farmerViewModel: FarmerViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity().application as BaseApplication).component.inject(this)

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater,
                R.layout.fragment_map_location,
                container, false)
       // Places.initialize(requireContext().applicationContext, getString(R.string.google_maps_key))

        farmerViewModel = ViewModelProvider(this,viewModelFactory).get(FarmerViewModel::class.java)

        binding.firstCoordinateID.isEnabled = false
        binding.secondCoordinateID.isEnabled = false
        binding.thirdCoordinateID.isEnabled = false
        binding.fourthCoordinateID.isEnabled = false
        mapView = binding.map

        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)


        val args: MapLocationFragmentArgs by navArgs()

        //Details of farmer from the Capture Fragements Screen
        val farmerDetails = args.farmDetails

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        binding.firstCoordinateButton.setOnClickListener {
            deviceLocation(object :GetResult{
                override fun onSuccess(result: String) {
                    Log.e("First", result)
                    binding.firstCoordinateID.setText(getString(R.string.success))
                    coordinateArray.clear()
                    coordinateArray.add(result)

                }

                override fun onFailure(failed: String) {
                    binding.firstCoordinateID.setText(failed)
                }

            })
        }

        binding.secondCoordinateButton.setOnClickListener {
            getLocationPermission(requireActivity())
            if(binding.firstCoordinateID.text.isEmpty()){
                Toast.makeText(requireContext(),"Initial Field Empty",Toast.LENGTH_LONG).show()
               // binding.secondCoordinateID.setText("Failed")

            }else {
                deviceLocation(object : GetResult {
                    override fun onSuccess(result: String) {
                        Log.e("Second", result)
                        binding.secondCoordinateID.setText(R.string.success)
                        coordinateArray2.clear()
                        coordinateArray2.add(result)
                    }

                    override fun onFailure(failed: String) {
                        binding.firstCoordinateID.setText(failed)
                    }

                })
            }
        }

        binding.thirdCoordinateButton.setOnClickListener {
            getLocationPermission(requireActivity())
            if(binding.secondCoordinateID.text.isEmpty()){
                Toast.makeText(requireContext(),"Initial Field Empty",Toast.LENGTH_LONG).show()
            }else {
                deviceLocation(object : GetResult {
                    override fun onSuccess(result: String) {
                        Log.e("Third", result)
                        binding.thirdCoordinateID.setText(R.string.success)
                        coordinateArray3.clear()
                        coordinateArray3.add(result)
                    }

                    override fun onFailure(failed: String) {
                        binding.firstCoordinateID.setText(failed)
                    }

                })
            }
        }

        binding.fourthCoordinateButton.setOnClickListener {
            getLocationPermission(requireActivity())
            if(binding.thirdCoordinateID.text.isEmpty()){
                Toast.makeText(requireContext(),"Initial Field Empty",Toast.LENGTH_LONG).show()
            }else {
                deviceLocation(object : GetResult {
                    override fun onSuccess(result: String) {
                        Log.e("Fourth", result)
                        binding.fourthCoordinateID.setText(R.string.success)
                        coordinateArray4.clear()
                        coordinateArray4.add(result)
                    }

                    override fun onFailure(failed: String) {
                        binding.firstCoordinateID.setText(failed)
                    }

                })
            }
        }


        binding.save.setOnClickListener {
            saveCoordinatesInArray()
            val completeDetails = Farmer(farmerDetails.farmerName,farmerDetails.farmerAddress,
                farmerDetails.farmerEmail,farmerDetails.phoneNumber,farmerDetails.farmerImage,farmerDetails.farmName,
                farmerDetails.farmLocation,allCoordinateArray)
                farmerViewModel.saveFarmerData(completeDetails)
                Log.e("AllArray", completeDetails.toString())
                val action = MapLocationFragmentDirections.actionMapLocationFragmentToDashBoardFragment2()
                findNavController().navigate(action)
                Snackbar.make(binding.root,"Information Saved",Snackbar.LENGTH_LONG).show()

        }


        binding.drawPolygon.setOnClickListener {
           val success = farmerViewModel.drawPolygon(allCoordinateArray,mMap)
            if (success){
                Toast.makeText(requireContext(),"Success",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(requireContext(),"failed",Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.cameraPosition)
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation)
            super.onSaveInstanceState(outState)
        }
    }


    override fun onMapReady(map: GoogleMap) {
        mMap = map

        // Prompt the user for permission.
        getLocationPermission(requireActivity())
        updateLocationUI(mMap,requireActivity())
        // deviceLocation
    }


    //DEVICE LOCATION
    private fun deviceLocation(getResult: GetResult) {

            try {
                if (mLocationPermissionGranted) {
                    val locationResult = mFusedLocationProviderClient!!.lastLocation
                    locationResult.addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) { // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.result
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(mLastKnownLocation!!.latitude,
                                            mLastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))

                               // Log.e("Latitude", mLastKnownLocation!!.latitude.toString())
                               // Log.e("Longitude",mLastKnownLocation!!.longitude.toString())
                                mDefaultLocation = LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude)
                                val marker = mMap.addMarker(MarkerOptions()
                                    .position(mDefaultLocation).visible(true))
                                marker.isVisible = true
                                latitude = mLastKnownLocation!!.latitude
                                longitude = mLastKnownLocation!!.longitude
                                getResult.onSuccess("$longitude/$latitude")

                                //SUPPOSED TO ADD TO DB HERE FOR START AND STOP



                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.")
                            Log.e(TAG, "Exception: %s", task.exception)
                            getResult.onFailure("Not Found")
                            Toast.makeText(requireContext(),"Check Network Connection",Toast.LENGTH_LONG).show()
                            mMap.moveCamera(
                                CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM.toFloat()))
                            mMap.uiSettings.isMyLocationButtonEnabled = false
                        }
                    }
                }
            } catch (e: SecurityException) {
                Toast.makeText(requireContext(),"Location request isn't Enabled",Toast.LENGTH_LONG).show()
                Log.e("Exception: %s", e.message.toString())
                val action = MapLocationFragmentDirections.actionMapLocationFragmentToDashBoardFragment2()
                findNavController().navigate(action)
            }
        }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                }
            }
        }
        updateLocationUI(mMap,requireActivity())
    }



    private fun saveCoordinatesInArray(){
        allCoordinateArray.addAll(coordinateArray)
        allCoordinateArray.addAll(coordinateArray2)
        allCoordinateArray.addAll(coordinateArray3)
        allCoordinateArray.addAll(coordinateArray4)
    }




}
