package com.example.threenitasproject_mvvm.music_player

import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context


class NotificationActionService : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        context.sendBroadcast(
            Intent("streaming")
                .putExtra("actionname", intent.action)
        )
    }
}