package com.pampam.lib777;

import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.example.trafficappscorelib.R;
import com.pampam.lib.notifications.BaseAlarmReceiver;

public class AlartReceiver extends BaseAlarmReceiver {

    @Override
    public int getSmallIconRes() {
        return R.drawable.ic_notification_eur;
    }

    @Override
    public int getSmallIconColor() {
        return R.color.orange;
    }

    @NonNull
    @Override
    public Class<?> getActivityOpen() {
        return MainActivity.class;
    }

    @NonNull
    @Override
    public RemoteViews getNotificationLayout(String text) {
        RemoteViews notificationLayout = new RemoteViews("com.pampam.lib777", R.layout.notification_collapsed);
        notificationLayout.setCharSequence(R.id.collapsed_notification_title, "setText", text);
        return notificationLayout;
    }
}
