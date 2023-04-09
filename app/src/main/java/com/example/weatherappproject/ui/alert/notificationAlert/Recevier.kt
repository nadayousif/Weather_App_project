package com.example.weatherappproject.ui.alert.notificationAlert

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherappproject.util.Constants


class Recevier: BroadcastReceiver() {
    lateinit var  alarm: Ringtone
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        val notification= Notification(context)
        if (intent.action.equals(Constants.ACTION_SNOOZE)) {
            alarm.stop()
            notification.alarmNotificationManager(context).cancel(1)
        }

    }
}