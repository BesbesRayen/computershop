package com.example.computershop.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computershop.R;
import com.example.computershop.adapters.UserAdapter;
import com.example.computershop.models.User;
import com.example.computershop.utils.FirebaseManager;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminUsersActivity extends AppCompatActivity {
    private RecyclerView usersRecyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);

        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        backBtn = findViewById(R.id.backBtn);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, user -> {

            showEditUserDialog(user);
        }, user -> {

            deleteUser(user);
        });

        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersRecyclerView.setAdapter(userAdapter);

        loadUsers();

        backBtn.setOnClickListener(v -> finish());
    }

    private void loadUsers() {
        // Add debug logging
        Log.d("LOAD_USERS", "=== Loading users ===");
        FirebaseManager.debugAllUserData();

        FirebaseManager.getAllUsers(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                userList.clear();
                if (snapshot != null && !snapshot.isEmpty()) {
                    Log.d("LOAD_USERS", "Raw documents from Firestore: " + snapshot.size());

                    for (com.google.firebase.firestore.DocumentSnapshot doc : snapshot.getDocuments()) {
                        try {
                            Map<String, Object> data = doc.getData();
                            if (data != null) {
                                Log.d("LOAD_USERS", "Processing document: " + doc.getId() + " - " + data);

                                User user = new User();
                                user.setIdInt(doc.getId());
                                user.setLogin(getStringValue(data, "login"));
                                user.setEmail(getStringValue(data, "email"));
                                user.setPays(getStringValue(data, "pays"));
                                user.setRole(getStringValue(data, "role"));


                                Object dateObj = data.get("dateInscrip");
                                if (dateObj instanceof String) {
                                    try {
                                        String dateStr = (String) dateObj;
                                        user.setDateInscrip(Long.parseLong(dateStr));
                                    } catch (NumberFormatException e) {
                                        user.setDateInscrip(System.currentTimeMillis());
                                    }
                                } else if (dateObj instanceof Long) {
                                    user.setDateInscrip((Long) dateObj);
                                } else {
                                    user.setDateInscrip(System.currentTimeMillis());
                                }

                                // Only add user if they have valid email
                                if (!user.getEmail().isEmpty()) {
                                    userList.add(user);
                                    Log.d("LOAD_USERS", "Added to list: " + user.getLogin());
                                } else {
                                    Log.d("LOAD_USERS", "Skipped empty user: " + doc.getId());
                                }
                            }
                        } catch (Exception e) {
                            Log.e("AdminUsersActivity", "Error parsing user document: " + e.getMessage());
                        }
                    }
                    Log.d("LOAD_USERS", "Final userList size: " + userList.size());
                    Toast.makeText(AdminUsersActivity.this, "Loaded " + userList.size() + " users", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminUsersActivity.this, "No users found", Toast.LENGTH_SHORT).show();
                }
                userAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(AdminUsersActivity.this, "Failed to load users: " +
                        (task.getException() != null ? task.getException().getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showEditUserDialog(User user) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Edit User: " + user.getLogin());


        View view = getLayoutInflater().inflate(R.layout.dialog_edit_user, null);


        TextView emailTextView = view.findViewById(R.id.emailTextView);
        AutoCompleteTextView roleSpinner = view.findViewById(R.id.roleSpinner);
        TextView countryEditText = view.findViewById(R.id.countryEditText);

        // Set current values
        emailTextView.setText(user.getEmail());
        countryEditText.setText(user.getPays());


        String[] roles = {"user", "admin"};
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, roles);
        roleSpinner.setAdapter(roleAdapter);
        roleSpinner.setText(user.getRole(), false);

        builder.setView(view);

        builder.setPositiveButton("Save", (dialog, which) -> {
            // Get updated values - ONLY role and country, NOT email
            String newRole = roleSpinner.getText().toString().trim();
            String newCountry = countryEditText.getText().toString().trim();

            // Validate inputs
            if (newRole.isEmpty() || newCountry.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newRole.equals("user") && !newRole.equals("admin")) {
                Toast.makeText(this, "Role must be 'user' or 'admin'", Toast.LENGTH_SHORT).show();
                return;
            }


            updateUserInFirebase(user, newRole, newCountry);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.show();
    }
    private void updateUserInFirebase(User user, String newRole, String newCountry) {
        // Use the original email (don't change it)
        FirebaseManager.updateUser(user.getIdInt(), user.getEmail(), newRole, newCountry, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();

                loadUsers();
            } else {
                Toast.makeText(this, "Failed to update user: " +
                                (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteUser(User user) {
        final String userId = user.getIdInt();
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Delete User");
        builder.setMessage("Are you sure you want to delete user: " + user.getLogin() + "? This action cannot be undone.");

        builder.setPositiveButton("Delete", (dialog, which) -> {
            Log.d("DELETE_USER", "Deleting user: " + userId);

            // Use the fixed deleteUser method (now uses correct Internaute collection)
            FirebaseManager.deleteUser(userId, task -> {
                if (task.isSuccessful()) {
                    Log.d("DELETE_USER", "User deleted successfully");
                    Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show();


                    userList.remove(user);
                    userAdapter.notifyDataSetChanged();

                    // Reload after delay to confirm
                    new android.os.Handler().postDelayed(() -> loadUsers(), 2000);

                } else {
                    Log.e("DELETE_USER", "Deletion failed: " +
                            (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                    Toast.makeText(this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.show();
    }
    private String getStringValue(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : "";
    }
}