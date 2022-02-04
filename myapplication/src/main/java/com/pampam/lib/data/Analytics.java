package com.pampam.lib.data;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;

public class Analytics {

    public static void sendAppsflyerData(String url) {
        HashMap<String, String> data = new HashMap<>();
        data.put("url", url);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("af_deeplinks")
            .add(data)
            .addOnCompleteListener(task -> {})
            .addOnFailureListener(e -> {});
    }

    public static void sendIDFAData(String url) {
        HashMap<String, String> data = new HashMap<>();
        data.put("url", url);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("idfa_deeplinks")
            .add(data)
            .addOnCompleteListener(task -> {})
            .addOnFailureListener(e -> {});
    }
}
