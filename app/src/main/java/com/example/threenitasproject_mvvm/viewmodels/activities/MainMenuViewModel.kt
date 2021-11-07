package com.example.threenitasproject_mvvm.viewmodels.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainMenuViewModel: ViewModel() {
    private val _selectedFragment = MutableLiveData<String>()
    val selectedFragment: LiveData<String>
        get()=_selectedFragment

    fun setSelectedFragment(selectedFragment:String){
        this._selectedFragment.value = selectedFragment
    }
}
