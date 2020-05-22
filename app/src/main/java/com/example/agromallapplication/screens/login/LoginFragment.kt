package com.example.agromallapplication.screens.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.agromallapplication.R
import com.example.agromallapplication.databinding.FragmentLoginBinding


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        hideView(binding.wrongEmail,binding.wrongPassword)


        //Login button
        binding.login.setOnClickListener {
            val userEmail = binding.email.text.toString()
            val userPassword = binding.password.text.toString()
            Log.e("Info",userEmail)
            Log.e("Info",userPassword)

            if (userEmail == "test@theagromall.com " && userPassword == "password"){
                //move to the dashBoard
                val navigate =
                    LoginFragmentDirections.actionLoginFragmentToDashBoardFragment2()
                findNavController().navigate(navigate)
                hideView(binding.wrongEmail,binding.wrongPassword)
            }else{
                displayView(binding.wrongEmail,binding.wrongPassword)
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
