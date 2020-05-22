package com.example.agromallapplication.screens.login

import android.content.Context
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
import com.example.agromallapplication.databinding.FragmentLoginBinding
import com.example.agromallapplication.screens.viewmodel.FarmerViewModel
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var farmerViewModel: FarmerViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity().application as BaseApplication).component.inject(this)

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)

        //hide view
        hideView(binding.wrongEmail,binding.wrongPassword)

        //viewModel
        farmerViewModel = ViewModelProvider(this,viewModelFactory).get(FarmerViewModel::class.java)

        //remember user login
        val sharedPref = activity?.getSharedPreferences(
            "login", Context.MODE_PRIVATE)

        if (sharedPref!!.getBoolean("logged",false)){
            //go to dashboard
            val navigate =
                LoginFragmentDirections.actionLoginFragmentToDashBoardFragment2()
                findNavController().navigate(navigate)
        }


        //Login button
        binding.login.setOnClickListener {
            val userEmail = binding.email.text.toString()
            val userPassword = binding.password.text.toString()

            val validate = farmerViewModel.loginValidation(binding.root, userEmail, userPassword)

            if (validate){
                if (userEmail == "test@theagromall.com" && userPassword == "password") {
                    sharedPref.edit().putBoolean("logged", true).apply()
                    //move to the dashBoard
                    val navigate =
                        LoginFragmentDirections.actionLoginFragmentToDashBoardFragment2()
                    findNavController().navigate(navigate)
                    hideView(binding.wrongEmail, binding.wrongPassword)
                } else {
                    displayView(binding.wrongEmail, binding.wrongPassword)
                }
            }
        }

        return binding.root
    }



    private fun displayView(emailView: View, passwordView: View){

        emailView.visibility = View.VISIBLE
        passwordView.visibility = View.VISIBLE

    }


    private fun hideView(emailView: View, passwordView: View){

        emailView.visibility = View.GONE
        passwordView.visibility = View.GONE

    }


}
