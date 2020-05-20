package com.example.agromallapplication.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController

import com.example.agromallapplication.R
import com.example.agromallapplication.databinding.FragmentDashBoardBinding

/**
 * A simple [Fragment] subclass.
 */
class DashBoardFragment : Fragment() {

    lateinit var binding:FragmentDashBoardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_dash_board,container,false)

        binding.addNew.setOnClickListener {
            val action = DashBoardFragmentDirections.actionDashBoardFragment2ToCapturingFragment()
            findNavController().navigate(action)
        }
        return binding.root
    }

}
