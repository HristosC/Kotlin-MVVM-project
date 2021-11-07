package com.example.threenitasproject_mvvm.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.feedback_module.activities.FeedbackActivity
import com.example.threenitasproject_mvvm.R
import com.example.threenitasproject_mvvm.databinding.FragmentSettingsBinding
import com.example.threenitasproject_mvvm.extensions.StartApp
import com.example.threenitasproject_mvvm.extensions.deleteFolder
import com.example.threenitasproject_mvvm.views.activities.LanguageActivity
import com.example.threenitasproject_mvvm.views.activities.Login


class SettingsFragment : Fragment(), View.OnClickListener {


    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        setListeners()
        return binding.root


    }


    private fun setListeners() {
        binding.deleteMagazines.setOnClickListener(this)
        binding.language.setOnClickListener(this)
        binding.feedback.setOnClickListener(this)
        binding.logout.setOnClickListener(this)
    }
    override fun onClick(v: View) {
        when (v.id) {
            R.id.deleteMagazines -> {
                deleteAllMagazines()
            }
            R.id.language -> {
                changeLanguageActivity()
            }
            R.id.feedback -> {
                feedbackActivity()
            }
            R.id.logout -> {
                logout()
            }
        }

    }

    private fun feedbackActivity() {
        val intent = Intent(activity, FeedbackActivity::class.java)
        startActivity(intent)
    }



    private fun deleteAllMagazines() {
        deleteFolder(context, "Magazines")
    }

    private fun changeLanguageActivity() {
        val intent = Intent(activity, LanguageActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
    private fun logout(){
        StartApp.sharedPreferencesProvider.putString("accessToken",
            null)
        val intent = Intent(activity, Login::class.java)
        startActivity(intent)
        activity?.finish()
    }
}