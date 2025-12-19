package com.example.computershop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computershop.R;
import com.example.computershop.models.Product;
import com.example.computershop.utils.ImageUtils;
import com.example.computershop.utils.ImageViewerUtils;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder> {
    private List<Product> productList;
    private OnEditListener editListener;
    private OnDeleteListener deleteListener;

    public interface OnEditListener {
        void onEdit(Product product);
    }

    public interface OnDeleteListener {
        void onDelete(Product product);
    }

    public AdminProductAdapter(List<Product> productList, OnEditListener editListener, OnDeleteListener deleteListener) {
        this.productList = productList != null ? productList : new ArrayList<>();
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
        if (productList == null || position >= productList.size()) return;

        Product product = productList.get(position);
        if (product == null) return;

        if (holder.productName != null) {
            holder.productName.setText(product.getLibArt() != null ? product.getLibArt() : "Unknown Product");
        }

        if (holder.productPrice != null) {
            holder.productPrice.setText("$" + (product.getPrixArt() > 0 ? product.getPrixArt() : "0.00"));
        }

        if (holder.productCategory != null) {
            holder.productCategory.setText(product.getCatArt() != null ? product.getCatArt() : "Uncategorized");
        }

        if (holder.productDescription != null) {
            holder.productDescription.setText(product.getDescription() != null ? product.getDescription() : "No description available");
        }

        if (holder.productStock != null) {
            int stock = product.getStock();
            String stockText = stock + " in stock";
            holder.productStock.setText(stockText);
        }

        // Bind product image if available
        if (holder.productImage != null) {
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                // Decode Base64 and display image
                android.graphics.Bitmap bitmap = ImageUtils.decodeBase64ToBitmap(product.getImageUrl());
                if (bitmap != null) {
                    holder.productImage.setImageBitmap(bitmap);
                    holder.productImage.setVisibility(View.VISIBLE);
                    
                    // Make image clickable to view full screen
                    holder.productImage.setOnClickListener(v -> {
                        ImageViewerUtils.showImageDialog(
                                v.getContext(),
                                product.getImageUrl(),
                                product.getLibArt()
                        );
                    });
                } else {
                    holder.productImage.setVisibility(View.GONE);
                }
            } else {
                holder.productImage.setVisibility(View.GONE);
            }
        }

        // Bind button listeners with null checks
        if (holder.editBtn != null) {
            holder.editBtn.setOnClickListener(v -> {
                if (editListener != null) editListener.onEdit(product);
            });
        }

        if (holder.deleteBtn != null) {
            holder.deleteBtn.setOnClickListener(v -> {
                if (deleteListener != null) deleteListener.onDelete(product);
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productCategory, productDescription, productStock;
        ImageView productImage;
        MaterialButton editBtn, deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                productName = itemView.findViewById(R.id.productName);
                productPrice = itemView.findViewById(R.id.productPrice);
                productCategory = itemView.findViewById(R.id.productCategory);
                productDescription = itemView.findViewById(R.id.productDescription);
                productStock = itemView.findViewById(R.id.productStock);
                productImage = itemView.findViewById(R.id.productImage);
                editBtn = itemView.findViewById(R.id.editBtn);
                deleteBtn = itemView.findViewById(R.id.deleteBtn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateData(List<Product> newProducts) {
        this.productList = newProducts != null ? newProducts : new ArrayList<>();
        notifyDataSetChanged();
    }
}