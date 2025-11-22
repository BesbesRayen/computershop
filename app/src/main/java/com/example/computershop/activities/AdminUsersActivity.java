package com.example.computershop.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
            deleteUser(user.getIdInt());
        });

        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersRecyclerView.setAdapter(userAdapter);

        loadUsers();

        backBtn.setOnClickListener(v -> finish());
    }

    private void loadUsers() {
        FirebaseManager.getAllUsers(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                userList.clear();
                if (snapshot != null) {
                    for (com.google.firebase.firestore.DocumentSnapshot doc : snapshot.getDocuments()) {
                        User user = doc.toObject(User.class);
                        userList.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showEditUserDialog(User user) {
        String[] roles = {"user", "admin"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit User: " + user.getLogin());
        builder.setSingleChoiceItems(roles, "user".equals(user.getRole()) ? 0 : 1, (dialog, which) -> {
            user.setRole(roles[which]);
            FirebaseManager.updateUser(user, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminUsersActivity.this, "User updated", Toast.LENGTH_SHORT).show();
                    loadUsers();
                    dialog.dismiss();
                }
            });
        });
        builder.show();
    }

    private void deleteUser(String userId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseManager.deleteUser(userId, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminUsersActivity.this, "User deleted", Toast.LENGTH_SHORT).show();
                            loadUsers();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }
}
