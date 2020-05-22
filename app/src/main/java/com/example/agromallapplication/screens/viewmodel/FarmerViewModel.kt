package com.example.agromallapplication.screens.viewmodel

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.agromallapplication.models.Farmer
import com.example.agromallapplication.repository.FarmerRepository
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
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


    fun drawPolygon(allCoordinateArray:List<String>, mMap:GoogleMap):Boolean{
        if(allCoordinateArray.size == 4){
            val firstCoordinate = seperateCoordinate(allCoordinateArray[0])
            val secondCoordinate = seperateCoordinate(allCoordinateArray[1])
            val thirdCoordinate = seperateCoordinate(allCoordinateArray[2])
            val fourthCoordinate = seperateCoordinate(allCoordinateArray[3])
            val add =  mMap
                .addPolygon(
                    PolygonOptions()
                        .add(
                            LatLng(firstCoordinate[0].toDouble(),firstCoordinate[1].toDouble()),
                            LatLng(secondCoordinate[0].toDouble(),secondCoordinate[1].toDouble()),
                            LatLng(thirdCoordinate[0].toDouble(),thirdCoordinate[1].toDouble()),
                            LatLng(fourthCoordinate[0].toDouble(),fourthCoordinate[1].toDouble())
                        )
                        .strokeColor(Color.RED)
                        .fillColor(Color.BLUE)
                )
            add.isVisible = true
            return true

        }else{
            return false
        }

    }


    private fun seperateCoordinate(data:String):List<String>{
        return data.split("/")
    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }




}
