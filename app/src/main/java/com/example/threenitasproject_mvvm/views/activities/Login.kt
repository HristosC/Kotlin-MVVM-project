package com.example.threenitasproject_mvvm.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.threenitasproject_mvvm.R
import com.example.threenitasproject_mvvm.databinding.ActivityLoginBinding
import com.example.threenitasproject_mvvm.extensions.StartApp.Companion.sharedPreferencesProvider
import com.example.threenitasproject_mvvm.extensions.checkForInternet
import com.example.threenitasproject_mvvm.extensions.isPasswordValid
import com.example.threenitasproject_mvvm.extensions.isUserIDValid
import com.example.threenitasproject_mvvm.extensions.setLocale
import com.example.threenitasproject_mvvm.viewmodels.activities.LoginViewModel
import java.util.*



class Login : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private val currentLang = Locale.getDefault().language
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initializeLateVar()
        setListeners()
        setObservers()
        setDefaultLanguageButton()
        setContentView(binding.root)
    }

    private fun setObservers() {
        //RedirectActivity variable Observer
        viewModel.changeActivity.observe(this, Observer<Boolean>{
            if(it){
                redirectActivity()
            }
        })
        //Recreate Activity for Locale Changes
        viewModel.reCreateActivity.observe(this,Observer<Boolean>{
            if(it){
                changeLocale()
                viewModel.activityRecreated()
            }
        })
        viewModel.showWrongCredentials.observe(this,Observer<Boolean>{
            if(it){
                binding.wrongcredentials.wrongCredentials.visibility = View.VISIBLE
            }
        })
        viewModel.passValid.observe(this,Observer<Boolean>{
            if(!it){
                binding.passText.error = getString(R.string.regex_error)

            }
        })
        viewModel.userValid.observe(this,Observer<Boolean>{
            if(!it){
                binding.userIdText.error = getString(R.string.regex_error)
            }
        })

    }

    private fun redirectActivity() {
        val intent = Intent(this, MainMenu::class.java)
        intent.putExtra("Fragment","Book")
        startActivity(intent)
        finish()
    }

    private fun initializeLateVar(){
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


    }

    private fun changeLocale(){
        val language = sharedPreferencesProvider.getString("locale")
        language?.let { setLocale(it,baseContext) }
        recreate()
    }

    private fun setDefaultLanguageButton(){
        if(currentLang=="en"){
            binding.defaultLanguage.setImageResource(R.drawable.united_states_of_america_flag_round)
            binding.defaultLanguageText.setText(R.string.englishButton)
        }
        else if(currentLang=="el"){
            binding.defaultLanguage.setImageResource(R.drawable.greece_flag_round_icon)
            binding.defaultLanguageText.setText(R.string.greekButton)
        }
    }

    private fun setLocale(lang:String){
        sharedPreferencesProvider.putString("locale",lang)
        viewModel.checkToRecreateActivity()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.englishButton ->
            {
                setLocale("en")
            }
            R.id.greekButton ->
            {
                setLocale("el")
            }
            R.id.defaultLanguageButton ->
            {
                showLanguageOptions()
            }
            R.id.loginButton ->
            {
                loginButtonClicked()
            }
            R.id.epistrofi ->
            {
                wrongCredentialsPopUpSettings()
            }
            R.id.infoID ->
            {
                infoIDClicked()
            }
            R.id.infoPass ->
            {
                infoPassClicked()
            }
            R.id.popup_bg ->
            {
                popUpBGClicked()
            }
            R.id.showPassword ->
            {
                showPasswordClicked()
            }
        }
    }
    private fun showPasswordClicked() {
        binding.passText.transformationMethod  = if (binding.passText.transformationMethod == PasswordTransformationMethod.getInstance()){
            HideReturnsTransformationMethod.getInstance()
        } else{
            PasswordTransformationMethod.getInstance()
        }
    }
    private fun popUpBGClicked() {
        binding.popupMessage.visibility = View.INVISIBLE
        binding.popupBg.visibility = View.INVISIBLE
        binding.popup.visibility = View.INVISIBLE
        binding.infoID.elevation = 0f
        binding.userIdMessage.elevation = 0f
        binding.infoPass.elevation = 0f
        binding.passMessage.elevation = 0f
    }
    private fun infoPassClicked() {
        binding.popupMessage.text = resources.getString(R.string.info_pass)
        binding.popupMessage.visibility = View.VISIBLE
        binding.popupBg.visibility = View.VISIBLE
        binding.popup.visibility = View.VISIBLE
        binding.infoPass.elevation = 50f
        binding.passMessage.elevation = 50f
    }
    private fun infoIDClicked(){
        binding.infoID.elevation = 50f
        binding.userIdMessage.elevation = 50f
        binding.popupMessage.text = resources.getString(R.string.info_user)
        binding.popupMessage.visibility = View.VISIBLE
        binding.popupBg.visibility = View.VISIBLE
        binding.popup.visibility = View.VISIBLE
    }
    private fun loginButtonClicked() {
        if(checkForInternet(this)){
            checkLoginInfo()
        }
        else{
            Toast.makeText(this,"There is no internet connection.\nPlease enable it!"
                ,Toast.LENGTH_SHORT).show()
        }
    }
    private fun wrongCredentialsPopUpSettings() {
        binding.wrongcredentials.wrongCredentials.visibility = View.INVISIBLE
        viewModel.wrongCredentialsFinish()
    }
    private fun setListeners() {
        binding.defaultLanguageButton.setOnClickListener(this)
        binding.greekButton.setOnClickListener(this)
        binding.englishButton.setOnClickListener(this)
        binding.loginButton.setOnClickListener(this)
        binding.wrongcredentials.epistrofi.setOnClickListener(this)
        binding.infoID.setOnClickListener(this)
        binding.infoPass.setOnClickListener(this)
        binding.popupBg.setOnClickListener(this)
        binding.showPassword.setOnClickListener(this)
    }
    private fun showLanguageOptions(){
        binding.greekButton.visibility = if (binding.greekButton.visibility == View.VISIBLE){
            View.INVISIBLE
        } else{
            View.VISIBLE
        }
        binding.englishButton.visibility = if (binding.englishButton.visibility == View.VISIBLE){
            View.INVISIBLE
        } else{
            View.VISIBLE
        }
    }
    private fun checkLoginInfo(){
        val usernameText = binding.userIdText.text.toString()
        val passwordText = binding.passText.text.toString()
        viewModel.setValidRegexTrue()
        viewModel.checkValidRegex(usernameText,passwordText)




    }



}