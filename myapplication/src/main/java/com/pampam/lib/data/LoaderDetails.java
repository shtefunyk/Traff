package com.pampam.lib.data;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pampam.lib.interfaces.IValueListener;

public class LoaderDetails {

    public static class Info {
        public String url;
        public String urlHideLoad;
        public String countriesAllowed;
        public boolean saveLastUrl;
        public Notification notification;
    }

    public static class Notification {
        public String title;
        public String text;
        public Long start; // Minutes
        public Long interval; // Minutes
    }

    public static void loadInfo(String id, IValueListener<Info> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(id);
        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if(document == null || !document.exists() || document.getData() == null) listener.failed();
                else {
                    String url = getFieldString(document, "url");
                    String trafficRedirectUrl = getFieldString(document, "traff_redirect_url");
                    Long trafficRedirectPercent = getFieldNumber(document, "traff_redirect_percent");
                    Long installs = getFieldNumber(document, "installs");

                    String notificationText = getFieldString(document, "notification_text");
                    Long notificationStart = getFieldNumber(document, "notification_start_mins");
                    Long notificationInterval = getFieldNumber(document, "notification_interval_mins");

                    boolean saveLastUrl = getFieldBoolean(document, "save_last_url");
                    String countriesAllowed = getFieldString(document, "countries");

                    Info info = new Info();
                    info.saveLastUrl = saveLastUrl;
                    info.countriesAllowed = countriesAllowed;
                    info.url = trafficRedirectUrl == null || trafficRedirectPercent == null || installs == null
                        ? url
                        : useTrafficRedirectUrl(installs, trafficRedirectPercent) ? trafficRedirectUrl : url;

                    if(info.url != null && info.url.equals(trafficRedirectUrl)) info.urlHideLoad = url;

                    if(notificationText != null && notificationStart != null) {
                        Notification notification = new Notification();
                        notification.text = notificationText;
                        notification.interval = notificationInterval;
                        notification.start = notificationStart;

                        info.notification = notification;
                    }

                    if(installs != null) docRef.update("installs", installs + 1);

                    listener.value(info);
                }

            }
            else listener.failed();
        }).addOnFailureListener(e -> listener.failed());
    }

    private static String getFieldString(DocumentSnapshot document, String fieldName) {
        Object object = document.getData().get(fieldName);
        return object == null ? null : object.toString();
    }

    private static Long getFieldNumber(DocumentSnapshot document, String fieldName) {
        Object object = document.getData().get(fieldName);
        return object == null ? null : (Long) object;
    }

    private static boolean getFieldBoolean(DocumentSnapshot document, String fieldName) {
        Object object = document.getData().get(fieldName);
        return object != null && (boolean) object;
    }

    private static boolean useTrafficRedirectUrl(long installsCount, long redirectPercent) {
        int additionalUrlCoefficient = (int) (100 / redirectPercent);
        return installsCount != 0 && installsCount % additionalUrlCoefficient == 0;
    }
}
