package com.example.agromallapplication.di

import com.example.agromallapplication.BaseApplication
import com.example.agromallapplication.di.module.DatabaseModule
import com.example.agromallapplication.screens.*
import com.example.agromallapplication.screens.capture.CapturingFragment
import com.example.agromallapplication.screens.dashboard.DashBoardFragment
import com.example.agromallapplication.screens.login.LoginActivity
import com.example.agromallapplication.screens.maplocation.MapLocationFragment
import com.example.agromallapplication.screens.singlefarmer.SingleFarmerFragment
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [DatabaseModule::class])
interface ApplicationComponents {

    fun inject(application: BaseApplication)
    fun inject(application: MainActivity)
    fun inject(fragment: CapturingFragment)
    fun inject(fragment: DashBoardFragment)
    fun inject(application: LoginActivity)
    fun inject(fragment: SingleFarmerFragment)
    fun inject(fragment: MapLocationFragment)
}