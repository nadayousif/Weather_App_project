package com.example.weatherappproject.ui.alert.notificationAlert

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weatherappproject.R
import com.example.weatherappproject.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {
    var alarm = com.example.weatherappproject.ui.alert.notificationAlert.AlertRecevier.alarm
    private var _binding: ActivityNotificationBinding? = null
    private val binding get() = _binding
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        Log.i("TAG", "getLocalWeathers: notification")
        _binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
        val notificationHelper = Notification(applicationContext)
        binding?.cancelAlarm?.setOnClickListener {
            alarm.stop()
            notificationHelper.alarmNotificationManager(applicationContext).cancel(1)
            finish()
        }
    }
}