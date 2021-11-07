package com.example.threenitasproject_mvvm.viewmodels.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threenitasproject_mvvm.network.LoginNetwork
import com.example.threenitasproject_mvvm.extensions.StartApp.Companion.sharedPreferencesProvider
import com.example.threenitasproject_mvvm.extensions.isPasswordValid
import com.example.threenitasproject_mvvm.extensions.isUserIDValid


class LoginViewModel: ViewModel() {


    private val _changeActivity = MutableLiveData<Boolean>()
    private val _reCreateActivity = MutableLiveData<Boolean>()
    private val _showWrongCredentials = MutableLiveData<Boolean>()
    private val _userValid = MutableLiveData<Boolean>()
    private val _passValid = MutableLiveData<Boolean>()
    val userValid:LiveData<Boolean>
        get()= _userValid
    val passValid:LiveData<Boolean>
        get()= _passValid
    val reCreateActivity:LiveData<Boolean>
        get()=_reCreateActivity
    val changeActivity: LiveData<Boolean>
        get() = _changeActivity
    val showWrongCredentials: LiveData<Boolean>
        get() = _showWrongCredentials

    private fun userLoggedIn() {
        if (sharedPreferencesProvider.doesContain("accessToken")) {
            val accessToken: String? = sharedPreferencesProvider.getString("accessToken")
            if(accessToken != null){
                _changeActivity.value = true
            }
        }
    }
    fun checkToRecreateActivity(){
        _reCreateActivity.value = sharedPreferencesProvider.doesContain("locale")
    }
    init{
        checkToRecreateActivity()
        userLoggedIn()
    }
    fun activityRecreated(){
        _reCreateActivity.value = false
    }

    fun wrongCredentialsFinish(){
        _showWrongCredentials.value = false
    }


    private fun createLoginResponse(usernameText:String, passwordText:String){
        LoginNetwork.checkLogin(
            usernameText
            ,passwordText
        ) {response ->
            if (response?.access_token != null) {
                sharedPreferencesProvider.putString("accessToken",
                    response.access_token)
                userLoggedIn()
            } else {
                _showWrongCredentials.value = true
            }
        }
    }

    fun checkValidRegex(usernameText: String,passwordText: String){
        if(!usernameText.isUserIDValid()){
            _userValid.value = false
        }
        if(!passwordText.isPasswordValid()){
            _passValid.value = false
        }
        if(passwordText.isPasswordValid() && usernameText.isUserIDValid()){
            createLoginResponse(usernameText,passwordText)
        }
    }
    fun setValidRegexTrue(){
        _userValid.value= true
        _passValid.value = true
    }


}