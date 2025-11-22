package com.example.computershop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.computershop.R;
import com.example.computershop.models.Product;
import com.example.computershop.utils.FirebaseManager;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView productImage;
    private EditText productName;
    private EditText productPrice;
    private EditText productCategory;
    private EditText productDescription;
    private EditText productStock;
    private Spinner packagingSpinner;
    private EditText quantityInput;
    private Button addToCartBtn;
    private Button backBtn;
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productCategory = findViewById(R.id.productCategory);
        productDescription = findViewById(R.id.productDescription);
        productStock = findViewById(R.id.productStock);
        packagingSpinner = findViewById(R.id.packagingSpinner);
        quantityInput = findViewById(R.id.quantityInput);
        addToCartBtn = findViewById(R.id.addToCartBtn);
        backBtn = findViewById(R.id.backBtn);

        currentProduct = (Product) getIntent().getSerializableExtra("product");

        if (currentProduct != null) {
            displayProduct();
        }

        addToCartBtn.setOnClickListener(v -> handleAddToCart());
        backBtn.setOnClickListener(v -> finish());
    }

    private void displayProduct() {
        if (currentProduct.getImageUrl() != null && !currentProduct.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(currentProduct.getImageUrl())
                    .into(productImage);
        }

        productName.setText(currentProduct.getLibArt());
        productPrice.setText(String.valueOf(currentProduct.getPrixArt()));
        productCategory.setText(currentProduct.getCatArt());
        productDescription.setText(currentProduct.getDescription());
        productStock.setText(String.valueOf(currentProduct.getStock()));

        productName.setEnabled(false);
        productPrice.setEnabled(false);
        productCategory.setEnabled(false);
        productDescription.setEnabled(false);
        productStock.setEnabled(false);
    }

    private void handleAddToCart() {
        String quantityStr = quantityInput.getText().toString().trim();
        String packaging = packagingSpinner.getSelectedItem().toString();

        if (quantityStr.isEmpty()) {
            quantityInput.setError("Quantity is required");
            return;
        }

        int quantity = Integer.parseInt(quantityStr);
        if (quantity <= 0 || quantity > currentProduct.getStock()) {
            quantityInput.setError("Invalid quantity");
            return;
        }

        com.example.computershop.models.CartItem cartItem = new com.example.computershop.models.CartItem(
                null,
                currentProduct.getIdArt(),
                FirebaseManager.getCurrentUser().getUid(),
                quantity,
                packaging,
                System.currentTimeMillis()
        );

        FirebaseManager.addToCart(cartItem, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ProductDetailActivity.this, "Added to cart!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ProductDetailActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
