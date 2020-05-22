package com.example.agromallapplication.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.agromallapplication.di.ViewModelKey
import com.example.agromallapplication.screens.viewmodel.FarmerViewModel
import com.example.agromallapplication.screens.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FarmerViewModel::class)
    abstract fun bindViewModule(farmerViewModel: FarmerViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}