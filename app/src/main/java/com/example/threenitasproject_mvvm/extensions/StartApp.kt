package com.example.threenitasproject_mvvm.extensions


import android.app.Application
import android.content.Context

class StartApp : Application() {

    init{
        instance = this
    }

    companion object{
        private var instance: StartApp? = null
        lateinit var sharedPreferencesProvider: PreferencesProvider

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferencesProvider = PreferencesProvider(applicationContext)
    }


}