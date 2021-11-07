package com.example.threenitasproject_mvvm.music_player

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import android.os.Build
import androidx.core.app.NotificationCompat
import android.app.PendingIntent
import android.content.Intent
import com.example.threenitasproject_mvvm.R



object CreateNotification {
    const val CHANNEL_ID = "channel1"
    const val ACTION_PLAY = "actionplay"

    private lateinit var notification: Notification


    @SuppressLint("UnspecifiedImmutableFlag")
    fun createNotification(context: Context, playButton: Int,status: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            val intentPlay = Intent(context, NotificationActionService::class.java)
                .setAction(ACTION_PLAY)
            val pendingIntentPlay = PendingIntent.getBroadcast(
                context, 0,
                intentPlay, PendingIntent.FLAG_UPDATE_CURRENT
            )


            //create notification
            notification =NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_note)
                .setContentTitle("Live Streaming")
                .setContentText(status)
                .setOnlyAlertOnce(true) //show notification for only first time
                .setShowWhen(false)
                .addAction(R.drawable.ic_transparent,"",null)
                .addAction(R.drawable.ic_transparent,"",null)
                .addAction(playButton,"Play",pendingIntentPlay)
                .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0,1,2)
                )
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()
            if(status=="Playing"){
                notification.flags = Notification.FLAG_ONGOING_EVENT
            }

            notificationManagerCompat.notify(1, notification)
        }
    }
}