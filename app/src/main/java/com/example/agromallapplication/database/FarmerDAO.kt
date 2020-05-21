package com.example.agromallapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.agromallapplication.models.Farmer

@Dao
interface FarmerDAO {


    @Query("SELECT * FROM farmers ORDER BY uid DESC")
    fun getAll(): LiveData<List<Farmer>>

    @Insert
    fun insertAll(vararg farmer: Farmer)

    @Query("DELETE FROM farmers WHERE uid = :farmerId")
    fun deleteMovieById(farmerId: Long): Int


}