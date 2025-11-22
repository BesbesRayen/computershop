package com.example.computershop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computershop.R;
import com.example.computershop.models.Product;

import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder> {
    private List<Product> products;
    private OnEditListener editListener;
    private OnDeleteListener deleteListener;

    public interface OnEditListener {
        void onEdit(Product product);
    }

    public interface OnDeleteListener {
        void onDelete(Product product);
    }

    public AdminProductAdapter(List<Product> products, OnEditListener editListener, OnDeleteListener deleteListener) {
        this.products = products;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.productName.setText(product.getLibArt());
        holder.productPrice.setText("Price: $" + product.getPrixArt());
        holder.productStock.setText("Stock: " + product.getStock());
        holder.productCategory.setText("Category: " + product.getCatArt());

        holder.editBtn.setOnClickListener(v -> editListener.onEdit(product));
        holder.deleteBtn.setOnClickListener(v -> deleteListener.onDelete(product));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        TextView productStock;
        TextView productCategory;
        Button editBtn;
        Button deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productStock = itemView.findViewById(R.id.productStock);
            productCategory = itemView.findViewById(R.id.productCategory);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
