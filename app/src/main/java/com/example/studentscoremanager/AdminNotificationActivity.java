package com.example.studentscoremanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminNotificationActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationItem> notificationList;
    private FloatingActionButton fabAddNotification;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification);

        dbHelper = new DatabaseHelper(this);
        notificationList = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerViewNotifications);
        fabAddNotification = findViewById(R.id.fabAddNotification);

        // Setup toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter();
        recyclerView.setAdapter(adapter);

        loadNotifications();

        fabAddNotification.setOnClickListener(v -> showAddNotificationDialog());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void loadNotifications() {
        notificationList.clear();
        Cursor cursor = dbHelper.getAllNotifications();
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOTIFICATION_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOTIFICATION_TITLE));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOTIFICATION_CONTENT));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOTIFICATION_DATE));
                String priority = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOTIFICATION_PRIORITY));
                int isRead = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOTIFICATION_IS_READ));
                
                notificationList.add(new NotificationItem(id, title, content, date, priority, isRead == 1));
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        adapter.notifyDataSetChanged();
    }

    private void showAddNotificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_notification, null);
        builder.setView(dialogView);

        EditText edtTitle = dialogView.findViewById(R.id.edtTitle);
        EditText edtContent = dialogView.findViewById(R.id.edtContent);
        Spinner spinnerPriority = dialogView.findViewById(R.id.spinnerPriority);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        // Setup spinner
        String[] priorities = {"high", "medium", "low"};
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, priorities);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityAdapter);

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String content = edtContent.getText().toString().trim();
            String priority = spinnerPriority.getSelectedItem().toString();

            if (title.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tiêu đề thông báo", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.addNotification(title, content, priority)) {
                Toast.makeText(this, "Thêm thông báo thành công", Toast.LENGTH_SHORT).show();
                loadNotifications();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Lỗi khi thêm thông báo", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showEditNotificationDialog(NotificationItem notification) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_notification, null);
        builder.setView(dialogView);

        EditText edtTitle = dialogView.findViewById(R.id.edtTitle);
        EditText edtContent = dialogView.findViewById(R.id.edtContent);
        Spinner spinnerPriority = dialogView.findViewById(R.id.spinnerPriority);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        // Fill current data
        edtTitle.setText(notification.getTitle());
        edtContent.setText(notification.getContent());

        // Setup spinner
        String[] priorities = {"high", "medium", "low"};
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, priorities);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityAdapter);
        
        // Set selected priority
        for (int i = 0; i < priorities.length; i++) {
            if (priorities[i].equals(notification.getPriority())) {
                spinnerPriority.setSelection(i);
                break;
            }
        }

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String content = edtContent.getText().toString().trim();
            String priority = spinnerPriority.getSelectedItem().toString();

            if (title.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tiêu đề thông báo", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update notification (you'll need to add updateNotification method to DatabaseHelper)
            if (dbHelper.addNotification(title, content, priority)) {
                Toast.makeText(this, "Cập nhật thông báo thành công", Toast.LENGTH_SHORT).show();
                loadNotifications();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật thông báo", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

        @Override
        public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
            return new NotificationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NotificationViewHolder holder, int position) {
            NotificationItem notification = notificationList.get(position);
            holder.bind(notification);
        }

        @Override
        public int getItemCount() {
            return notificationList.size();
        }

        class NotificationViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTitle, tvContent, tvDate, tvPriority;
            private Button btnEdit, btnDelete;

            NotificationViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvContent = itemView.findViewById(R.id.tvContent);
                tvDate = itemView.findViewById(R.id.tvDate);
                tvPriority = itemView.findViewById(R.id.tvPriority);
                btnEdit = itemView.findViewById(R.id.btnEdit);
                btnDelete = itemView.findViewById(R.id.btnDelete);
            }

            void bind(NotificationItem notification) {
                tvTitle.setText(notification.getTitle());
                tvContent.setText(notification.getContent());
                tvDate.setText(notification.getDate());
                tvPriority.setText(notification.getPriority().toUpperCase());

                // Set priority color
                int priorityColor = getPriorityColor(notification.getPriority());
                tvPriority.setTextColor(priorityColor);

                btnEdit.setOnClickListener(v -> showEditNotificationDialog(notification));
                btnDelete.setOnClickListener(v -> {
                    new AlertDialog.Builder(AdminNotificationActivity.this)
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc chắn muốn xóa thông báo này?")
                            .setPositiveButton("Xóa", (dialog, which) -> {
                                if (dbHelper.deleteNotification(notification.getId())) {
                                    Toast.makeText(AdminNotificationActivity.this, "Xóa thông báo thành công", Toast.LENGTH_SHORT).show();
                                    loadNotifications();
                                } else {
                                    Toast.makeText(AdminNotificationActivity.this, "Lỗi khi xóa thông báo", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                });
            }

            private int getPriorityColor(String priority) {
                switch (priority.toLowerCase()) {
                    case "high":
                        return getResources().getColor(android.R.color.holo_red_dark);
                    case "medium":
                        return getResources().getColor(android.R.color.holo_orange_dark);
                    case "low":
                        return getResources().getColor(android.R.color.holo_green_dark);
                    default:
                        return getResources().getColor(android.R.color.black);
                }
            }
        }
    }

    public static class NotificationItem {
        private int id;
        private String title, content, date, priority;
        private boolean isRead;

        public NotificationItem(int id, String title, String content, String date, String priority, boolean isRead) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.date = date;
            this.priority = priority;
            this.isRead = isRead;
        }

        // Getters
        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getContent() { return content; }
        public String getDate() { return date; }
        public String getPriority() { return priority; }
        public boolean isRead() { return isRead; }
    }
}
