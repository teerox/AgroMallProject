package com.example.agromallapplication.di

import com.example.agromallapplication.BaseApplication
import com.example.agromallapplication.di.module.DatabaseModule
import com.example.agromallapplication.screens.*
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [DatabaseModule::class])
interface ApplicationComponents {

    fun inject(application: BaseApplication)
    fun inject(application: MainActivity)
    fun inject(fragment: CapturingFragment)
    fun inject(fragment: DashBoardFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: SingleFarmerFragment)
    fun inject(fragment:MapLocationFragment)
}