package com.example.agromallapplication.datasource

import androidx.lifecycle.LiveData
import com.example.agromallapplication.database.FarmerDAO
import com.example.agromallapplication.models.Farmer
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val farmerDAO: FarmerDAO){


    fun getAllFarmers(): LiveData<List<Farmer>>{
        return farmerDAO.getAll()
    }



    suspend fun save(item:Farmer){
        farmerDAO.insertAll(item)
    }



    suspend fun delete(id:Long){
        farmerDAO.deleteFarmById(id)
    }
}