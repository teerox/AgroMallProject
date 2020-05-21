package com.example.agromallapplication

import android.app.Application
import com.example.agromallapplication.di.ApplicationComponents
import com.example.agromallapplication.di.DaggerApplicationComponents


class BaseApplication : Application() {

    lateinit var component: ApplicationComponents

    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponents.create()

    }

}