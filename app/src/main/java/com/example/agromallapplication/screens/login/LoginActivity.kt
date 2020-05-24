package com.example.agromallapplication.screens.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
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
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val binding:LoginActivityBinding  = DataBindingUtil.setContentView(
            this, R.layout.login_activity)
        (applicationContext as BaseApplication).component.inject(this)

        //viewModel
        farmerViewModel = ViewModelProvider(this,viewModelFactory).get(FarmerViewModel::class.java)

        farmerViewModel.hideView(binding.wrongEmail,binding.wrongPassword)
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

            val validate = farmerViewModel.loginValidation(binding.root,
                userEmail,
                userPassword,
                binding.wrongEmail,binding.wrongPassword,binding.email,binding.password,this)

            if (validate){
                    sharedPref.edit().putBoolean("logged", true).apply()
                    //move to the dashBoard
                    val intent = Intent(applicationContext,MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }

        }


    }






}
