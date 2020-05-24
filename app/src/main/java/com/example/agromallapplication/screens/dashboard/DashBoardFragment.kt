package com.example.agromallapplication.screens.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.agromallapplication.BaseApplication

import com.example.agromallapplication.R
import com.example.agromallapplication.databinding.FragmentDashBoardBinding
import com.example.agromallapplication.models.Farmer
import com.example.agromallapplication.screens.MainActivity
import com.example.agromallapplication.screens.login.LoginActivity
import com.example.agromallapplication.screens.viewmodel.FarmerViewModel
import com.example.agromallapplication.utils.Permissions.getLocationPermission
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class DashBoardFragment : Fragment() {

    lateinit var binding:FragmentDashBoardBinding
    private var farmersArray = ArrayList<Farmer>()
    lateinit var adapter: FarmerAdapter
    lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var farmerViewModel: FarmerViewModel


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        (requireActivity().application as BaseApplication).component.inject(this)
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_dash_board,container,false)
        getLocationPermission(requireActivity())

        farmerViewModel = ViewModelProvider(this,viewModelFactory).get(FarmerViewModel::class.java)

        val greeting = farmerViewModel.time()
        binding.time.text = "Hi, $greeting"
        recyclerView = binding.recycler



        farmerViewModel.getAllFarmers().observeForever {
           // Log.e("All farmers",it.toString())
            binding.numberOfFarmers.text = it.size.toString()

            if(it.isEmpty()){
                binding.nofarmer.visibility = View.VISIBLE
            }else{
                binding.nofarmer.visibility = View.GONE
                //Log.e("Empty",it.size.toString())
            }
            farmersArray.clear()
            farmersArray.addAll(it)
            adapter.notifyDataSetChanged()

        }


        adapter = FarmerAdapter(farmersArray){
            val action = DashBoardFragmentDirections.actionDashBoardFragment2ToSingleFarmerFragment(it)
            findNavController().navigate(action)

        }


        recyclerView.adapter = adapter


        binding.addNew.setOnClickListener {
            val action =
                DashBoardFragmentDirections.actionDashBoardFragment2ToCapturingFragment()
            findNavController().navigate(action)
        }


        binding.logout.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
            dialogBuilder.setMessage("Do you want to logout")
            dialogBuilder.setPositiveButton("Yes"){ _, _ ->
              //move to login page
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

                val sharedPreferences = activity?.getSharedPreferences(
                    "login", Context.MODE_PRIVATE)
                    sharedPreferences!!.edit().clear().apply()
            }
            dialogBuilder.setNegativeButton("No"){
                    _,_ ->

            }

            //show the dialogue
            val alert = dialogBuilder.create()
            alert.setCanceledOnTouchOutside(false)
            alert.show()
        }
//
//
//        requireActivity().onBackPressedDispatcher.addCallback {
//            requireActivity().finish()
//        }
        return binding.root
    }





}
