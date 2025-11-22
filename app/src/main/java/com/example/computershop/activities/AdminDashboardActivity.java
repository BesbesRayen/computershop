package com.example.computershop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computershop.R;
import com.example.computershop.adapters.ProductAdapter;
import com.example.computershop.models.Product;
import com.example.computershop.utils.FirebaseManager;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {
    private Button addProductBtn;
    private Button manageUsersBtn;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        addProductBtn = findViewById(R.id.addProductBtn);
        manageUsersBtn = findViewById(R.id.manageUsersBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        addProductBtn.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, AdminProductsActivity.class));
        });

        manageUsersBtn.setOnClickListener(v -> startActivity(new Intent(AdminDashboardActivity.this, AdminUsersActivity.class)));

        logoutBtn.setOnClickListener(v -> {
            FirebaseManager.logoutUser();
            startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
            finish();
        });
    }
}
