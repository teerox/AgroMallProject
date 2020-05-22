package com.example.agromallapplication.screens.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.agromallapplication.BaseApplication

import com.example.agromallapplication.R
import com.example.agromallapplication.databinding.FragmentDashBoardBinding
import com.example.agromallapplication.screens.viewmodel.FarmerViewModel
import com.example.agromallapplication.utils.Permissions.getLocationPermission
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class DashBoardFragment : Fragment() {

    lateinit var binding:FragmentDashBoardBinding



    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var farmerViewModel: FarmerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity().application as BaseApplication).component.inject(this)
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_dash_board,container,false)
        getLocationPermission(requireActivity())

        farmerViewModel = ViewModelProvider(this,viewModelFactory).get(FarmerViewModel::class.java)

        farmerViewModel.getAllFarmers().observeForever {
            Log.e("All farmers",it.toString())
        }


        binding.addNew.setOnClickListener {
            val action =
                DashBoardFragmentDirections.actionDashBoardFragment2ToCapturingFragment()
            findNavController().navigate(action)
        }
        return binding.root
    }

}
