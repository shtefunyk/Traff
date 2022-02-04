package com.pampam.lib.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.concurrent.TimeUnit

class NotificationsManager(context: Context, cls: Class<*>) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val alarmPendingIntent by lazy {
        val intent = Intent(context, cls)
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
    }

    fun schedulePushNotifications(startMins: Long, intervalMins: Long) {
        val start = TimeUnit.MINUTES.toMillis(startMins) + System.currentTimeMillis()
        val interval = if(intervalMins > 0) TimeUnit.MINUTES.toMillis(intervalMins) else TimeUnit.DAYS.toMillis(300)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            start,
            interval,
            alarmPendingIntent
        )
    }
}