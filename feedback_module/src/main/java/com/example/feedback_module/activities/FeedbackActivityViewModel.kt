package com.example.feedback_module.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FeedbackActivityViewModel: ViewModel() {
    private val _tabsList = MutableLiveData<MutableList<String>>()
    val tabsList: LiveData<MutableList<String>>
        get()=_tabsList
    private val _email = MutableLiveData<Array<String>>()
    val email: LiveData<Array<String>>
        get()=_email

    private fun setTabList()/*here you setup your tabs array */ {
        val list : MutableList<String> = ArrayList()
        list.add("Login")
        list.add("Book Page")
        list.add("Radio")
        list.add("Settings")
        _tabsList.value = list
    }

    private fun setEmail(){

        _email.value = "ccondrea@threenitas.com".split(",".toRegex()).toTypedArray()
    }
    init {

        setEmail()
        setTabList()

    }
}