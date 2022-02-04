package com.pampam.lib.data;

import android.content.SharedPreferences;

public class Preferences {

    private SharedPreferences preferences;

    public static final String PREFS_NAME = "robin_prefs";
    public static final String PREFS_FIRST_LAUNCH = "PREFS_FIRST_LAUNCH";
    public static final String PREFS_URL = "PREFS_URL";
    public static final String PREFS_SAVE_LAST_URL = "PREFS_SAVE_LAST_URL";

    public static final String PREFS_NOTIFICATIONS_START_MINS = "PREFS_NOTIFICATION_START";
    public static final String PREFS_NOTIFICATIONS_INTERVAL_MINS = "PREFS_NOTIFICATION_INTERVAL";
    public static final String PREFS_NOTIFICATIONS_TITLE = "PREFS_NOTIFICATION_TITLE";
    public static final String PREFS_NOTIFICATIONS_TEXT = "PREFS_NOTIFICATION_TEXT";

    public Preferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void saveUrl(String url) {
        preferences.edit().putString(PREFS_URL, url).apply();
    }

    public String getUrl() {
        return preferences.getString(PREFS_URL, null);
    }

    public void setSaveLastUrl(boolean saveLastUrl) {
        preferences.edit().putBoolean(PREFS_SAVE_LAST_URL, saveLastUrl).apply();
    }

    public boolean getSaveLastUrl() {
        return preferences.getBoolean(PREFS_SAVE_LAST_URL, false);
    }

    public void saveNotification(String title, String text, Long start, Long interval) {
        preferences.edit().putString(PREFS_NOTIFICATIONS_TITLE, title).apply();
        preferences.edit().putString(PREFS_NOTIFICATIONS_TEXT, text).apply();
        preferences.edit().putLong(PREFS_NOTIFICATIONS_START_MINS, start).apply();
        preferences.edit().putLong(PREFS_NOTIFICATIONS_INTERVAL_MINS, interval).apply();
    }

    public String getNotificationTitle() {
        return preferences.getString(PREFS_NOTIFICATIONS_TITLE, null);
    }

    public String getNotificationText() {
        return preferences.getString(PREFS_NOTIFICATIONS_TEXT, null);
    }

    public Long getNotificationStartMins() {
        return preferences.getLong(PREFS_NOTIFICATIONS_START_MINS, 0);
    }

    public Long getNotificationIntervalMins() {
        return preferences.getLong(PREFS_NOTIFICATIONS_INTERVAL_MINS, 0);
    }
}
