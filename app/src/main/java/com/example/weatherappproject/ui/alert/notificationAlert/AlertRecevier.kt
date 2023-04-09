package com.example.weatherappproject.ui.alert.notificationAlert

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.weatherappproject.localData.LocalDataSource
import com.example.weatherappproject.localData.MyDatabase
import com.example.weatherappproject.model.Alert
import com.example.weatherappproject.remoteData.RemoteDataSource
import com.example.weatherappproject.repositary.Repositary
import com.example.weatherappproject.util.Constants
import com.example.weatherappproject.util.Utils
import com.google.android.gms.common.api.Api
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch



class AlertRecevier : BroadcastReceiver() {
    lateinit var notificationManager: NotificationManager
    var notificationId: Int? = null
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        lateinit var notification: Uri
        lateinit var alarm: Ringtone
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        sharedPreferences = context.getSharedPreferences("My Shared", Context.MODE_PRIVATE)
        val repo = Repositary.getInstance(
            RemoteDataSource.getInstance(), LocalDataSource(context),
            PreferenceManager.getDefaultSharedPreferences(context)
        )
        var alertSettings = repo.getAlertSettings()
        val alertJson = intent.getStringExtra(Constants.Alert)
        var alert = Gson().fromJson(alertJson, Alert::class.java)
        val notificationHelper = Notification(context)
        notificationId = 1

        notificationManager = notificationHelper.alarmNotificationManager(context)

        Toast.makeText(context, "Alarm", Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.IO).launch {
            if (!Utils.isDaily(alert.startDay, alert.endDay)) {
                Utils.canelAlarm(context, alert.toString(), alert.startDay.toInt())
                repo.deleteAlert(alert)
                WorkManager.getInstance(context.applicationContext)
                    .cancelAllWorkByTag(alert.startDay.toString())
            }
            try {
                repo.getWeatherOverNetwork(
                    lat = alert.lat,
                    lon = alert.lon,
                    "en",
                    "unit",
                ).collectLatest {
                    val bitmap = arrayOf<Bitmap?>(null)
                    Glide.with(context)
                        .asBitmap()
                        .load(Utils.getIconUrl(it.body()?.current!!.weather[0].icon))
                        .into(object : CustomTarget<Bitmap?>() {
                            @RequiresApi(Build.VERSION_CODES.S)
                            override fun onResourceReady(
                                resource: Bitmap,
                                @Nullable transition: Transition<in Bitmap?>?
                            ) {
                                bitmap[0] = resource
                                notification = RingtoneManager.getActualDefaultRingtoneUri(
                                    context.applicationContext,
                                    RingtoneManager.TYPE_ALARM
                                )
                                alarm = RingtoneManager.getRingtone(
                                    context.applicationContext,
                                    notification
                                )
                                if (alertSettings?.isALarm == true && !alertSettings.isNotification) {
                                    alarm.play()
                                    notificationManager.notify(
                                        notificationId!!, notificationHelper.getNotification(
                                            context,
                                            notificationId!!,
                                            Utils.getAddressEnglish(
                                                context,
                                                alert.lat,
                                                alert.lon
                                            ),
                                            it.body()!!.current.weather[0].description,
                                            bitmap[0]!!
                                        )
                                    )

                                }
                                if (alertSettings?.isALarm == false && alertSettings.isNotification) {
                                    notificationManager.notify(
                                        notificationId!!, notificationHelper.getNotificationBuilder(
                                            Utils.getAddressEnglish(
                                                context,
                                                alert.lat,
                                                alert.lon
                                            )!!,
                                            it.body()!!.current.weather[0].description,
                                            context,
                                            bitmap[0]!!

                                        ).build()
                                    )
                                }
                            }

                            override fun onLoadCleared(@Nullable placeholder: Drawable?) {

                            }
                        })
                }
            } finally {
                cancel()
            }

        }


    }


}
