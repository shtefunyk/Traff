package com.pampam.lib.data;

import android.app.Activity;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.pampam.lib.interfaces.IValueListener;

public class LoaderConfig {

    public static final String CONFIG_FIELD_ONE_SIGNAL_ID = "one_signal_id";

    public static class Config {
        public String oneSignal;
    }

    public static void prepareConfig(Activity context, IValueListener<Config> listener) {
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder().build());
        firebaseRemoteConfig
            .fetchAndActivate()
            .addOnCompleteListener(context, task -> {
                if(task.isSuccessful()) {
                    String oneSignal = firebaseRemoteConfig.getString(CONFIG_FIELD_ONE_SIGNAL_ID);

                    Config config = new Config();
                    config.oneSignal = oneSignal;
                    listener.value(config);
                }
                else listener.failed();
            })
            .addOnFailureListener(e -> listener.failed());
    }
}
