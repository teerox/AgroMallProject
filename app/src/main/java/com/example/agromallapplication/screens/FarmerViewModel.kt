package com.example.agromallapplication.screens

import android.graphics.Color
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions

class FarmerViewModel(): ViewModel(){



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
                            LatLng(firstCoordinate[0].toDouble(),
                                firstCoordinate[1].toDouble()),
                            LatLng(secondCoordinate[0].toDouble(), secondCoordinate[1].toDouble()),
                            LatLng(thirdCoordinate[0].toDouble(),thirdCoordinate[1].toDouble()),
                            LatLng(fourthCoordinate[0].toDouble(), fourthCoordinate[1].toDouble())
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

        val newArray = data.split("/")
        return newArray
    }
    
}
