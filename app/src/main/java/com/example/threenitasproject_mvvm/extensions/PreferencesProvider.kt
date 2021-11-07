package com.example.threenitasproject_mvvm.extensions

import android.content.Context


class PreferencesProvider(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("myPreferences",0)

    fun putString(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key,null)
    }
    fun doesContain(key: String): Boolean{
        return sharedPreferences.contains(key)
    }
}