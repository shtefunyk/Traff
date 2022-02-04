package com.pampam.lib.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.pampam.lib.data.Preferences

abstract class BaseAlarmReceiver : BroadcastReceiver() {

    abstract fun getSmallIconRes() : Int
    abstract fun getSmallIconColor() : Int
    abstract fun getNotificationLayout(text: String) : RemoteViews
    abstract fun getActivityOpen() : Class<*>

    private val NOTIFICATION_ID = "10010"

    override fun onReceive(context: Context, intent: Intent) {

        val preferences = Preferences(context.getSharedPreferences(
            Preferences.PREFS_NAME,
            Context.MODE_PRIVATE
        ))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(
                NOTIFICATION_ID,
                "Robin",
                importance
            )
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(mChannel)
        }

        val resultIntent = Intent(context, getActivityOpen())
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_ID)
            .setSmallIcon(getSmallIconRes())
            .setColor(ContextCompat.getColor(context, getSmallIconColor()))
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(getNotificationLayout(preferences.notificationText))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            notify(717, notificationBuilder.build())
        }
    }
}