package com.example.agromallapplication.screens.viewmodel

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.agromallapplication.models.Farmer
import com.example.agromallapplication.repository.FarmerRepository
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import javax.inject.Inject


class FarmerViewModel @Inject constructor(private val farmerRepository: FarmerRepository): ViewModel(){


    private val viewModelJob = SupervisorJob()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    fun getAllFarmers(): LiveData<List<Farmer>> {
       return farmerRepository.getAllFarmer()
    }


    fun saveFarmerData(item: Farmer) {
        uiScope.launch {
            saveFarmer(item)

        }
    }


    private suspend fun saveFarmer(item:Farmer)= withContext(Dispatchers.IO) {
        farmerRepository.addFarmer(item)

    }


    private suspend fun deleteFarmer(id:Long)= withContext(Dispatchers.IO) {
        farmerRepository.deleteFarmer(id)

    }


    fun drawPolygon(allCoordinateArray:List<String>, mMap:GoogleMap,view: View):Boolean{

        if(allCoordinateArray.size == 4){
            val firstCoordinate = seperateCoordinate(allCoordinateArray[0])
            val secondCoordinate = seperateCoordinate(allCoordinateArray[1])
            val thirdCoordinate = seperateCoordinate(allCoordinateArray[2])
            val fourthCoordinate = seperateCoordinate(allCoordinateArray[3])

            val add =  mMap
                .addPolygon(
                    PolygonOptions()
                        .add(
                            LatLng(firstCoordinate[1].toDouble(),firstCoordinate[0].toDouble()),
                            LatLng(secondCoordinate[1].toDouble(),secondCoordinate[0].toDouble()),
                            LatLng(thirdCoordinate[1].toDouble(),thirdCoordinate[0].toDouble()),
                            LatLng(fourthCoordinate[1].toDouble(),fourthCoordinate[0].toDouble())
                        )
                        .strokeColor(Color.RED)
                        .fillColor(Color.BLUE)
                )
            add.isVisible = true
            return true

        }else{
            Snackbar.make(view,"Kindly generate Coordinates",Snackbar.LENGTH_LONG).show()
            return false
        }

    }


    private fun seperateCoordinate(data:String):List<String>{
        return data.split("/")
    }


    fun loginValidation(view:View,email:String,password:String): Boolean {
        //VALIDATION
        return if (email.isEmpty() || password.isEmpty()) {
            Snackbar.make(view, "Name or password cannot be empty", Snackbar.LENGTH_SHORT).show()
            false
        } else if (!email.contains('@') || !email.contains('.')) {
            Snackbar.make(view, "Invalid Email Address", Snackbar.LENGTH_SHORT).show()
            false
        }else {
            true
        }
    }

    fun captureValidation(view:View,name:String,
                          farmerNumber:String,
                          address:String,email:String,
                          farmerPicture:String,farmName:String,
                          farmLocation:String): Boolean {
        //VALIDATION
       if(name.isEmpty() ||
            farmerNumber.isEmpty() ||
            address.isEmpty() ||
            farmerPicture.isEmpty() ||
            email.isEmpty() ||
            farmName.isEmpty() ||
            farmLocation.isEmpty() ){
            Snackbar.make(view, "Empty Field Detected", Snackbar.LENGTH_SHORT).show()
            return false
        } else if (farmerNumber.length < 11 || farmerNumber[0] != '0') {
           Snackbar.make(view, "Invalid phone, must be 11 digits and start with 0", Snackbar.LENGTH_SHORT).show()
            return false
        } else if (!email.contains('@') || !email.contains('.')) {
           Snackbar.make(view, "Invalid Email Address", Snackbar.LENGTH_SHORT).show()
            return false
        }
            else{
            return true
        }

    }



    fun coordinateValidation(view: View,firstCoordinate:String,
                             secondCoordinate:String,thirdCoordinate:String,fourthCoordinate:String):Boolean{
        return if(firstCoordinate.contains("f") ||
            secondCoordinate.contains("f") ||
            thirdCoordinate.contains("f") ||
            fourthCoordinate.contains("f")){
            Snackbar.make(view, " Coordinate required", Snackbar.LENGTH_SHORT).show()
            false
        }else{
            true
        }

    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }




}
