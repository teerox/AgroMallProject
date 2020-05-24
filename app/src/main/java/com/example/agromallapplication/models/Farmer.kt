package com.example.agromallapplication.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "farmers")
@Parcelize
data class Farmer(

    @ColumnInfo(name = "farmerName")
    var farmerName:String = "",
    @ColumnInfo(name = "farmerAddress")
    var farmerAddress:String = "",
    @ColumnInfo(name = "farmerEmail")
    var farmerEmail:String = "",
    @ColumnInfo(name = "phoneNumber")
    var phoneNumber:String = "",
    @ColumnInfo(name = "farmerImage")
    var farmerImage:String = "",
    @ColumnInfo(name = "farmName")
    var farmName:String = "",
    @ColumnInfo(name = "farmLocation")
    var farmLocation:String = "",
    @ColumnInfo(name = "farmCoordinate")
    var farmCoordinate:MutableList<String>? = mutableListOf()

): Parcelable
{
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)var uid:Int = 0
}