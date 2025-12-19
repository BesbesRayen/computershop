package com.example.computershop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.computershop.R;
import com.example.computershop.utils.FirebaseManager;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginBtn;
    private TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseManager.init();

        // Check if user is already logged in
        FirebaseUser currentUser = FirebaseManager.getCurrentUser();
        if (currentUser != null) {
            navigateToHome(currentUser.getUid());
            return;
        }

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        registerLink = findViewById(R.id.registerLink);

        loginBtn.setOnClickListener(v -> handleLogin());
        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void handleLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            return;
        }

        if (password.isEmpty()) {
            passwordInput.setError("Password is required");
            return;
        }

        loginBtn.setEnabled(false);

        FirebaseManager.loginUser(email, password, task -> {
            loginBtn.setEnabled(true);

            if (task.isSuccessful()) {
                FirebaseUser user = FirebaseManager.getCurrentUser();
                if (user != null) {
                    navigateToHome(user.getUid());
                }
            } else {
                Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToHome(String userId) {
        FirebaseUser currentUser = FirebaseManager.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(LoginActivity.this, ProductListActivity.class));
            finish();
            return;
        }

        String userEmail = currentUser.getEmail();

        // Check if admin email first
        if (isAdminEmail(userEmail)) {
            Toast.makeText(LoginActivity.this, "Welcome Admin!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, AdminPageActivity.class));
            finish();
            return;
        }

        // For regular users, check role from Firestore
        FirebaseManager.getUserFromFirestore(userId, task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String role = document.getString("role");
                    if ("admin".equals(role)) {
                        Toast.makeText(LoginActivity.this, "Welcome Admin!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, AdminPageActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, ProductListActivity.class));
                    }
                } else {
                    // If user not found in Firestore, go to product list
                    startActivity(new Intent(LoginActivity.this, ProductListActivity.class));
                }
            } else {
                // If error fetching user data, go to product list
                startActivity(new Intent(LoginActivity.this, ProductListActivity.class));
            }
            finish();
        });
    }

    private boolean isAdminEmail(String email) {
        if (email == null) return false;
        return "admin@gmail.com".equals(email.toLowerCase());
    }
}
