package com.example.studentscoremanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerViewNotifications;
    private NotificationAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<NotificationItem> notificationList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        
        dbHelper = new DatabaseHelper(getContext());
        notificationList = new ArrayList<>();
        
        recyclerViewNotifications = view.findViewById(R.id.recyclerViewNotifications);
        
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter(notificationList);
        recyclerViewNotifications.setAdapter(adapter);
        
        loadNotifications();
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tự động tải lại thông báo khi màn hình hiển thị lại
        loadNotifications();
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

    private class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

        private List<NotificationItem> notifications;

        public NotificationAdapter(List<NotificationItem> notifications) {
            this.notifications = notifications;
        }

        @Override
        public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_notification, parent, false);
            return new NotificationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NotificationViewHolder holder, int position) {
            NotificationItem notification = notifications.get(position);
            holder.bind(notification);
        }

        @Override
        public int getItemCount() {
            return notifications.size();
        }

        class NotificationViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTitle, tvContent, tvDate, tvPriority;

            NotificationViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvContent = itemView.findViewById(R.id.tvContent);
                tvDate = itemView.findViewById(R.id.tvDate);
                tvPriority = itemView.findViewById(R.id.tvPriority);
            }

            void bind(NotificationItem notification) {
                tvTitle.setText(notification.getTitle());
                tvContent.setText(notification.getContent());
                tvDate.setText(notification.getDate());
                tvPriority.setText(notification.getPriority().toUpperCase());

                // Set priority color
                int priorityColor = getPriorityColor(notification.getPriority());
                tvPriority.setTextColor(priorityColor);
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
}
