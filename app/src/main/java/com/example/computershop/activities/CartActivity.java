package com.example.computershop.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computershop.R;
import com.example.computershop.adapters.CartAdapter;
import com.example.computershop.models.CartItem;
import com.example.computershop.models.Product;
import com.example.computershop.utils.FirebaseManager;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private Map<String, Product> productMap;
    private TextView totalPriceText;
    private Button checkoutBtn;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceText = findViewById(R.id.totalPriceText);
        checkoutBtn = findViewById(R.id.checkoutBtn);
        backBtn = findViewById(R.id.backBtn);

        cartItems = new ArrayList<>();
        productMap = new HashMap<>();

        cartAdapter = new CartAdapter(cartItems, cartItem -> {
            removeFromCart(cartItem.getNumPanier());
        }, productMap);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        loadCart();

        checkoutBtn.setOnClickListener(v -> {
            Toast.makeText(CartActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });

        backBtn.setOnClickListener(v -> finish());
    }

    private void loadCart() {
        String userId = FirebaseManager.getCurrentUser().getUid();
        FirebaseManager.getCartItems(userId, task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                cartItems.clear();
                if (snapshot != null) {
                    for (com.google.firebase.firestore.DocumentSnapshot doc : snapshot.getDocuments()) {
                        CartItem cartItem = doc.toObject(CartItem.class);
                        cartItems.add(cartItem);
                        loadProductDetails(cartItem.getIdArt());
                    }
                }
                cartAdapter.notifyDataSetChanged();
                calculateTotal();
            }
        });
    }

    private void loadProductDetails(String productId) {
        FirebaseManager.getProductById(productId, task -> {
            if (task.isSuccessful()) {
                Product product = task.getResult().toObject(Product.class);
                if (product != null) {
                    productMap.put(productId, product);
                    cartAdapter.notifyDataSetChanged();
                    calculateTotal();
                }
            }
        });
    }

    private void removeFromCart(String cartItemId) {
        FirebaseManager.removeFromCart(cartItemId, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CartActivity.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
                loadCart();
            }
        });
    }

    private void calculateTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            if (productMap.containsKey(item.getIdArt())) {
                Product product = productMap.get(item.getIdArt());
                total += product.getPrixArt() * item.getQuantité();
            }
        }
        totalPriceText.setText(String.format("Total: $%.2f", total));
    }
}
