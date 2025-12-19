package com.example.computershop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.computershop.R;
import com.example.computershop.utils.FirebaseManager;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class AdminPageActivity extends AppCompatActivity {
    private TextView adminWelcomeTv;
    private TextView adminEmailTv;
    private TextView productsCountTv;
    private TextView usersCountTv;
    private Button manageProductsBtn;
    private Button manageUsersBtn;
    private Button settingsBtn;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        initializeViews();
        setupClickListeners();
        loadStats();
    }

    private void initializeViews() {
        adminWelcomeTv = findViewById(R.id.adminWelcomeTv);
        adminEmailTv = findViewById(R.id.adminEmailTv);
        productsCountTv = findViewById(R.id.productsCountTv);
        usersCountTv = findViewById(R.id.usersCountTv);
        manageProductsBtn = findViewById(R.id.manageProductsBtn);
        manageUsersBtn = findViewById(R.id.manageUsersBtn);
        settingsBtn = findViewById(R.id.settingsBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        // Set admin email
        FirebaseUser currentUser = FirebaseManager.getCurrentUser();
        if (currentUser != null) {
            adminEmailTv.setText("Admin: " + currentUser.getEmail());
        }
    }

    private void setupClickListeners() {
        manageProductsBtn.setOnClickListener(v -> {
            startActivity(new Intent(AdminPageActivity.this, AdminProductsActivity.class));
        });

        manageUsersBtn.setOnClickListener(v -> {
            startActivity(new Intent(AdminPageActivity.this, AdminUsersActivity.class));
        });

        settingsBtn.setOnClickListener(v -> {
            showChangePasswordDialog();
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseManager.logoutUser();
            Toast.makeText(AdminPageActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminPageActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void loadStats() {
        // Load products count
        FirebaseManager.getProducts(task -> {
            if (task.isSuccessful()) {
                int productsCount = task.getResult().size();
                productsCountTv.setText(String.valueOf(productsCount));
            } else {
                productsCountTv.setText("0");
            }
        });

        // Load users count
        FirebaseManager.getAllUsers(task -> {
            if (task.isSuccessful()) {
                int validUsersCount = 0;

                for (com.google.firebase.firestore.DocumentSnapshot doc : task.getResult().getDocuments()) {
                    Map<String, Object> data = doc.getData();
                    if (data != null) {
                        Object emailObj = data.get("email");
                        String email = emailObj != null ? emailObj.toString().trim() : "";

                        if (!email.isEmpty()) {
                            validUsersCount++;
                        }
                    }
                }

                usersCountTv.setText(String.valueOf(validUsersCount));
                Log.d("QUICK_STATS", "Displaying users count: " + validUsersCount);
            } else {
                usersCountTv.setText("0");
                Log.e("QUICK_STATS", "Failed to load users: " + task.getException());
            }
        });
    }

    private void showChangePasswordDialog() {
        FirebaseUser user = FirebaseManager.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // First, ask for current password to re-authenticate
        AlertDialog.Builder authBuilder = new AlertDialog.Builder(AdminPageActivity.this);
        authBuilder.setTitle("Confirm Current Password");

        LinearLayout authLayout = new LinearLayout(AdminPageActivity.this);
        authLayout.setOrientation(LinearLayout.VERTICAL);
        authLayout.setPadding(50, 40, 50, 10);

        EditText currentPasswordInput = new EditText(AdminPageActivity.this);
        currentPasswordInput.setHint("Enter your current password");
        currentPasswordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        currentPasswordInput.setPadding(10, 10, 10, 10);
        authLayout.addView(currentPasswordInput);

        authBuilder.setView(authLayout);
        authBuilder.setPositiveButton("Continue", (dialog, which) -> {
            String currentPassword = currentPasswordInput.getText().toString().trim();
            if (currentPassword.isEmpty()) {
                Toast.makeText(AdminPageActivity.this, "Please enter your current password", Toast.LENGTH_SHORT).show();
                return;
            }
            reauthenticateAndChangeCredentials(user, currentPassword);
        });
        authBuilder.setNegativeButton("Cancel", null);
        authBuilder.show();
    }

    //change password and email
    private void reauthenticateAndChangeCredentials(FirebaseUser user, String currentPassword) {
        String userEmail = user.getEmail();
        if (userEmail == null) {
            Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show();
            return;
        }

        user.reauthenticate(EmailAuthProvider.getCredential(userEmail, currentPassword))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showUpdateCredentialsDialog(user);
                    } else {
                        Toast.makeText(AdminPageActivity.this, "Authentication failed: " +
                                (task.getException() != null ? task.getException().getMessage() : "Wrong password"), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showUpdateCredentialsDialog(FirebaseUser user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPageActivity.this);
        builder.setTitle("Change Email/Password");

        LinearLayout layout = new LinearLayout(AdminPageActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        EditText emailInput = new EditText(AdminPageActivity.this);
        emailInput.setHint("New Email (leave empty to keep current)");
        emailInput.setText(user.getEmail());
        emailInput.setPadding(10, 10, 10, 10);
        layout.addView(emailInput);

        EditText passwordInput = new EditText(AdminPageActivity.this);
        passwordInput.setHint("New Password (leave empty to keep current)");
        passwordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordInput.setPadding(10, 20, 10, 10);
        layout.addView(passwordInput);

        builder.setView(layout);
        builder.setPositiveButton("Update", (dialog, which) -> {
            String newEmail = emailInput.getText().toString().trim();
            String newPassword = passwordInput.getText().toString().trim();

            if (newEmail.isEmpty() && newPassword.isEmpty()) {
                Toast.makeText(AdminPageActivity.this, "Please enter at least one field", Toast.LENGTH_SHORT).show();
                return;
            }

            updateCredentials(user, newEmail, newPassword);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    private void updateCredentials(FirebaseUser user, String newEmail, String newPassword) {
        boolean updateEmail = !newEmail.isEmpty() && !newEmail.equals(user.getEmail());
        boolean updatePassword = !newPassword.isEmpty();

        if (updateEmail && updatePassword) {
            // Update both
            user.updateEmail(newEmail).addOnCompleteListener(emailTask -> {
                if (emailTask.isSuccessful()) {
                    user.updatePassword(newPassword).addOnCompleteListener(passwordTask -> {
                        if (passwordTask.isSuccessful()) {
                            Toast.makeText(AdminPageActivity.this, "Email and password updated successfully!", Toast.LENGTH_SHORT).show();
                            adminEmailTv.setText("Admin: " + newEmail);
                        } else {
                            Toast.makeText(AdminPageActivity.this, "Password update failed: " +
                                    (passwordTask.getException() != null ? passwordTask.getException().getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(AdminPageActivity.this, "Email update failed: " +
                            (emailTask.getException() != null ? emailTask.getException().getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (updateEmail) {
            // Update only email
            user.updateEmail(newEmail).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminPageActivity.this, "Email updated successfully!", Toast.LENGTH_SHORT).show();
                    adminEmailTv.setText("Admin: " + newEmail);
                } else {
                    Toast.makeText(AdminPageActivity.this, "Email update failed: " +
                            (task.getException() != null ? task.getException().getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (updatePassword) {
            // Update only password
            user.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminPageActivity.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminPageActivity.this, "Password update failed: " +
                            (task.getException() != null ? task.getException().getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
                } 
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh stats when returning to this activity
        Log.d("ADMIN_PAGE", "Refreshing quick stats...");
        loadStats();
    }

}