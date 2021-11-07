package com.example.threenitasproject_mvvm.views.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.threenitasproject_mvvm.views.fragments.SettingsFragment
import com.example.threenitasproject_mvvm.R
import com.example.threenitasproject_mvvm.databinding.ActivityMainMenuBinding
import com.example.threenitasproject_mvvm.music_player.CreateNotification
import com.example.threenitasproject_mvvm.music_player.CreateNotification.createNotification
import com.example.threenitasproject_mvvm.music_player.OnClearFromRecentService
import com.example.threenitasproject_mvvm.music_player.Playable
import com.example.threenitasproject_mvvm.viewmodels.activities.LoginViewModel
import com.example.threenitasproject_mvvm.viewmodels.activities.MainMenuViewModel
import com.example.threenitasproject_mvvm.views.fragments.BookFragment
import com.example.threenitasproject_mvvm.views.fragments.LinkFragment
import com.example.threenitasproject_mvvm.views.fragments.MiscFragment


class MainMenu : AppCompatActivity(), Playable {

    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var viewModel: MainMenuViewModel
    private lateinit var selectedFragment: String
    private lateinit var bookFragment: BookFragment
    private lateinit var linkFragment : LinkFragment
    private lateinit var miscFragment : MiscFragment
    private lateinit var settingsFragment : SettingsFragment
    private lateinit var mediaPlayer: MediaPlayer
    var isPlaying = false
    private lateinit var notificationManager :NotificationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeLateInit()
        setContentView(binding.root)
        setObservers()
        setBottomNavViews()
        setSelectedFragmentFromIntent()
        mediaPlayerInitialize()
        onItemClick()
        createChannel()
        playButton()
    }



    private fun initializeLateInit() {
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(MainMenuViewModel::class.java)
        selectedFragment = intent.getStringExtra("Fragment").toString();
        bookFragment = BookFragment()
        linkFragment = LinkFragment()
        miscFragment = MiscFragment()
        settingsFragment = SettingsFragment()
    }

    private fun setObservers(){
        viewModel.selectedFragment.observe(this, Observer<String>{
            if(it == "Book"){
                binding.bottomNavBar.selectedItemId = R.id.book
                makeCurrentFragment(bookFragment)
            }
            else if (it == "Settings"){
                binding.mainPageTitle.setText(R.string.settings)
                binding.bottomNavBar.selectedItemId = R.id.settings
                makeCurrentFragment(settingsFragment)
            }
        })
    }

    private fun setSelectedFragmentFromIntent() {
        viewModel.setSelectedFragment(selectedFragment)
    }
    private fun setBottomNavViews() {
        binding.bottomNavBar.itemIconTintList = null
        binding.bottomNavBar.menu.findItem(R.id.nav_hidden_option).isEnabled = false
    }
    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_place, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
    private fun onItemClick(){
        binding.bottomNavBar.setOnItemSelectedListener  {item ->
            when(item.itemId){
                R.id.book -> {
                    makeCurrentFragment(bookFragment)
                    binding.mainPageTitle.setText(R.string.books)
                }
                R.id.link -> makeCurrentFragment(linkFragment)
                R.id.misc -> makeCurrentFragment(miscFragment)
                R.id.settings -> {
                    makeCurrentFragment(settingsFragment)
                    binding.mainPageTitle.setText(R.string.settings)
                }
            }
            true
        }
    }
    override fun onBackPressed() {
        if(binding.bottomNavBar.selectedItemId == R.id.settings ||
                binding.bottomNavBar.selectedItemId == R.id.misc ||
                binding.bottomNavBar.selectedItemId == R.id.link){
            goBack()
        }
        else if(binding.bottomNavBar.selectedItemId == R.id.book ){
            super.onBackPressed()
        }
    }

    private fun goBack() {
        binding.bottomNavBar.selectedItemId = R.id.book
        makeCurrentFragment(bookFragment)
    }

    private fun mediaPlayerInitialize() {
        mediaPlayer = MediaPlayer.create(this@MainMenu, Uri.parse("https://tonesmp3.com/ringtones/tarasti-hai-nigahen-meri-asim-azhar.mp3"))
    }

    private fun playButton() {
        binding.bottomNavBarButton.setOnClickListener{
            if(isPlaying){
                onTrackPause()
            }
            else{
                onTrackPlay()
            }


        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CreateNotification.CHANNEL_ID,
                "Radio Streaming", NotificationManager.IMPORTANCE_LOW
            )
            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

        }
        registerReceiver(broadcastReceiver, IntentFilter("streaming"))
        startService(Intent(baseContext, OnClearFromRecentService::class.java))
    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.extras!!.getString("actionname")) {
                CreateNotification.ACTION_PLAY -> if (isPlaying) {
                    onTrackPause()
                } else {
                    onTrackPlay()
                }
            }
        }
    }

    override fun onTrackPlay() {
        createNotification(
            baseContext,
            R.drawable.ic_pause,
            "Playing"
        )
        binding.bottomNavBarButton.setImageResource(R.drawable.ic_btn_pause)
        isPlaying = true
        mediaPlayer.start()
    }

    override fun onTrackPause() {
        createNotification(
            baseContext,
            R.drawable.ic_play,
            "Paused"
        )
        binding.bottomNavBarButton.setImageResource(R.drawable.ic_btn_play)
        isPlaying = false
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.cancelAll()
        }
        unregisterReceiver(broadcastReceiver)
    }
}