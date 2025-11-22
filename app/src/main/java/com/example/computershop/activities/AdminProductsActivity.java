package com.example.computershop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computershop.R;
import com.example.computershop.adapters.AdminProductAdapter;
import com.example.computershop.models.Product;
import com.example.computershop.utils.FirebaseManager;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminProductsActivity extends AppCompatActivity {
    private RecyclerView productsRecyclerView;
    private AdminProductAdapter productAdapter;
    private List<Product> productList;
    private Button addProductBtn;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_products);

        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        addProductBtn = findViewById(R.id.addProductBtn);
        backBtn = findViewById(R.id.backBtn);

        productList = new ArrayList<>();
        productAdapter = new AdminProductAdapter(productList, product -> {
            showEditProductDialog(product);
        }, product -> {
            deleteProduct(product.getIdArt());
        });

        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsRecyclerView.setAdapter(productAdapter);

        loadProducts();

        addProductBtn.setOnClickListener(v -> showAddProductDialog());
        backBtn.setOnClickListener(v -> finish());
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
            }
        });
    }

    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Product");

        // Create a custom layout for the dialog
        EditText nameInput = new EditText(this);
        nameInput.setHint("Product Name");

        EditText priceInput = new EditText(this);
        priceInput.setHint("Price");
        priceInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

        EditText categoryInput = new EditText(this);
        categoryInput.setHint("Category");

        EditText descriptionInput = new EditText(this);
        descriptionInput.setHint("Description");

        EditText stockInput = new EditText(this);
        stockInput.setHint("Stock");
        stockInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        EditText imageUrlInput = new EditText(this);
        imageUrlInput.setHint("Image URL");

        // Create a vertical layout for inputs
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);
        layout.addView(nameInput);
        layout.addView(priceInput);
        layout.addView(categoryInput);
        layout.addView(descriptionInput);
        layout.addView(stockInput);
        layout.addView(imageUrlInput);

        builder.setView(layout);
        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String priceStr = priceInput.getText().toString().trim();
            String category = categoryInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            String stockStr = stockInput.getText().toString().trim();
            String imageUrl = imageUrlInput.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty() || category.isEmpty() || stockStr.isEmpty()) {
                Toast.makeText(AdminProductsActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            Product product = new Product(
                    null,
                    name,
                    Double.parseDouble(priceStr),
                    category,
                    description,
                    Integer.parseInt(stockStr),
                    imageUrl
            );

            FirebaseManager.saveProduct(product, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminProductsActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                    loadProducts();
                } else {
                    Toast.makeText(AdminProductsActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showEditProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Product");

        EditText nameInput = new EditText(this);
        nameInput.setText(product.getLibArt());

        EditText priceInput = new EditText(this);
        priceInput.setText(String.valueOf(product.getPrixArt()));
        priceInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

        EditText categoryInput = new EditText(this);
        categoryInput.setText(product.getCatArt());

        EditText descriptionInput = new EditText(this);
        descriptionInput.setText(product.getDescription());

        EditText stockInput = new EditText(this);
        stockInput.setText(String.valueOf(product.getStock()));
        stockInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        EditText imageUrlInput = new EditText(this);
        imageUrlInput.setText(product.getImageUrl());

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);
        layout.addView(nameInput);
        layout.addView(priceInput);
        layout.addView(categoryInput);
        layout.addView(descriptionInput);
        layout.addView(stockInput);
        layout.addView(imageUrlInput);

        builder.setView(layout);
        builder.setPositiveButton("Update", (dialog, which) -> {
            product.setLibArt(nameInput.getText().toString().trim());
            product.setPrixArt(Double.parseDouble(priceInput.getText().toString().trim()));
            product.setCatArt(categoryInput.getText().toString().trim());
            product.setDescription(descriptionInput.getText().toString().trim());
            product.setStock(Integer.parseInt(stockInput.getText().toString().trim()));
            product.setImageUrl(imageUrlInput.getText().toString().trim());

            FirebaseManager.saveProduct(product, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminProductsActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    loadProducts();
                } else {
                    Toast.makeText(AdminProductsActivity.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteProduct(String productId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseManager.deleteProduct(productId, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminProductsActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
                            loadProducts();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }
}
