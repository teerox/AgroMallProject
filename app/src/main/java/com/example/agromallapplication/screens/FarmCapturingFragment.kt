package com.example.agromallapplication.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs

import com.example.agromallapplication.R
import com.example.agromallapplication.databinding.FragmentFarmCapturingBinding

/**
 * A simple [Fragment] subclass.
 */
class FarmCapturingFragment : Fragment() {

    lateinit var binding:FragmentFarmCapturingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_farm_capturing,container,false)

        val args: FarmCapturingFragmentArgs by navArgs()
        Log.e("Result",args.farmerInfo.toString())



        return binding.root
    }

}
