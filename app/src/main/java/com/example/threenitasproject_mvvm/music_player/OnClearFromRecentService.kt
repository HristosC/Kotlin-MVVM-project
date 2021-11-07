package com.example.threenitasproject_mvvm.music_player

import android.app.Service
import android.content.Intent
import androidx.annotation.Nullable

import android.os.IBinder


class OnClearFromRecentService : Service() {
    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopSelf()
    }
}