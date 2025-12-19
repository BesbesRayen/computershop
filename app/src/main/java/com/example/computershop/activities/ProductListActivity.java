package com.example.computershop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computershop.R;
import com.example.computershop.adapters.ProductAdapter;
import com.example.computershop.models.CartItem;
import com.example.computershop.models.Product;
import com.example.computershop.utils.FirebaseManager;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private RecyclerView productsRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Product> productListFull;
    private SearchView searchView;
    private ImageButton cartBtn;
    private ImageButton settingsBtn;
    private ImageButton logoutBtn;
    private TextView cartBadge;
    private String currentUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // Get current user ID
        currentUserId = FirebaseManager.getCurrentUserId();

        // Initialize views
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        searchView = findViewById(R.id.searchView);
        cartBtn = findViewById(R.id.cartBtn);
        settingsBtn = findViewById(R.id.settingsBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        cartBadge = findViewById(R.id.cartBadge);

        // Initialize product lists
        productList = new ArrayList<>();
        productListFull = new ArrayList<>();

        // Initialize adapter with cart functionality
        productAdapter = new ProductAdapter(productList,
                new ProductAdapter.OnProductClickListener() {
                    @Override
                    public void onProductClick(Product product) {
                        // Navigate to product detail
                        Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                        intent.putExtra("product", product);
                        startActivity(intent);
                    }
                },
                currentUserId,
                new ProductAdapter.OnCartUpdateListener() {
                    @Override
                    public void onCartUpdated() {
                        updateCartBadge();
                    }
                });

        // Setup RecyclerView
        productsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productsRecyclerView.setAdapter(productAdapter);

        // Load products and cart data
        loadProductsFromFirestore();
        updateCartBadge();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterLocal(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterLocal(newText);
                return true;
            }
        });

        // Cart button click
        cartBtn.setOnClickListener(v -> {
            if (currentUserId != null) {
                startActivity(new Intent(ProductListActivity.this, CartActivity.class));
            } else {
                Toast.makeText(ProductListActivity.this, "Please login to view cart", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProductListActivity.this, LoginActivity.class));
            }
        });


        settingsBtn.setOnClickListener(v -> {
            showChangePasswordDialog();
        });


        logoutBtn.setOnClickListener(v -> {
            FirebaseManager.logoutUser();
            startActivity(new Intent(ProductListActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void loadProductsFromFirestore() {
        FirebaseManager.getProducts(new FirebaseManager.FirebaseCallback() {
            @Override
            public void onSuccess(Object result) {
                com.google.firebase.firestore.QuerySnapshot snapshot = (com.google.firebase.firestore.QuerySnapshot) result;
                productList.clear();
                productListFull.clear();

                if (snapshot != null && !snapshot.isEmpty()) {
                    for (com.google.firebase.firestore.DocumentSnapshot doc : snapshot.getDocuments()) {
                        Product product = doc.toObject(Product.class);
                        if (product != null) {
                            product.setIdArt(doc.getId());
                            productList.add(product);
                            productListFull.add(product);
                        }
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(ProductListActivity.this, "Failed to load products: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCartBadge() {
        if (currentUserId == null) {
            cartBadge.setVisibility(TextView.GONE);
            return;
        }

        FirebaseManager.getCartItems(currentUserId, new FirebaseManager.FirebaseCallback() {
            @Override
            public void onSuccess(Object result) {
                List<CartItem> cartItems = (List<CartItem>) result;
                runOnUiThread(() -> {
                    if (cartItems != null && !cartItems.isEmpty()) {
                        int totalItems = 0;
                        for (CartItem item : cartItems) {
                            totalItems += item.getQuantité();
                        }
                        cartBadge.setText(String.valueOf(totalItems));
                        cartBadge.setVisibility(TextView.VISIBLE);
                    } else {
                        cartBadge.setVisibility(TextView.GONE);
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() -> {
                    cartBadge.setVisibility(TextView.GONE);
                });
            }
        });
    }

    private void filterLocal(String query) {
        if (query == null) query = "";
        String q = query.trim().toLowerCase();
        productList.clear();

        if (q.isEmpty()) {
            productList.addAll(productListFull);
        } else {
            for (Product p : productListFull) {
                boolean matchName = p.getLibArt() != null && p.getLibArt().toLowerCase().contains(q);
                boolean matchCat = p.getCatArt() != null && p.getCatArt().toLowerCase().contains(q);
                boolean matchPrice = String.valueOf(p.getPrixArt()).contains(q);
                if (matchName || matchCat || matchPrice) {
                    productList.add(p);
                }
            }
        }
        productAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateCartBadge();
    }


    private void showChangePasswordDialog() {
        FirebaseUser user = FirebaseManager.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // First, ask for current password
        AlertDialog.Builder authBuilder = new AlertDialog.Builder(ProductListActivity.this);
        authBuilder.setTitle("Confirm Current Password");

        LinearLayout authLayout = new LinearLayout(ProductListActivity.this);
        authLayout.setOrientation(LinearLayout.VERTICAL);
        authLayout.setPadding(50, 40, 50, 10);

        EditText currentPasswordInput = new EditText(ProductListActivity.this);
        currentPasswordInput.setHint("Enter your current password");
        currentPasswordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        currentPasswordInput.setPadding(10, 10, 10, 10);
        authLayout.addView(currentPasswordInput);

        authBuilder.setView(authLayout);
        authBuilder.setPositiveButton("Continue", (dialog, which) -> {
            String currentPassword = currentPasswordInput.getText().toString().trim();
            if (currentPassword.isEmpty()) {
                Toast.makeText(ProductListActivity.this, "Please enter your current password", Toast.LENGTH_SHORT).show();
                return;
            }
            reauthenticateAndChangeCredentials(user, currentPassword);
        });
        authBuilder.setNegativeButton("Cancel", null);
        authBuilder.show();
    }


    private void reauthenticateAndChangeCredentials(FirebaseUser user, String currentPassword) {
        String userEmail = user.getEmail();
        if (userEmail == null) {
            Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Re-authenticate user
        user.reauthenticate(EmailAuthProvider.getCredential(userEmail, currentPassword))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Password is correct, now show change dialog
                        showUpdateCredentialsDialog(user);
                    } else {
                        Toast.makeText(ProductListActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showUpdateCredentialsDialog(FirebaseUser user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
        builder.setTitle("Change Email/Password");

        LinearLayout layout = new LinearLayout(ProductListActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        EditText emailInput = new EditText(ProductListActivity.this);
        emailInput.setHint("New Email (leave empty to keep current)");
        emailInput.setText(user.getEmail());
        emailInput.setPadding(10, 10, 10, 10);
        layout.addView(emailInput);

        EditText passwordInput = new EditText(ProductListActivity.this);
        passwordInput.setHint("New Password (leave empty to keep current)");
        passwordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordInput.setPadding(10, 20, 10, 10);
        layout.addView(passwordInput);

        builder.setView(layout);
        builder.setPositiveButton("Update", (dialog, which) -> {
            String newEmail = emailInput.getText().toString().trim();
            String newPassword = passwordInput.getText().toString().trim();

            if (newEmail.isEmpty() && newPassword.isEmpty()) {
                Toast.makeText(ProductListActivity.this, "Please enter at least one field", Toast.LENGTH_SHORT).show();
                return;
            }

            updateCredentials(user, newEmail, newPassword);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    /**
     * Update user email and/or password
     */
    private void updateCredentials(FirebaseUser user, String newEmail, String newPassword) {
        boolean updateEmail = !newEmail.isEmpty() && !newEmail.equals(user.getEmail());
        boolean updatePassword = !newPassword.isEmpty();

        if (updateEmail && updatePassword) {
            // Update both
            user.updateEmail(newEmail).addOnCompleteListener(emailTask -> {
                if (emailTask.isSuccessful()) {
                    user.updatePassword(newPassword).addOnCompleteListener(passwordTask -> {
                        if (passwordTask.isSuccessful()) {
                            Toast.makeText(ProductListActivity.this, "Email and password updated successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProductListActivity.this, "Password update failed: " + passwordTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ProductListActivity.this, "Email update failed: " + emailTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (updateEmail) {
            // Update only email
            user.updateEmail(newEmail).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ProductListActivity.this, "Email updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductListActivity.this, "Email update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (updatePassword) {
            // Update only password
            user.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ProductListActivity.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductListActivity.this, "Password update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}