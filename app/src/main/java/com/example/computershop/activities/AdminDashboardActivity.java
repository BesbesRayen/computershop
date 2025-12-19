package com.example.computershop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.computershop.R;
import com.example.computershop.utils.FirebaseManager;

public class AdminDashboardActivity extends AppCompatActivity {
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        logoutBtn = findViewById(R.id.logoutBtn);

       

        logoutBtn.setOnClickListener(v -> {
            FirebaseManager.logoutUser();
            Toast.makeText(AdminDashboardActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
            finish();
        });
    }
}