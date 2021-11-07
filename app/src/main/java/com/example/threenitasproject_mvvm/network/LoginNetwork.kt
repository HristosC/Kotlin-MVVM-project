package com.example.threenitasproject_mvvm.network

import android.widget.Toast
import com.example.threenitas_project.data_classes.LoginResponse
import com.example.threenitas_project.network.ServiceBuilder
import com.example.threenitasproject_mvvm.extensions.StartApp.Companion.applicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object LoginNetwork {

    fun checkLogin(
        username: String,
        password: String,
        callback: (LoginResponse?) -> Unit
    )
    {


        ServiceBuilder.instance.sendLogin(username,password)
            .enqueue(object: Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>,
                ) {
                    callback(response.body())

                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(applicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show()
                }

            })
    }
}