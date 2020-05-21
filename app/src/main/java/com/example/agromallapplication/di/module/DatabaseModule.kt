package com.example.agromallapplication.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.agromallapplication.BaseApplication
import com.example.agromallapplication.database.FarmerDAO
import com.example.agromallapplication.database.FarmerDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class DatabaseModule(var application: BaseApplication){

    @Singleton
    @Provides
    fun context(): Context {
        return application
    }

    @Singleton
    @Provides
    internal fun provideRoomDatabase(context: Context): RoomDatabase {
        return Room.databaseBuilder(context, RoomDatabase::class.java, "farmer-db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(database: FarmerDatabase): FarmerDAO {
        return database.farmerDao()
    }

}