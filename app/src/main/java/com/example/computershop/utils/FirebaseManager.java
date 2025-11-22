package com.example.computershop.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.computershop.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseManager {
    private static final String TAG = "FirebaseManager";
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore db;

    public static void init() {
        try {
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            
            Log.d(TAG, "Firebase and Firestore initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static FirebaseAuth getAuth() {
        if (mAuth == null) {
            init();
        }
        return mAuth;
    }

    public static FirebaseFirestore getDatabase() {
        if (db == null) {
            init();
        }
        return db;
    }

    // Authentication Methods
    public static void registerUser(String email, String password, String login, String pays, OnCompleteListener<AuthResult> listener) {
        try {
            Log.d(TAG, "Starting user registration for email: " + email);
            
            getAuth().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Auth registration successful");
                                FirebaseUser firebaseUser = task.getResult().getUser();
                                if (firebaseUser != null) {
                                    User user = new User(
                                            firebaseUser.getUid(),
                                            login,
                                            email,
                                            System.currentTimeMillis(),
                                            pays,
                                            "user"
                                    );
                                    saveUserToFirestore(user, task, listener);
                                } else {
                                    Log.e(TAG, "FirebaseUser is null after registration");
                                    listener.onComplete(task);
                                }
                            } else {
                                Log.e(TAG, "Registration failed: " + task.getException());
                                if (task.getException() != null) {
                                    String errorMsg = task.getException().getMessage();
                                    Log.e(TAG, "Error details: " + errorMsg);
                                    
                                    // Check for reCAPTCHA error
                                    if (errorMsg.contains("CONFIGURATION_NOT_FOUND") || errorMsg.contains("reCAPTCHA")) {
                                        Log.w(TAG, "reCAPTCHA configuration issue detected");
                                    }
                                }
                                listener.onComplete(task);
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "Exception during registration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void loginUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public static void logoutUser() {
        getAuth().signOut();
    }

    public static FirebaseUser getCurrentUser() {
        return getAuth().getCurrentUser();
    }

    // Firestore Methods
    private static void saveUserToFirestore(User user, Task<AuthResult> authTask, OnCompleteListener<AuthResult> listener) {
        try {
            if (db == null) {
                Log.e(TAG, "Firestore is not initialized!");
                listener.onComplete(authTask);
                return;
            }
            
            Log.d(TAG, "Attempting to save user to Firestore: " + user.getIdInt());
            
            getDatabase().collection("Internaute").document(user.getIdInt())
                    .set(user)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "User saved to Firestore successfully");
                        listener.onComplete(authTask);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to save user to Firestore: " + e.getMessage());
                        e.printStackTrace();
                        listener.onComplete(authTask);
                    });
        } catch (Exception e) {
            Log.e(TAG, "Exception in saveUserToFirestore: " + e.getMessage());
            e.printStackTrace();
            listener.onComplete(authTask);
        }
    }

    public static void getUserFromFirestore(String userId, OnCompleteListener<DocumentSnapshot> listener) {
        getDatabase().collection("Internaute").document(userId)
                .get()
                .addOnCompleteListener(listener);
    }

    public static void saveProduct(com.example.computershop.models.Product product, OnCompleteListener<Void> listener) {
        if (product.getIdArt() == null || product.getIdArt().isEmpty()) {
            product.setIdArt(getDatabase().collection("Article").document().getId());
        }
        getDatabase().collection("Article").document(product.getIdArt())
                .set(product)
                .addOnCompleteListener(listener);
    }

    public static void deleteProduct(String productId, OnCompleteListener<Void> listener) {
        getDatabase().collection("Article").document(productId)
                .delete()
                .addOnCompleteListener(listener);
    }

    public static void getProducts(OnCompleteListener<QuerySnapshot> listener) {
        getDatabase().collection("Article")
                .get()
                .addOnCompleteListener(listener);
    }

    public static void getProductById(String productId, OnCompleteListener<DocumentSnapshot> listener) {
        getDatabase().collection("Article").document(productId)
                .get()
                .addOnCompleteListener(listener);
    }

    public static void addToCart(com.example.computershop.models.CartItem cartItem, OnCompleteListener<Void> listener) {
        if (cartItem.getNumPanier() == null || cartItem.getNumPanier().isEmpty()) {
            cartItem.setNumPanier(getDatabase().collection("Panier").document().getId());
        }
        getDatabase().collection("Panier").document(cartItem.getNumPanier())
                .set(cartItem)
                .addOnCompleteListener(listener);
    }

    public static void getCartItems(String userId, OnCompleteListener<QuerySnapshot> listener) {
        getDatabase().collection("Panier")
                .whereEqualTo("idInt", userId)
                .get()
                .addOnCompleteListener(listener);
    }

    public static void removeFromCart(String cartItemId, OnCompleteListener<Void> listener) {
        getDatabase().collection("Panier").document(cartItemId)
                .delete()
                .addOnCompleteListener(listener);
    }

    public static void getAllUsers(OnCompleteListener<QuerySnapshot> listener) {
        getDatabase().collection("Internaute")
                .get()
                .addOnCompleteListener(listener);
    }

    public static void updateUser(User user, OnCompleteListener<Void> listener) {
        getDatabase().collection("Internaute").document(user.getIdInt())
                .set(user)
                .addOnCompleteListener(listener);
    }

    public static void deleteUser(String userId, OnCompleteListener<Void> listener) {
        getDatabase().collection("Internaute").document(userId)
                .delete()
                .addOnCompleteListener(listener);
    }
}
