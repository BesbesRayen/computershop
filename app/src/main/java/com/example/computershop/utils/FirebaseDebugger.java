package com.example.computershop.utils;

import android.util.Log;

public class FirebaseDebugger {
    private static final String TAG = "FirebaseDebugger";

    public static void checkFirebaseConfig() {
        try {
            Log.d(TAG, "=== Firebase Configuration Check ===");
            
            // Check if FirebaseAuth is available
            try {
                com.google.firebase.auth.FirebaseAuth auth = FirebaseManager.getAuth();
                if (auth != null) {
                    Log.d(TAG, "✓ FirebaseAuth initialized successfully");
                } else {
                    Log.e(TAG, "✗ FirebaseAuth is NULL");
                }
            } catch (Exception e) {
                Log.e(TAG, "✗ FirebaseAuth error: " + e.getMessage());
            }

            // Check if Firestore is available
            try {
                com.google.firebase.firestore.FirebaseFirestore db = FirebaseManager.getDatabase();
                if (db != null) {
                    Log.d(TAG, "✓ Firestore initialized successfully");
                } else {
                    Log.e(TAG, "✗ Firestore is NULL");
                }
            } catch (Exception e) {
                Log.e(TAG, "✗ Firestore error: " + e.getMessage());
            }

            Log.d(TAG, "=== End Configuration Check ===");
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error during config check: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
