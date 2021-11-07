package com.example.threenitasproject_mvvm.network

import android.widget.Toast
import com.example.threenitas_project.network.ServiceBuilder
import com.example.threenitasproject_mvvm.models.BooksResponse
import com.example.threenitasproject_mvvm.extensions.StartApp
import com.example.threenitasproject_mvvm.extensions.StartApp.Companion.sharedPreferencesProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object BooksNetwork {

    fun checkBooks(
        callback: (List<BooksResponse>?) -> Unit
    )
    {


        ServiceBuilder.instance.fetchBooks(sharedPreferencesProvider
            .getString("accessToken")
            .toString())
            .enqueue(object: Callback<List<BooksResponse>> {
                override fun onResponse(
                    call: Call<List<BooksResponse>>,
                    response: Response<List<BooksResponse>>,
                ) {
                    for(i in response.body()!!.indices) {
                        response.body()!![i].img_url = response.body()!![i]
                            .img_url
                            .replace("http","https")
                        if (!response.body()!![i].pdf_url.contains("https")){
                            response.body()!![i].pdf_url = response.body()!![i]
                                .pdf_url
                                .replace("http","https")
                        }

                    }


                    callback(response.body())
                }

                override fun onFailure(call: Call<List<BooksResponse>>, t: Throwable) {
                    Toast.makeText(StartApp.applicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show()
                }
            })
    }

}