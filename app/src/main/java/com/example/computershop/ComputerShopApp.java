package com.example.computershop;

import android.app.Application;
import android.util.Log;

import com.example.computershop.utils.FirebaseManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ComputerShopApp extends Application {
    private static final String TAG = "ComputerShopApp";

    @Override
    public void onCreate() {
        super.onCreate();
        
        try {
            Log.d(TAG, "========== App Initialization Started ==========");
            
            // Initialize Firebase
            if (FirebaseApp.getApps(this).isEmpty()) {
                Log.d(TAG, "Initializing FirebaseApp...");
                FirebaseApp.initializeApp(this);
                Log.d(TAG, "FirebaseApp initialized successfully");
            } else {
                Log.d(TAG, "FirebaseApp already initialized");
            }
            
            // Initialize FirebaseAuth
            try {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                Log.d(TAG, "FirebaseAuth initialized: " + (auth != null ? "SUCCESS" : "FAILED"));
            } catch (Exception e) {
                Log.e(TAG, "FirebaseAuth initialization error: " + e.getMessage());
            }
            
            // Initialize Firestore
            try {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Log.d(TAG, "Firestore initialized: " + (db != null ? "SUCCESS" : "FAILED"));
            } catch (Exception e) {
                Log.e(TAG, "Firestore initialization error: " + e.getMessage());
            }
            
            // Initialize FirebaseManager
            FirebaseManager.init();
            Log.d(TAG, "FirebaseManager initialized successfully");
            
            Log.d(TAG, "========== App Initialization Completed ==========");
        } catch (Exception e) {
            Log.e(TAG, "CRITICAL ERROR during app initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
