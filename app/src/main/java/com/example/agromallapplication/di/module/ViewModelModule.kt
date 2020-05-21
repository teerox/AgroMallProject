package com.example.agromallapplication.di.module

import androidx.lifecycle.ViewModel
import com.example.agromallapplication.di.ViewModelKey
import com.example.agromallapplication.screens.FarmerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FarmerViewModel::class)
    abstract fun bindViewModule(farmerViewModel: FarmerViewModel): ViewModel

}