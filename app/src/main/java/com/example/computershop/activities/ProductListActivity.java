package com.example.computershop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;
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

public class ProductListActivity extends AppCompatActivity {
    private RecyclerView productsRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private SearchView searchView;
    private Button cartBtn;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        searchView = findViewById(R.id.searchView);
        cartBtn = findViewById(R.id.cartBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, product -> {
            Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });

        productsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productsRecyclerView.setAdapter(productAdapter);

        loadProducts();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProducts(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return false;
            }
        });

        cartBtn.setOnClickListener(v -> {
            startActivity(new Intent(ProductListActivity.this, CartActivity.class));
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseManager.logoutUser();
            startActivity(new Intent(ProductListActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void loadProducts() {
        FirebaseManager.getProducts(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                productList.clear();
                if (snapshot != null) {
                    for (com.google.firebase.firestore.DocumentSnapshot doc : snapshot.getDocuments()) {
                        Product product = doc.toObject(Product.class);
                        productList.add(product);
                    }
                }
                productAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(ProductListActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterProducts(String query) {
        FirebaseManager.getProducts(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                productList.clear();
                if (snapshot != null) {
                    for (com.google.firebase.firestore.DocumentSnapshot doc : snapshot.getDocuments()) {
                        Product product = doc.toObject(Product.class);
                        if (product != null && (
                                product.getLibArt().toLowerCase().contains(query.toLowerCase()) ||
                                product.getCatArt().toLowerCase().contains(query.toLowerCase()) ||
                                String.valueOf(product.getPrixArt()).contains(query)
                        )) {
                            productList.add(product);
                        }
                    }
                }
                productAdapter.notifyDataSetChanged();
            }
        });
    }
}
