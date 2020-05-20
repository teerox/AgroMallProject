package com.example.agromallapplication.models

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Farmer(var farmerName:String = "",
                  var farmerAddress:String = "",
                  var farmerEmail:String = "",
                  var phoneNumber:String = "",
                  var farmerImage:String = "",
                  var farmName:String = "",
                  var farmLocation:String = "",
                  var farmCoordinate:ArrayList<String>? = arrayListOf
("123","hhhh")): Parcelable
{
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)var uid:Int? = 0
}