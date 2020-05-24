package com.example.agromallapplication.screens.maplocation

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import kotlinx.android.synthetic.main.fragment_map_location.*
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

        farmerViewModel.dialogue(requireContext(),"Move to four different Edges of the farm before selecting a coordinate ")

        binding.firstCoordinateID.isEnabled = false
        binding.secondCoordinateID.isEnabled = false
        binding.thirdCoordinateID.isEnabled = false
        binding.fourthCoordinateID.isEnabled = false
        binding.second.visibility = View.GONE
        binding.third.visibility = View.GONE
        binding.fourth.visibility = View.GONE
        binding.mainLayout.visibility = View.GONE


        mapView = binding.map

        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)


        val args: MapLocationFragmentArgs by navArgs()

        //Details of farmer from the Capture Fragements Screen
        val farmerDetails = args.farmDetails

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        //first Coordinate Button
        binding.firstCoordinateButton.setOnClickListener {
            getLocationPermission(requireActivity())
            deviceLocation(object :GetResult{
                override fun onSuccess(result: String) {
                    Log.e("First", result)
                    binding.firstCoordinateID.setText("Successfull")
                    coordinateArray.clear()
                    coordinateArray.add(result)
                    showFieldView(binding.second)
                }

                override fun onFailure(failed: String) {
                    binding.firstCoordinateID.setText(failed)
                    Snackbar.make(binding.root, "Change Device Location",Snackbar.LENGTH_LONG).show()
                }
            })

        }

        //second Coordinate Button
        binding.secondCoordinateButton.setOnClickListener {
            getLocationPermission(requireActivity())
                deviceLocation(object : GetResult {
                    override fun onSuccess(result: String) {
                        Log.e("Second", result)
                        binding.secondCoordinateID.setText("Successfull")
                        coordinateArray2.clear()
                        coordinateArray2.add(result)
                        showFieldView(binding.third)
                    }

                    override fun onFailure(failed: String) {
                        binding.firstCoordinateID.setText(failed)
                        Snackbar.make(binding.root, "Change Device Location",Snackbar.LENGTH_LONG).show()
                    }
                })

        }


        //third Coordinate Button
        binding.thirdCoordinateButton.setOnClickListener {
            getLocationPermission(requireActivity())
                deviceLocation(object : GetResult {
                    override fun onSuccess(result: String) {
                        Log.e("Third", result)
                        binding.thirdCoordinateID.setText("Successfull")
                        coordinateArray3.clear()
                        coordinateArray3.add(result)
                        showFieldView(binding.fourth)
                    }

                    override fun onFailure(failed: String) {
                        binding.firstCoordinateID.setText(failed)
                        Snackbar.make(binding.root, "Change Device Location",Snackbar.LENGTH_LONG).show()
                    }

                })

        }


        //fourth Coordinate Button
        binding.fourthCoordinateButton.setOnClickListener {
            getLocationPermission(requireActivity())
                deviceLocation(object : GetResult {
                    override fun onSuccess(result: String) {
                        Log.e("Fourth", result)
                        binding.fourthCoordinateID.setText("Successfull")
                        coordinateArray4.clear()
                        coordinateArray4.add(result)
                        showFieldView(binding.mainLayout)
                        drawPolygonOnMap()
                        farmerViewModel.dialogue(requireContext(),"Polygon Drawn, Zoom in to View")
                    }
                    override fun onFailure(failed: String) {
                        binding.firstCoordinateID.setText(failed)
                        Snackbar.make(binding.root, "Change Device Location",Snackbar.LENGTH_LONG).show()
                    }

                })

        }


        //Save Button
        binding.save.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
            dialogBuilder.setMessage("Do you want to Save ")
            dialogBuilder.setPositiveButton("Yes"){ _, _ ->
                saveCoordinatesInArray()
                Log.e("Cordinate",allCoordinateArray.toString())
                if(allCoordinateArray.size == 4) {
                    val completeDetails = Farmer(
                        farmerDetails.farmerName,
                        farmerDetails.farmerAddress,
                        farmerDetails.farmerEmail,
                        farmerDetails.phoneNumber,
                        farmerDetails.farmerImage,
                        farmerDetails.farmName,
                        farmerDetails.farmLocation,
                        allCoordinateArray
                    )
                    farmerViewModel.saveFarmerData(completeDetails)
                    val action =
                        MapLocationFragmentDirections.actionMapLocationFragmentToDashBoardFragment2()
                    findNavController().navigate(action)
                    Snackbar.make(binding.root, "Information Saved", Snackbar.LENGTH_LONG).show()
                }else {
                    Snackbar.make(binding.root, "Error saving information Please try Again", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
            dialogBuilder.setNegativeButton("No"){
                    _,_ ->
                Snackbar.make(binding.root, "Not Saved", Snackbar.LENGTH_LONG)
                    .show()
            }

            //show the dialogue
            val alert = dialogBuilder.create()
            alert.setCanceledOnTouchOutside(false)
            alert.show()



        }


//        //Draw Polygon on Map
//        binding.drawPolygon.setOnClickListener {
//            saveCoordinatesInArray()
//            val validate = farmerViewModel.coordinateValidation(binding.root,
//                firstCoordinateID.text.toString(),
//                secondCoordinateID.text.toString(),
//                thirdCoordinateID.text.toString(),
//                firstCoordinateID.text.toString())
//            if(validate) {
//                farmerViewModel.drawPolygon(allCoordinateArray, mMap, binding.root)
//                val success = farmerViewModel.drawPolygon(allCoordinateArray, mMap, binding.root)
//                if (success) {
//                    Snackbar.make(binding.root, "Polygon Drawn", Snackbar.LENGTH_LONG).show()
//                }
//            }
//        }


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
        allCoordinateArray.clear()
        allCoordinateArray.addAll(coordinateArray)
        allCoordinateArray.addAll(coordinateArray2)
        allCoordinateArray.addAll(coordinateArray3)
        allCoordinateArray.addAll(coordinateArray4)
    }



    fun showFieldView(view: View){
        view.visibility = View.VISIBLE
    }


    fun drawPolygonOnMap(){
        saveCoordinatesInArray()
        val validate = farmerViewModel.coordinateValidation(binding.root,
            firstCoordinateID.text.toString(),
            secondCoordinateID.text.toString(),
            thirdCoordinateID.text.toString(),
            firstCoordinateID.text.toString())
        if(validate) {
            farmerViewModel.drawPolygon(allCoordinateArray, mMap, binding.root)
//            val success = farmerViewModel.drawPolygon(allCoordinateArray, mMap, binding.root)
//            if (success) {
//                Snackbar.make(binding.root, "Polygon Drawn", Snackbar.LENGTH_LONG).show()
//            }
        }
    }


}
