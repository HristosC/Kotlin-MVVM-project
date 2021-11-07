package com.example.feedback_module.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.example.feedback_module.R
import com.example.feedback_module.databinding.ActivityFeedbackBinding
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent
import android.provider.MediaStore
import android.content.DialogInterface
import android.net.Uri
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import com.example.feedback_module.BuildConfig
import java.io.File
import android.content.pm.PackageInfo
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class FeedbackActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var binding: ActivityFeedbackBinding
    private lateinit var viewModel: FeedbackActivityViewModel
    private lateinit var email: Array<String>
    private lateinit var tabList: MutableList<String>
    private lateinit var getFile: ActivityResultLauncher<String>
    private lateinit var saveFile:Uri
    private var addedPicture = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(FeedbackActivityViewModel::class.java)
        setContentView(binding.root)
        setListeners()
        setObservers()
        setGetFile()


    }

    private fun setObservers() {
        viewModel.email.observe(this, Observer<Array<String>>{
            email = it
            Toast.makeText(this,"EMAIL OK",Toast.LENGTH_SHORT).show()
        })
        viewModel.tabsList.observe(this, Observer<MutableList<String>> {
            tabList = it
            setSpinner(this,tabList)
            Toast.makeText(this,"tabs list OK",Toast.LENGTH_SHORT).show()
        })
    }

    private fun setGetFile() {
        getFile = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                saveFile = it
            }
        )
    }

   /* private fun setEmail(email:String){
        this.email = email.split(",".toRegex()).toTypedArray()
    }
    private fun setTabList()/*here you setup your tabs array */ {
        tabList = ArrayList()
        tabList.add("Login")
        tabList.add("Book Page")
        tabList.add("Radio")
        tabList.add("Settings")
    }*/

    private fun setListeners() {
        binding.backIcon.setOnClickListener(this)
        binding.addPictureButton.setOnClickListener(this)
        binding.submitButton.setOnClickListener(this)

    }

    private fun setSpinner(context: Context,list:MutableList<String>){
        val adapter :ArrayAdapter<String> = ArrayAdapter(context,R.layout.support_simple_spinner_dropdown_item,list)
        binding.spinner.adapter = adapter
        Toast.makeText(this,"tabs list OK",Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.backIcon -> {
                goBack()
            }
            R.id.addPictureButton -> {
                addPicture()
            }
            R.id.submitButton -> {
                submit()
            }

        }
    }
    private fun submit(){
        openEmail()
    }
    private fun goBack(){
        finish()
    }
    private fun addPicture(){
        getFile.launch("image/* video/*")
        addedPicture = true
    }
    private fun openEmail(){
        val bodyText = bodyText()
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, email)
        intent.putExtra(Intent.EXTRA_SUBJECT, binding.spinner.selectedItem.toString())
        if(addedPicture){
            intent.putExtra(Intent.EXTRA_STREAM, saveFile)
        }
        intent.putExtra(Intent.EXTRA_TEXT,bodyText)
        intent.type = "text/*"
        intent.type = "image/* video/*"
        startActivity(intent)
    }
    private fun bodyText():String{
        var bodyText = binding.feedbackText.text.toString()
        val pInfo: PackageInfo =
            baseContext.packageManager.getPackageInfo(baseContext.packageName, 0)
        val appVersionName = pInfo.versionName
        val myVersion = Build.VERSION.RELEASE
        val phoneModel = Build.MODEL
        bodyText = "$bodyText\nApp Version name: $appVersionName\nAndroid Version: $myVersion\nPhone Model: $phoneModel"
        return (bodyText)
    }
}