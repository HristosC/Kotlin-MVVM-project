package com.example.threenitasproject_mvvm.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.threenitasproject_mvvm.R
import com.example.threenitasproject_mvvm.databinding.ActivityLanguageBinding
import com.example.threenitasproject_mvvm.extensions.StartApp
import com.example.threenitasproject_mvvm.extensions.setLocale



class LanguageActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var binding: ActivityLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        checkLanguage()

    }

    private fun checkLanguage() {

        if(StartApp.sharedPreferencesProvider.doesContain("locale")){
            val language = StartApp.sharedPreferencesProvider.getString("locale")
            if(language.equals("en")){
                binding.englishCheck.visibility = View.VISIBLE
            }
            else{
                binding.greekCheck.visibility = View.VISIBLE
            }
        }
    }

    private fun setListeners() {
        binding.backIcon.setOnClickListener(this)
        binding.englishConstraint.setOnClickListener(this)
        binding.greekConstraint.setOnClickListener(this)

    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.backIcon -> {
                goBack()
            }
            R.id.englishConstraint -> {
                if(binding.englishCheck.visibility==View.INVISIBLE){
                    setLocale("en",this)
                    recreate()
                }
            }
            R.id.greekConstraint -> {
                if(binding.greekCheck.visibility==View.INVISIBLE){
                    setLocale("el",this)
                    recreate()
                }
            }

        }
    }


    private fun goBack(){
        val intent = Intent(this,MainMenu::class.java)
        intent.putExtra("Fragment", "Settings")
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        goBack()
    }









}