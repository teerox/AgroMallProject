package com.example.agromallapplication.screens.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.agromallapplication.BaseApplication
import com.example.agromallapplication.R
import com.example.agromallapplication.databinding.LoginActivityBinding
import com.example.agromallapplication.screens.MainActivity
import com.example.agromallapplication.screens.viewmodel.FarmerViewModel
import javax.inject.Inject


class LoginActivity : AppCompatActivity() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var farmerViewModel: FarmerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:LoginActivityBinding  = DataBindingUtil.setContentView(
            this, R.layout.login_activity)
        (applicationContext as BaseApplication).component.inject(this)

        hideView(binding.wrongEmail,binding.wrongPassword)

        //viewModel
        farmerViewModel = ViewModelProvider(this,viewModelFactory).get(FarmerViewModel::class.java)

        //remember user login
        val sharedPref = this.getSharedPreferences(
            "login", Context.MODE_PRIVATE)

        if (sharedPref!!.getBoolean("logged",false)){
            //go to dashboard
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()

        }


        //Login button
        binding.login.setOnClickListener {
            val userEmail = binding.email.text.toString().replace(" ","")
            val userPassword = binding.password.text.toString().replace(" ","")

            val validate = farmerViewModel.loginValidation(binding.root, userEmail, userPassword)

            if (validate){
                if (userEmail == "test@theagromall.com" && userPassword == "password") {
                    sharedPref.edit().putBoolean("logged", true).apply()
                    //move to the dashBoard
                    val intent = Intent(applicationContext,MainActivity::class.java)
                    startActivity(intent)
                    finish()

                    hideView(binding.wrongEmail, binding.wrongPassword)
                } else {
                    displayView(binding.wrongEmail, binding.wrongPassword)
                }
            }
        }


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
