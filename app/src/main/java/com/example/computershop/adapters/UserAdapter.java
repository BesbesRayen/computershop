package com.example.computershop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computershop.R;
import com.example.computershop.models.User;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> users;
    private OnEditListener editListener;
    private OnDeleteListener deleteListener;

    public interface OnEditListener {
        void onEdit(User user);
    }

    public interface OnDeleteListener {
        void onDelete(User user);
    }

    public UserAdapter(List<User> users, OnEditListener editListener, OnDeleteListener deleteListener) {
        this.users = users;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (users == null || position >= users.size()) return;
        
        User user = users.get(position);
        if (user == null) return;

        if (holder.userName != null) holder.userName.setText("Name: " + user.getLogin());
        if (holder.userEmail != null) holder.userEmail.setText("Email: " + user.getEmail());
        if (holder.userRole != null) holder.userRole.setText("Role: " + user.getRole());
        if (holder.userCountry != null) holder.userCountry.setText("Country: " + user.getPays());

        // Format and display date
        if (holder.userDate != null) {
            if (user.getDateInscrip() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
                String dateStr = sdf.format(new Date(user.getDateInscrip()));
                holder.userDate.setText("Joined: " + dateStr);
            } else {
                holder.userDate.setText("Joined: Unknown");
            }
        }

        // Set click listeners for buttons
        if (holder.editBtn != null) {
            holder.editBtn.setOnClickListener(v -> {
                if (editListener != null) {
                    editListener.onEdit(user);
                }
            });
        }

        if (holder.deleteBtn != null) {
            holder.deleteBtn.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDelete(user);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userEmail, userRole, userCountry, userDate;
        MaterialButton editBtn, deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            userRole = itemView.findViewById(R.id.userRole);
            userCountry = itemView.findViewById(R.id.userCountry); // Changed from userPays to userCountry
            userDate = itemView.findViewById(R.id.userDate);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}