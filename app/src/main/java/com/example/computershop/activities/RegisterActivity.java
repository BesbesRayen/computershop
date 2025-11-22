package com.example.computershop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.computershop.R;
import com.example.computershop.utils.FirebaseManager;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private EditText loginInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Spinner countrySpinner;
    private Button registerBtn;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.d(TAG, "RegisterActivity created");

        // Initialize Firebase
        FirebaseManager.init();
        
        // Check if Firebase is properly initialized
        FirebaseAuth auth = FirebaseManager.getAuth();
        if (auth == null) {
            Log.e(TAG, "CRITICAL: FirebaseAuth is NULL!");
            Toast.makeText(this, "Firebase configuration error. Please restart the app.", Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "FirebaseAuth initialized successfully");
        }

        loginInput = findViewById(R.id.loginInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        countrySpinner = findViewById(R.id.countrySpinner);
        registerBtn = findViewById(R.id.registerBtn);
        loginLink = findViewById(R.id.loginLink);

        registerBtn.setOnClickListener(v -> handleRegister());
        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void handleRegister() {
        String login = loginInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();
        String country = countrySpinner.getSelectedItem().toString();

        if (login.isEmpty()) {
            loginInput.setError("Login is required");
            return;
        }

        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            return;
        }

        if (!isValidEmail(email)) {
            emailInput.setError("Invalid email format");
            return;
        }

        if (password.isEmpty()) {
            passwordInput.setError("Password is required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Passwords do not match");
            return;
        }

        if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            return;
        }

        registerBtn.setEnabled(false);

        FirebaseManager.registerUser(email, password, login, country, task -> {
            registerBtn.setEnabled(true);

            if (task.isSuccessful()) {
                Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            } else {
                String errorMsg = "Registration failed";
                if (task.getException() != null) {
                    String exceptionMsg = task.getException().getMessage();
                    if (exceptionMsg != null) {
                        if (exceptionMsg.contains("email address is already in use")) {
                            errorMsg = "This email is already registered";
                        } else if (exceptionMsg.contains("invalid email")) {
                            errorMsg = "Invalid email format";
                        } else if (exceptionMsg.contains("password")) {
                            errorMsg = "Password is too weak. Use at least 6 characters";
                        } else {
                            errorMsg = exceptionMsg;
                        }
                    }
                }
                Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
