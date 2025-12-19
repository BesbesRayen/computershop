package com.example.computershop.utils;

import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseManager {
    private static final String TAG = "FirebaseManager";
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore db;
    private static FirebaseStorage storage;

    // Collection names - FIXED
    private static final String COLLECTION_USERS = "Internaute";
    private static final String COLLECTION_PRODUCTS = "Article";
    private static final String COLLECTION_CART = "Panier";

    // Add FirebaseCallback interface
    public interface FirebaseCallback {
        void onSuccess(Object result);
        void onFailure(String error);
    }

    public static void init() {
        try {
            if (mAuth == null) mAuth = FirebaseAuth.getInstance();
            if (db == null) db = FirebaseFirestore.getInstance();
            if (storage == null) storage = FirebaseStorage.getInstance();
            Log.d(TAG, "FirebaseManager init OK");
        } catch (Exception e) {
            Log.e(TAG, "init error: " + e.getMessage());
        }
    }

    public static FirebaseAuth getAuth() {
        if (mAuth == null) init();
        return mAuth;
    }

    public static FirebaseFirestore getDatabase() {
        if (db == null) init();
        return db;
    }

    public static FirebaseFirestore getFirestore() {
        return getDatabase();
    }



    public static FirebaseUser getCurrentUser() {
        return getAuth().getCurrentUser();
    }

    public static String getCurrentUserId() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    public static boolean isCurrentUserAdmin() {
        FirebaseUser user = getCurrentUser();
        return user != null && isAdminEmail(user.getEmail());
    }

    public static boolean isAdminEmail(String email) {
        if (email == null) return false;
        return "admin@gmail.com".equals(email.toLowerCase());
    }

    public static void registerUser(String email, String password, String login, String pays, OnCompleteListener<AuthResult> listener) {
        try {
            getAuth().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = getAuth().getCurrentUser();
                            if (firebaseUser != null) {
                                User user = new User(firebaseUser.getUid(), login, email, System.currentTimeMillis(), pays, "user");
                                saveUserToFirestore(user, task, listener);
                            } else {
                                listener.onComplete(task);
                            }
                        } else {
                            listener.onComplete(task);
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "registerUser exception: " + e.getMessage());
        }
    }

    public static void loginUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        getAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public static void logoutUser() {
        if (mAuth != null) mAuth.signOut();
    }

    private static void saveUserToFirestore(User user, Task<AuthResult> authTask, OnCompleteListener<AuthResult> listener) {
        if (db == null) init();
        // FIXED: Use correct collection name "Internaute"
        db.collection(COLLECTION_USERS).document(user.getIdInt())
                .set(user)
                .addOnSuccessListener(aVoid -> listener.onComplete(authTask))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "saveUserToFirestore failed: " + e.getMessage());
                    listener.onComplete(authTask);
                });
    }

    public static void getUserFromFirestore(String userId, OnCompleteListener<DocumentSnapshot> listener) {
        if (db == null) init();
        // FIXED: Use correct collection name "Internaute"
        db.collection(COLLECTION_USERS).document(userId).get().addOnCompleteListener(listener);
    }

    // Products - FIXED collection names
    public static void saveProduct(com.example.computershop.models.Product product, OnCompleteListener<Void> listener) {
        if (db == null) init();

        // If product has no ID, create a new one
        if (product.getIdArt() == null || product.getIdArt().isEmpty()) {
            product.setIdArt(db.collection("Article").document().getId());
        }

        // This will UPDATE if the document exists, or CREATE if it doesn't
        db.collection("Article").document(product.getIdArt()).set(product).addOnCompleteListener(listener);
    }
    public static void deleteProduct(String productId, OnCompleteListener<Void> listener) {
        if (db == null) init();
        db.collection(COLLECTION_PRODUCTS).document(productId).delete().addOnCompleteListener(listener);
    }

    public static void getProducts(OnCompleteListener<QuerySnapshot> listener) {
        if (db == null) init();
        db.collection(COLLECTION_PRODUCTS).get().addOnCompleteListener(listener);
    }

    public static void getProducts(FirebaseCallback callback) {
        if (db == null) init();
        db.collection(COLLECTION_PRODUCTS).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(task.getResult());
                    } else {
                        callback.onFailure(task.getException() != null ? task.getException().getMessage() : "Unknown error");
                    }
                });
    }

    public static void getProductById(String productId, OnCompleteListener<DocumentSnapshot> listener) {
        if (db == null) init();
        db.collection(COLLECTION_PRODUCTS).document(productId).get().addOnCompleteListener(listener);
    }

    // Cart - FIXED collection names
    public static void addToCart(com.example.computershop.models.CartItem cartItem, OnCompleteListener<Void> listener) {
        if (db == null) init();
        if (cartItem.getNumPanier() == null || cartItem.getNumPanier().isEmpty()) {
            cartItem.setNumPanier(db.collection(COLLECTION_CART).document().getId());
        }
        db.collection(COLLECTION_CART).document(cartItem.getNumPanier()).set(cartItem).addOnCompleteListener(listener);
    }

    public static void addToCart(com.example.computershop.models.CartItem cartItem, FirebaseCallback callback) {
        if (db == null) init();
        if (cartItem.getNumPanier() == null || cartItem.getNumPanier().isEmpty()) {
            cartItem.setNumPanier(db.collection(COLLECTION_CART).document().getId());
        }
        db.collection(COLLECTION_CART).document(cartItem.getNumPanier()).set(cartItem)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        callback.onFailure(task.getException() != null ? task.getException().getMessage() : "Unknown error");
                    }
                });
    }

    public static void updateCartItem(com.example.computershop.models.CartItem cartItem, OnCompleteListener<Void> listener) {
        if (db == null) init();
        db.collection(COLLECTION_CART).document(cartItem.getNumPanier()).set(cartItem).addOnCompleteListener(listener);
    }

    public static void updateCartItem(com.example.computershop.models.CartItem cartItem, FirebaseCallback callback) {
        if (db == null) init();
        db.collection(COLLECTION_CART).document(cartItem.getNumPanier()).set(cartItem)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        callback.onFailure(task.getException() != null ? task.getException().getMessage() : "Unknown error");
                    }
                });
    }

    public static void getCartItems(String userId, OnCompleteListener<QuerySnapshot> listener) {
        if (db == null) init();
        db.collection(COLLECTION_CART).whereEqualTo("idInt", userId).get().addOnCompleteListener(listener);
    }

    public static void getCartItems(String userId, FirebaseCallback callback) {
        if (db == null) init();
        db.collection(COLLECTION_CART).whereEqualTo("idInt", userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<com.example.computershop.models.CartItem> cartItems = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            com.example.computershop.models.CartItem cartItem = document.toObject(com.example.computershop.models.CartItem.class);
                            if (cartItem != null) {
                                cartItem.setNumPanier(document.getId());
                                cartItems.add(cartItem);
                            }
                        }
                        callback.onSuccess(cartItems);
                    } else {
                        callback.onFailure(task.getException() != null ? task.getException().getMessage() : "Unknown error");
                    }
                });
    }

    public static void removeFromCart(String cartItemId, OnCompleteListener<Void> listener) {
        if (db == null) init();
        db.collection(COLLECTION_CART).document(cartItemId).delete().addOnCompleteListener(listener);
    }

    public static void removeFromCart(String cartItemId, FirebaseCallback callback) {
        if (db == null) init();
        db.collection(COLLECTION_CART).document(cartItemId).delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        callback.onFailure(task.getException() != null ? task.getException().getMessage() : "Unknown error");
                    }
                });
    }

    // Users admin - FIXED: Using correct collection name "Internaute"
    public static void getAllUsers(OnCompleteListener<QuerySnapshot> listener) {
        if (db == null) init();
        Log.d("FIRESTORE_QUERY", "Querying collection: " + COLLECTION_USERS);
        db.collection(COLLECTION_USERS).get().addOnCompleteListener(listener);
    }

    // Modify the existing updateUser method to make email optional
    public static void updateUser(String userId, String email, String role, String country, OnCompleteListener<Void> listener) {
        Map<String, Object> updates = new HashMap<>();

        // Only add email to updates if it's not null and not empty
        if (email != null && !email.isEmpty()) {
            updates.put("email", email);
        }

        updates.put("role", role);
        updates.put("pays", country);

        // FIXED: Use correct collection name "Internaute"
        db.collection(COLLECTION_USERS).document(userId)
                .update(updates)
                .addOnCompleteListener(listener);
    }
    public static void deleteUser(String userId, OnCompleteListener<Void> listener) {
        Log.d("FIREBASE_DELETE", "Deleting user with ID: " + userId + " from " + COLLECTION_USERS);

        if (userId == null || userId.isEmpty()) {
            Log.e("FIREBASE_DELETE", "User ID is null or empty");
            return;
        }

        // FIXED: Delete from correct collection "Internaute"
        db.collection(COLLECTION_USERS).document(userId)
                .delete()
                .addOnCompleteListener(listener)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FIREBASE_DELETE", "Successfully deleted user: " + userId);
                })
                .addOnFailureListener(e -> {
                    Log.e("FIREBASE_DELETE", "Error deleting user: " + e.getMessage());
                });
    }

    // Debug method to check all user data
    public static void debugAllUserData() {
        Log.d("DEBUG_ALL_DATA", "=== DEBUGGING ALL USER DATA ===");

        // Check the correct collection
        db.collection(COLLECTION_USERS).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("DEBUG_ALL_DATA", "Firestore '" + COLLECTION_USERS + "' collection count: " + task.getResult().size());
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    Log.d("DEBUG_ALL_DATA", "User: " + doc.getId() + " - " + doc.getData());
                }
            } else {
                Log.e("DEBUG_ALL_DATA", "Error reading Firestore: " + task.getException());
            }
        });
    }
    // Add these methods to your FirebaseManager class

    public static StorageReference getStorageReference() {
        if (storage == null) {
            try {
                storage = FirebaseStorage.getInstance();
                // Set a longer timeout for uploads
                storage.setMaxUploadRetryTimeMillis(30000);
                storage.setMaxOperationRetryTimeMillis(30000);
            } catch (Exception e) {
                Log.e(TAG, "Storage init error: " + e.getMessage());
            }
        }
        return storage.getReference();
    }

    // Add a dedicated method for uploading product images
    public static void uploadProductImage(Uri imageUri, StringCallback callback) {
        try {
            if (imageUri == null) {
                callback.onFailure("No image selected");
                return;
            }

            StorageReference storageRef = getStorageReference();
            String fileName = "products/product_" + System.currentTimeMillis() + ".jpg";
            StorageReference fileRef = storageRef.child(fileName);

            Log.d(TAG, "Starting upload to: " + fileName);

            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Log.d(TAG, "Image upload successful");
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            Log.d(TAG, "Download URL obtained: " + downloadUrl);
                            callback.onSuccess(downloadUrl);
                        }).addOnFailureListener(e -> {
                            Log.e(TAG, "Failed to get download URL: " + e.getMessage());
                            callback.onFailure("Failed to get download URL: " + e.getMessage());
                        });
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Upload failed: " + e.getMessage());
                        callback.onFailure("Upload failed: " + e.getMessage());
                    })
                    .addOnProgressListener(snapshot -> {
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        Log.d(TAG, "Upload progress: " + progress + "%");
                    });

        } catch (Exception e) {
            Log.e(TAG, "Upload exception: " + e.getMessage());
            callback.onFailure("Upload exception: " + e.getMessage());
        }
    }

    // Add this callback interface for image uploads
    public interface StringCallback {
        void onSuccess(String result);
        void onFailure(String error);
    }
}