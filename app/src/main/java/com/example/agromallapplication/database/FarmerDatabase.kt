package com.example.agromallapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.agromallapplication.models.Farmer


@Database(entities = [Farmer::class],version = 1,exportSchema = false)
@TypeConverters(Converters::class)
abstract class FarmerDatabase:RoomDatabase(){
    abstract fun farmerDao():FarmerDAO
}