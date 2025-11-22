package com.example.computershop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computershop.R;
import com.example.computershop.models.CartItem;
import com.example.computershop.models.Product;

import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<CartItem> cartItems;
    private OnRemoveListener removeListener;
    private Map<String, Product> productMap;

    public interface OnRemoveListener {
        void onRemove(CartItem cartItem);
    }

    public CartAdapter(List<CartItem> cartItems, OnRemoveListener removeListener, Map<String, Product> productMap) {
        this.cartItems = cartItems;
        this.removeListener = removeListener;
        this.productMap = productMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        Product product = productMap.get(cartItem.getIdArt());

        if (product != null) {
            holder.productName.setText(product.getLibArt());
            holder.productPrice.setText("$" + product.getPrixArt());
            holder.quantity.setText("Qty: " + cartItem.getQuantité());
            holder.packaging.setText("Packaging: " + cartItem.getEmballage());
            holder.total.setText("Total: $" + (product.getPrixArt() * cartItem.getQuantité()));
        }

        holder.removeBtn.setOnClickListener(v -> removeListener.onRemove(cartItem));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        TextView quantity;
        TextView packaging;
        TextView total;
        Button removeBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.quantity);
            packaging = itemView.findViewById(R.id.packaging);
            total = itemView.findViewById(R.id.total);
            removeBtn = itemView.findViewById(R.id.removeBtn);
        }
    }
}
