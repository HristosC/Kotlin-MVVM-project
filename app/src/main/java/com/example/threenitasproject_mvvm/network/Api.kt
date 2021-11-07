package com.example.threenitasproject_mvvm.network

import com.example.threenitasproject_mvvm.models.BooksResponse
import com.example.threenitas_project.data_classes.LoginResponse
import retrofit2.Call
import retrofit2.http.*


interface Api {

    @FormUrlEncoded
    @POST("Login")
    fun sendLogin(
        @Field("UserName") UserName: String,
        @Field("Password") Password: String,
    ): Call<LoginResponse>

    @GET("Books")
    fun fetchBooks(@Header("Authorization") authToken: String):Call<List<BooksResponse>>


}