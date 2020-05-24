package com.example.agromallapplication.repository

import androidx.lifecycle.LiveData
import com.example.agromallapplication.datasource.LocalDataSource
import com.example.agromallapplication.models.Farmer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FarmerRepository @Inject constructor(private val dataSource: LocalDataSource){

    fun getAllFarmer(): LiveData<List<Farmer>>{
        return dataSource.getAllFarmers()
    }


    suspend fun addFarmer(item:Farmer){
        dataSource.save(item)
    }


    suspend fun deleteFarmer(id:Long){
        dataSource.delete(id)
    }
}