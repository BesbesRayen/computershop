package com.example.computershop.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computershop.R;
import com.example.computershop.adapters.AdminProductAdapter;
import com.example.computershop.models.Product;
import com.example.computershop.utils.FirebaseManager;
import com.example.computershop.utils.ImageUtils;
import com.example.computershop.utils.ImageViewerUtils;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminProductsActivity extends AppCompatActivity {
    private RecyclerView productsRecyclerView;
    private AdminProductAdapter productAdapter;
    private List<Product> productList;
    private Button addProductBtn;
    private Button backBtn;
    private Uri selectedImageUri = null;
    private android.widget.ImageView selectedImagePreviewView = null;
    private Button selectedUploadButton = null;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_products);

        Log.d("DEBUG", "AdminProductsActivity onCreate started");

        try {
            // Global exception handler
            Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
                Log.e("APP_CRASH", "Crash in AdminProductsActivity: " + throwable.getMessage());
                throwable.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "App crashed: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
            });

            // Initialize views with null checks
            productsRecyclerView = findViewById(R.id.productsRecyclerView);
            addProductBtn = findViewById(R.id.addProductBtn);
            backBtn = findViewById(R.id.backBtn);

            if (productsRecyclerView == null) {
                Log.e("DEBUG", "productsRecyclerView is null");
                Toast.makeText(this, "RecyclerView not found", Toast.LENGTH_SHORT).show();
                return;
            }
            if (addProductBtn == null) {
                Log.e("DEBUG", "addProductBtn is null");
                return;
            }
            if (backBtn == null) {
                Log.e("DEBUG", "backBtn is null");
                return;
            }

            Log.d("DEBUG", "All views initialized successfully");

            productList = new ArrayList<>();
            productAdapter = new AdminProductAdapter(productList, product -> {
                showEditProductDialog(product);
            }, product -> {
                deleteProduct(product);
            });

            productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            productsRecyclerView.setAdapter(productAdapter);

            loadProducts();

            addProductBtn.setOnClickListener(v -> {
                Log.d("DEBUG", "Add product button clicked");
                showAddProductDialog();
            });
            backBtn.setOnClickListener(v -> {
                Log.d("DEBUG", "Back button clicked");
                finish();
            });

            Log.d("DEBUG", "AdminProductsActivity onCreate completed successfully");

        } catch (Exception e) {
            Log.e("DEBUG", "Error in onCreate: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Error loading activity: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadProducts() {
        Log.d("DEBUG", "Loading products...");
        try {
            FirebaseManager.getProducts(task -> {
                if (task.isSuccessful()) {
                    Log.d("DEBUG", "Products loaded successfully");
                    QuerySnapshot snapshot = task.getResult();
                    productList.clear();
                    if (snapshot != null) {
                        Log.d("DEBUG", "Snapshot size: " + snapshot.size());
                        for (com.google.firebase.firestore.DocumentSnapshot doc : snapshot.getDocuments()) {
                            Product product = doc.toObject(Product.class);
                            if (product != null) {
                                product.setIdArt(doc.getId());
                                productList.add(product);
                                Log.d("DEBUG", "Added product: " + product.getLibArt());
                            }
                        }
                    } else {
                        Log.d("DEBUG", "Snapshot is null");
                    }
                    productAdapter.notifyDataSetChanged();
                    Log.d("DEBUG", "Adapter notified, product count: " + productList.size());
                } else {
                    Log.e("DEBUG", "Failed to load products: " + task.getException());
                    Toast.makeText(AdminProductsActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("DEBUG", "Error in loadProducts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAddProductDialog() {
        Log.d("DEBUG", "Showing add product dialog");
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("📦 Add New Product");

            EditText nameInput = new EditText(this);
            nameInput.setHint("Product Name *");
            nameInput.setPadding(16, 16, 16, 16);

            EditText priceInput = new EditText(this);
            priceInput.setHint("Price (e.g., 99.99) *");
            priceInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
            priceInput.setPadding(16, 16, 16, 16);

            EditText categoryInput = new EditText(this);
            categoryInput.setHint("Category (e.g., Electronics) *");
            categoryInput.setPadding(16, 16, 16, 16);

            EditText descriptionInput = new EditText(this);
            descriptionInput.setHint("Description");
            descriptionInput.setPadding(16, 16, 16, 16);

            EditText stockInput = new EditText(this);
            stockInput.setHint("Stock Quantity *");
            stockInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            stockInput.setPadding(16, 16, 16, 16);

            // Image preview - same style as product list
            android.widget.ImageView imagePreview = new android.widget.ImageView(this);
            android.widget.LinearLayout.LayoutParams imageParams = new android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                    250
            );
            imageParams.setMargins(0, 10, 0, 10);
            imagePreview.setLayoutParams(imageParams);
            imagePreview.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
            imagePreview.setBackgroundColor(android.graphics.Color.parseColor("#E0E0E0"));
            imagePreview.setVisibility(android.view.View.GONE);

            // Image upload button with visual feedback
            Button uploadImageBtn = new Button(this);
            updateUploadButtonUI(uploadImageBtn);
            uploadImageBtn.setPadding(16, 24, 16, 24);
            uploadImageBtn.setTextSize(16);
            uploadImageBtn.setOnClickListener(v -> {
                Log.d("DEBUG", "Upload photo button clicked");
                selectedImagePreviewView = imagePreview;
                selectedUploadButton = uploadImageBtn;
                checkPermissionAndOpenImagePicker();
            });

            android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
            layout.setOrientation(android.widget.LinearLayout.VERTICAL);
            layout.setPadding(16, 16, 16, 16);
            layout.addView(nameInput);
            layout.addView(priceInput);
            layout.addView(categoryInput);
            layout.addView(descriptionInput);
            layout.addView(stockInput);
            
            // Add spacing before image
            android.widget.Space space = new android.widget.Space(this);
            space.setMinimumHeight(10);
            layout.addView(space);
            
            layout.addView(imagePreview);
            layout.addView(uploadImageBtn);

            builder.setView(layout);
            builder.setPositiveButton("✅ Add Product", (dialog, which) -> {
                String name = nameInput.getText().toString().trim();
                String priceStr = priceInput.getText().toString().trim();
                String category = categoryInput.getText().toString().trim();
                String description = descriptionInput.getText().toString().trim();
                String stockStr = stockInput.getText().toString().trim();

                if (name.isEmpty() || priceStr.isEmpty() || category.isEmpty() || stockStr.isEmpty()) {
                    Toast.makeText(AdminProductsActivity.this, "❌ All fields (marked with *) are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedImageUri == null) {
                    Toast.makeText(AdminProductsActivity.this, "❌ Please select a photo", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double price = Double.parseDouble(priceStr);
                    int stock = Integer.parseInt(stockStr);
                    
                    if (price <= 0) {
                        Toast.makeText(AdminProductsActivity.this, "❌ Price must be greater than 0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (stock < 0) {
                        Toast.makeText(AdminProductsActivity.this, "❌ Stock cannot be negative", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    createProductWithBase64Image(name, price, category, description, stock);
                } catch (NumberFormatException e) {
                    Toast.makeText(AdminProductsActivity.this, "❌ Please enter valid price and stock numbers", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Cancel", null);
            builder.setCancelable(false);
            
            AlertDialog dialog = builder.create();
            dialog.show();
            
            int width = (int)(getResources().getDisplayMetrics().widthPixels * 0.9);
            dialog.getWindow().setLayout(width, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            
            Log.d("DEBUG", "Add product dialog shown successfully");
        } catch (Exception e) {
            Log.e("DEBUG", "Error showing add product dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updateUploadButtonUI(Button btn) {
        if (btn == null) {
            Log.w("DEBUG", "updateUploadButtonUI: Button is null");
            return;
        }
        try {
            if (selectedImageUri != null) {
                btn.setText("✅ Photo Selected - Tap to Change");
                btn.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50"));
                btn.setTextColor(android.graphics.Color.WHITE);
            } else {
                btn.setText("📷 Select Photo from Gallery");
                btn.setBackgroundColor(android.graphics.Color.parseColor("#2196F3"));
                btn.setTextColor(android.graphics.Color.WHITE);
            }
        } catch (Exception e) {
            Log.e("DEBUG", "Error updating button UI: " + e.getMessage());
        }
    }

    private void checkPermissionAndOpenImagePicker() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            openImagePicker();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Permission denied. Cannot access photos.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImagePicker() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, "Select Product Photo"), PICK_IMAGE_REQUEST);
            Log.d("DEBUG", "Image picker opened");
        } catch (Exception e) {
            Log.e("DEBUG", "Error opening image picker: " + e.getMessage());
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                try {
                    selectedImageUri = data.getData();
                    Log.d("DEBUG", "Photo selected: " + selectedImageUri.toString());
                    
                    // Show image in preview if available
                    if (selectedImagePreviewView != null) {
                        selectedImagePreviewView.setImageURI(selectedImageUri);
                        selectedImagePreviewView.setVisibility(android.view.View.VISIBLE);
                        Log.d("DEBUG", "Image preview displayed");
                    }
                    
                    // Update button UI if available
                    if (selectedUploadButton != null) {
                        updateUploadButtonUI(selectedUploadButton);
                    }
                    
                    Toast.makeText(this, "✅ Photo selected!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("DEBUG", "Error displaying image preview: " + e.getMessage());
                    Toast.makeText(this, "Error loading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "❌ Failed to select photo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showEditProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("✏️ Edit Product: " + product.getLibArt());

        EditText nameInput = new EditText(this);
        nameInput.setText(product.getLibArt());
        nameInput.setPadding(16, 16, 16, 16);

        EditText priceInput = new EditText(this);
        priceInput.setText(String.valueOf(product.getPrixArt()));
        priceInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        priceInput.setPadding(16, 16, 16, 16);

        EditText categoryInput = new EditText(this);
        categoryInput.setText(product.getCatArt());
        categoryInput.setPadding(16, 16, 16, 16);

        EditText descriptionInput = new EditText(this);
        descriptionInput.setText(product.getDescription());
        descriptionInput.setPadding(16, 16, 16, 16);

        EditText stockInput = new EditText(this);
        stockInput.setText(String.valueOf(product.getStock()));
        stockInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        stockInput.setPadding(16, 16, 16, 16);

        // Image preview - same style as product list
        android.widget.ImageView imagePreview = new android.widget.ImageView(this);
        android.widget.LinearLayout.LayoutParams imageParams = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                250
        );
        imageParams.setMargins(0, 10, 0, 10);
        imagePreview.setLayoutParams(imageParams);
        imagePreview.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
        imagePreview.setBackgroundColor(android.graphics.Color.parseColor("#E0E0E0"));
        
        // Load current product image if exists
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            try {
                android.graphics.Bitmap bitmap = ImageUtils.decodeBase64ToBitmap(product.getImageUrl());
                if (bitmap != null) {
                    imagePreview.setImageBitmap(bitmap);
                } else {
                    imagePreview.setVisibility(android.view.View.GONE);
                }
            } catch (Exception e) {
                Log.e("DEBUG", "Error loading current image: " + e.getMessage());
                imagePreview.setVisibility(android.view.View.GONE);
            }
        } else {
            imagePreview.setVisibility(android.view.View.GONE);
        }

        // Button to change image
        Button changeImageBtn = new Button(this);
        changeImageBtn.setText("📷 Change Image");
        changeImageBtn.setBackgroundColor(android.graphics.Color.parseColor("#2196F3"));
        changeImageBtn.setTextColor(android.graphics.Color.WHITE);
        changeImageBtn.setPadding(16, 20, 16, 20);
        changeImageBtn.setTextSize(14);
        changeImageBtn.setOnClickListener(v -> {
            Log.d("DEBUG", "Change image button clicked");
            selectedImagePreviewView = imagePreview;
            selectedUploadButton = changeImageBtn;
            checkPermissionAndOpenImagePicker();
        });

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);
        layout.addView(nameInput);
        layout.addView(priceInput);
        layout.addView(categoryInput);
        layout.addView(descriptionInput);
        layout.addView(stockInput);
        
        // Add spacing before image
        android.widget.Space space = new android.widget.Space(this);
        space.setMinimumHeight(10);
        layout.addView(space);
        
        layout.addView(imagePreview);
        layout.addView(changeImageBtn);

        builder.setView(layout);
        builder.setPositiveButton("✅ Update", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String priceStr = priceInput.getText().toString().trim();
            String category = categoryInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            String stockStr = stockInput.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty() || category.isEmpty() || stockStr.isEmpty()) {
                Toast.makeText(AdminProductsActivity.this, "❌ All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                int stock = Integer.parseInt(stockStr);

                if (price <= 0) {
                    Toast.makeText(AdminProductsActivity.this, "❌ Price must be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (stock < 0) {
                    Toast.makeText(AdminProductsActivity.this, "❌ Stock cannot be negative", Toast.LENGTH_SHORT).show();
                    return;
                }

                product.setLibArt(name);
                product.setPrixArt(price);
                product.setCatArt(category);
                product.setDescription(description);
                product.setStock(stock);

                // Update image if a new one was selected
                if (selectedImageUri != null) {
                    Toast.makeText(AdminProductsActivity.this, "⏳ Updating image...", Toast.LENGTH_SHORT).show();
                    String base64Image = ImageUtils.convertImageToBase64(selectedImageUri, getContentResolver());
                    if (base64Image != null) {
                        product.setImageUrl(base64Image);
                        selectedImageUri = null;
                        Log.d("DEBUG", "Product image updated");
                    }
                }

                FirebaseManager.saveProduct(product, saveTask -> {
                    if (saveTask.isSuccessful()) {
                        Toast.makeText(AdminProductsActivity.this, "✅ Product updated successfully", Toast.LENGTH_SHORT).show();
                        loadProducts();
                    } else {
                        Toast.makeText(AdminProductsActivity.this, "❌ Failed to update product", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (NumberFormatException e) {
                Toast.makeText(AdminProductsActivity.this, "❌ Please enter valid price and stock numbers", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        
        AlertDialog dialog = builder.create();
        dialog.show();
        
        if (dialog.getWindow() != null) {
            int width = (int)(getResources().getDisplayMetrics().widthPixels * 0.9);
            dialog.getWindow().setLayout(width, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void deleteProduct(Product product) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete: " + product.getLibArt() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    String productId = product.getIdArt();
                    if (productId == null || productId.isEmpty()) {
                        Toast.makeText(AdminProductsActivity.this, "Error: Product ID is missing", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseManager.deleteProduct(productId, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminProductsActivity.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                            loadProducts();
                        } else {
                            Toast.makeText(AdminProductsActivity.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void createProductWithBase64Image(String name, double price, String category, String description, int stock) {
        Log.d("DEBUG", "Creating product with Base64 image");
        if (selectedImageUri == null) {
            Toast.makeText(this, "❌ No photo selected", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Toast.makeText(this, "⏳ Processing image and saving product...", Toast.LENGTH_SHORT).show();

            // Use unified ImageUtils to convert image
            String base64Image = ImageUtils.convertImageToBase64(selectedImageUri, getContentResolver());

            if (base64Image != null && !base64Image.isEmpty()) {
                Log.d("DEBUG", "Base64 image created, size: " + ImageUtils.getBase64SizeKB(base64Image) + " KB");
                
                // Create product with Base64 image
                Product product = new Product(
                        null,
                        name,
                        price,
                        category,
                        description,
                        stock,
                        base64Image
                );
                
                product.setImageUrl(base64Image);

                // Save to Firestore
                FirebaseManager.saveProduct(product, saveTask -> {
                    if (saveTask.isSuccessful()) {
                        Log.d("DEBUG", "Product saved successfully");
                        Toast.makeText(AdminProductsActivity.this, "✅ Product added successfully!", Toast.LENGTH_LONG).show();
                        selectedImageUri = null;
                        loadProducts();
                    } else {
                        String error = saveTask.getException() != null ? saveTask.getException().getMessage() : "Unknown error";
                        Toast.makeText(AdminProductsActivity.this, "❌ Failed to save product:\n" + error, Toast.LENGTH_LONG).show();
                        Log.e("SAVE_PRODUCT", "Error: " + error);
                    }
                });
            } else {
                Toast.makeText(this, "❌ Failed to process image - file may be corrupted", Toast.LENGTH_SHORT).show();
                Log.e("CREATE_PRODUCT", "Base64 conversion returned null");
            }

        } catch (Exception e) {
            Toast.makeText(this, "❌ Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("CREATE_PRODUCT", "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

}