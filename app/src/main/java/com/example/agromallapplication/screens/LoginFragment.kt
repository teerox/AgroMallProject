package com.example.agromallapplication.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.agromallapplication.R
import com.example.agromallapplication.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*

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

        val userEmail = binding.email.text.toString()
        val userPassword = binding.password.text.toString()

        //Login button
        binding.login.setOnClickListener {
            val validate = login(userEmail,userPassword)
            if (validate){
                //move to the dashBoard
                hideView(binding.wrongEmail,binding.wrongPassword)
            }else{
                displayView(binding.wrongEmail,binding.wrongPassword)
            }
        }


        return binding.root
    }


    private fun login(email:String, password:String):Boolean{
        return email == "test@theagromall.com" && password == "password"
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
