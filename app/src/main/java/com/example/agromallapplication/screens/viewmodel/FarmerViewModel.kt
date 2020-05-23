package com.example.agromallapplication.screens.viewmodel

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.example.agromallapplication.R
import com.example.agromallapplication.models.Farmer
import com.example.agromallapplication.repository.FarmerRepository
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject


class FarmerViewModel @Inject constructor(private val farmerRepository: FarmerRepository): ViewModel(){


    private val viewModelJob = SupervisorJob()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val mAwesomeValidation = AwesomeValidation(ValidationStyle.BASIC)


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
                        .strokeColor(Color.parseColor("#00602e"))
                        .fillColor(Color.WHITE)
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


    fun loginValidation(view:View,email:String,
                        password:String,emailView:
                        View,
                        passwordView: View, emailEditText:View,
                        passwordEditText:View,
                        context:Context): Boolean {
        //VALIDATION
        return if (email.isEmpty() ) {
            displayView(emailView)
            DrawableCompat.setTint(emailEditText.background,ContextCompat.getColor(context, R.color.wrong))
            DrawableCompat.setTint(passwordEditText.background,ContextCompat.getColor(context, R.color.colorWhite))
            hideEdit(passwordView)
            false
        } else if(password.isEmpty()){
            displayView(passwordView)
            DrawableCompat.setTint(passwordEditText.background,ContextCompat.getColor(context, R.color.wrong))
            DrawableCompat.setTint(emailEditText.background,ContextCompat.getColor(context, R.color.colorWhite))
            hideEdit(emailView)
             false
        }else if (!email.contains('@') || !email.contains('.') || email != "test@theagromall.com") {
            hideView(emailView,passwordView)
            displayView(emailView)
            DrawableCompat.setTint(emailEditText.background,ContextCompat.getColor(context, R.color.wrong))

            false
        }else if (password != "password") {
            hideView(emailView,passwordView)
            displayView(passwordView)
            DrawableCompat.setTint(passwordEditText.background,ContextCompat.getColor(context, R.color.wrong))

            false
        }else {
            true
        }

    }

    private fun displayView(view: View){
        view.visibility = View.VISIBLE
    }
    private fun hideEdit(view: View){
        view.visibility = View.GONE
    }

    fun hideView(emailView: View, passwordView: View){
        emailView.visibility = View.GONE
        passwordView.visibility = View.GONE

    }

    fun captureValidation(activity:Activity,name:String,
                          farmerNumber:String,
                          address:String,email:String,
                          farmerPicture:String,farmName:String,
                          farmLocation:String,view: View): Boolean {
        //VALIDATION

       if(name.isEmpty()){
           mAwesomeValidation.addValidation(
               activity,
               R.id.farmerMainName,
               "[a-zA-Z\\s]+",
               R.string.err_empty)
            return false
        }  else if (email.isEmpty()) {
           //mAwesomeValidation.addValidation(name,"name","Error");
           mAwesomeValidation.addValidation(
               activity,
               R.id.farmerEmail,
               "[a-zA-Z\\s]+",
               R.string.err_empty)

           return false
       } else if (address.isEmpty()) {
           //mAwesomeValidation.addValidation(name,"name","Error");
           mAwesomeValidation.addValidation(
               activity,
               R.id.farmerAddress,
               "[a-zA-Z\\s]+",
               R.string.err_empty)

           return false
       }else if (farmerNumber.isEmpty()) {
           //mAwesomeValidation.addValidation(name,"name","Error");
           mAwesomeValidation.addValidation(
               activity,
               R.id.farmerPhoneNumber,
               Patterns.PHONE,
               R.string.err_empty)
           return false
       } else if (farmName.isEmpty()) {
           //mAwesomeValidation.addValidation(name,"name","Error");
           mAwesomeValidation.addValidation(
               activity,
               R.id.farmNameID,
               "[a-zA-Z\\s]+",
               R.string.err_empty)

           return false
       } else if (farmLocation.isEmpty()) {
           //mAwesomeValidation.addValidation(name,"name","Error");
           mAwesomeValidation.addValidation(
               activity,
               R.id.farmLocationID,
               "[a-zA-Z\\s]+",
               R.string.err_empty)

           return false
       }else if (farmerPicture.isEmpty()) {
           Snackbar.make(view, " No picture Uploaded", Snackbar.LENGTH_SHORT).show()
           return false
       } else if (farmerNumber.length < 11 || farmerNumber[0] != '0') {
           //mAwesomeValidation.addValidation(name,"name","Error");
           mAwesomeValidation.addValidation(
               activity,
               R.id.farmerPhoneNumber,
               Patterns.PHONE,
               R.string.err_phone)

            return false
        } else if (!email.contains('@') || !email.contains('.')) {
           mAwesomeValidation.addValidation(
               activity,
               R.id.farmerEmail,
               Patterns.EMAIL_ADDRESS,
               R.string.err_email)
            return false
        }
            else{
            return true
        }

    }



    fun coordinateValidation(view: View,firstCoordinate:String,
                             secondCoordinate:String,thirdCoordinate:String,fourthCoordinate:String):Boolean{
        return if(firstCoordinate.contains("fail") ||
            secondCoordinate.contains("fail") ||
            thirdCoordinate.contains("fail") ||
            fourthCoordinate.contains("fail")){
            Snackbar.make(view, " Coordinate required", Snackbar.LENGTH_SHORT).show()
            false
        }else{
            true
        }

    }



    private fun drawMarker(point :LatLng,mMap:GoogleMap){
        val marker = MarkerOptions()
        marker.position(point)
        mMap.addMarker(marker)
    }


    fun coordinates(farmer:Farmer,mMap:GoogleMap){
        val newLatLon1 = farmer.farmCoordinate!![0].split("/")
        val newLatLon2 = farmer.farmCoordinate!![1].split("/")
        val newLatLon3 = farmer.farmCoordinate!![2].split("/")
        val newLatLon4 = farmer.farmCoordinate!![3].split("/")

        val firstLag = LatLng(newLatLon1[1].toDouble(),newLatLon1[0].toDouble())
        val secondLag = LatLng(newLatLon2[1].toDouble(),newLatLon2[0].toDouble())
        val thirdLag = LatLng(newLatLon3[1].toDouble(),newLatLon3[0].toDouble())
        val fourthLag = LatLng(newLatLon4[1].toDouble(),newLatLon4[0].toDouble())

        drawMarker(firstLag,mMap)
        drawMarker(secondLag,mMap)
        drawMarker(thirdLag,mMap)
        drawMarker(fourthLag,mMap)


    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

    }

    fun time():String {
        val calender = Calendar.getInstance()
        return when (calender.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> {
                "Good Morning"
            }
            in 12..15 -> {
                "Good Afternoon"
            }
            in 16..20 -> {
                "Good Evening"
            }
            else -> {
                "Good Night"

            }
        }
    }



}
