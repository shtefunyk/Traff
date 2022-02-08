package com.pampam.lib.data;

import android.app.Activity;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.pampam.lib.interfaces.IValueListener;

public class LoaderConfig {

    public static final String CONFIG_FIELD_ONE_SIGNAL_ID = "one_signal_id";
    public static final String CONFIG_FIELD_USE_NAMING = "use_naming";

    public static class Config {
        public String oneSignal;
        public boolean useNaming;
    }

    public static void prepareConfig(Activity context, IValueListener<Config> listener) {
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder().build());
        firebaseRemoteConfig
            .fetchAndActivate()
            .addOnCompleteListener(context, task -> {
                if(task.isSuccessful()) {
                    String oneSignal = firebaseRemoteConfig.getString(CONFIG_FIELD_ONE_SIGNAL_ID);
                    boolean useNaming = firebaseRemoteConfig.getBoolean(CONFIG_FIELD_USE_NAMING);

                    Config config = new Config();
                    config.oneSignal = oneSignal;
                    config.useNaming = useNaming;
                    listener.value(config);
                }
                else listener.failed();
            })
            .addOnFailureListener(e -> listener.failed());
    }
}
