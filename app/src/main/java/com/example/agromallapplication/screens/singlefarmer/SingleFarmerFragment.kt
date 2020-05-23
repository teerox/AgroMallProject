package com.example.agromallapplication.screens.singlefarmer

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.agromallapplication.BaseApplication

import com.example.agromallapplication.R
import com.example.agromallapplication.databinding.FragmentSingleFarmerBinding
import com.example.agromallapplication.databinding.SingleFarmBinding
import com.example.agromallapplication.models.Farmer
import com.example.agromallapplication.screens.maplocation.MapLocationFragmentDirections
import com.example.agromallapplication.screens.viewmodel.FarmerViewModel
import com.example.agromallapplication.utils.GetResult
import com.example.agromallapplication.utils.Permissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SingleFarmerFragment : Fragment() , OnMapReadyCallback {

    lateinit var binding: FragmentSingleFarmerBinding
    lateinit var mapView: MapView
    private  var longitude:Double = 0.0
    private var latitude:Double = 0.0
    private lateinit var mMap: GoogleMap
    private var mCameraPosition: CameraPosition? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    var mDefaultLocation = LatLng(-33.8523341, 151.2106085)
    var allCoordinateArray = mutableListOf<String>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var farmerViewModel: FarmerViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().requestWindowFeature(Window.FEATURE_NO_TITLE)
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        (requireActivity().application as BaseApplication).component.inject(this)

        if (savedInstanceState != null) {
            Permissions.mLastKnownLocation = savedInstanceState.getParcelable(Permissions.KEY_LOCATION)
            mCameraPosition = savedInstanceState.getParcelable(Permissions.KEY_CAMERA_POSITION)
        }
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_single_farmer, container, false)
        farmerViewModel = ViewModelProvider(this,viewModelFactory).get(FarmerViewModel::class.java)
        mapView = binding.map

        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        //Data from previous fragement
        val args:SingleFarmerFragmentArgs by navArgs()
        val farmer = args.farmer
        binding.singleFarmer = farmer


        //Setting the Default Location
        val newLatLon1 = farmer.farmCoordinate!![0].split("/")
        mDefaultLocation = LatLng(newLatLon1[1].toDouble(),newLatLon1[0].toDouble())
        allCoordinateArray.addAll(farmer.farmCoordinate!!)


        //draw polygon
        binding.drawPolygon.setOnClickListener {
            farmerViewModel.coordinates(farmer,mMap)
            farmerViewModel.drawPolygon(allCoordinateArray, mMap, binding.root)
            val success = farmerViewModel.drawPolygon(allCoordinateArray, mMap, binding.root)

            if (success) {
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "failed", Toast.LENGTH_LONG).show()
            }
        }

        //Start Coordinate
        farmLocation(newLatLon1[1].toDouble(),newLatLon1[0].toDouble())


        Glide.with(requireContext()).asBitmap().error(R.drawable.human).load(farmer.farmerImage).into(binding.farmerImage)


        return binding.root
    }



    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
    }

    //Farm LOCATION
    private fun farmLocation(startLat:Double,startLon:Double) {

        try {
            if (Permissions.mLocationPermissionGranted) {
                val locationResult = mFusedLocationProviderClient!!.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) { // Set the map's camera position to the current location of the device.
                        Permissions.mLastKnownLocation = task.result
                        if (Permissions.mLastKnownLocation != null) {
                            mMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        startLat,
                                        startLon), Permissions.DEFAULT_ZOOM.toFloat()))

                            mDefaultLocation = LatLng(startLat, startLon)
                            val marker = mMap.addMarker(
                                MarkerOptions()
                                    .position(mDefaultLocation).visible(true))
                            marker.isVisible = true
                            latitude = Permissions.mLastKnownLocation!!.latitude
                            longitude = Permissions.mLastKnownLocation!!.longitude


                            //SUPPOSED TO ADD TO DB HERE FOR START AND STOP



                        }
                    } else {
                        Log.d(Permissions.TAG, "Current location is null. Using defaults.")
                        Log.e(Permissions.TAG, "Exception: %s", task.exception)
                        Toast.makeText(requireContext(),"Check Network Connection", Toast.LENGTH_LONG).show()
                        mMap.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(mDefaultLocation, Permissions.DEFAULT_ZOOM.toFloat()))
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Toast.makeText(requireContext(),"Location request isn't Enabled", Toast.LENGTH_LONG).show()
            Log.e("Exception: %s", e.message.toString())
            val action = MapLocationFragmentDirections.actionMapLocationFragmentToDashBoardFragment2()
            findNavController().navigate(action)
        }
    }



}
