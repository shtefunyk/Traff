package com.pampam.lib777;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.traffappscorelib.wsc.App;

public class TestApp extends App {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
    }

    @NonNull
    @Override
    public String getAppsflyerAppId() {
        return "tuRVfbsRT7QoUMoFqjUL8a";
    }
}
