package com.example.studentscoremanager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;

public class HomeFragment extends Fragment {

    private CardView cardDiemSo, cardLichHoc, cardTinTuc, cardThongBao, cardProfile;
    private SharedPreferencesHelper prefsHelper;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("HomeFragment", "onCreateView started");
        
        try {
            // Nạp layout XML
            View view = inflater.inflate(R.layout.fragment_home, container, false);
            Log.d("HomeFragment", "Layout inflated successfully");

            // Ánh xạ các CardView
            cardDiemSo = view.findViewById(R.id.cardDiemSo);
            cardLichHoc = view.findViewById(R.id.cardLichHoc);
            cardTinTuc = view.findViewById(R.id.cardTinTuc);
            cardThongBao = view.findViewById(R.id.cardThongBao);
            cardProfile = view.findViewById(R.id.cardProfile);
            Log.d("HomeFragment", "CardViews found");

            // Helpers
            prefsHelper = new SharedPreferencesHelper(requireContext());
            dbHelper = new DatabaseHelper(requireContext());

            // Xử lý sự kiện click
            addPressAnimation(cardDiemSo);
            cardDiemSo.setOnClickListener(v -> {
                Log.d("HomeFragment", "Card Điểm số clicked");
                openFragment(new ScoresFragment(), "Điểm số");
            });
            addPressAnimation(cardLichHoc);
            cardLichHoc.setOnClickListener(v -> {
                Log.d("HomeFragment", "Card Lịch học clicked");
                openFragment(new TimetableFragment(), "Lịch học");
            });
            addPressAnimation(cardTinTuc);
            cardTinTuc.setOnClickListener(v -> {
                Log.d("HomeFragment", "Card Tin tức clicked");
                openFragment(new ScheduleFragment(), "Thời khóa biểu");
            });
            addPressAnimation(cardThongBao);
            cardThongBao.setOnClickListener(v -> {
                openFragment(new NotificationsFragment(), "Thông báo");
            });
            addPressAnimation(cardProfile);
            cardProfile.setOnClickListener(v -> {
                Log.d("HomeFragment", "Card Profile clicked");
                Intent intent = new Intent(getActivity(), StudentProfileActivity.class);
                startActivity(intent);
            });
            Log.d("HomeFragment", "Click listeners set");

            // Loại bỏ lối tắt admin (long press)

            return view;
        } catch (Exception e) {
            Log.e("HomeFragment", "Error in onCreateView", e);
            Toast.makeText(getContext(), "Lỗi tạo HomeFragment: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    // Hàm mở Fragment khác
    private void openFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fragment_fade_in,
                R.anim.fragment_fade_out,
                R.anim.fragment_fade_in,
                R.anim.fragment_fade_out
        );
        transaction.replace(R.id.student_fragment_container, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    // Hàm hiển thị Toast tạm
    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void addPressAnimation(View view) {
        float pressedScale = 0.98f;
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case android.view.MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(pressedScale).scaleY(pressedScale).setDuration(100).start();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.animate().translationZ(12f).setDuration(100).start();
                    }
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    break;
                case android.view.MotionEvent.ACTION_UP:
                case android.view.MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.animate().translationZ(0f).setDuration(100).start();
                    }
                    break;
            }
            return false;
        });
    }

    private void showScheduleDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("📅 Lịch học")
                .setMessage("Lịch học tuần này:\n\n" +
                        "Thứ 2: Lập trình Java (8:00-10:00)\n" +
                        "Thứ 3: Cơ sở dữ liệu (8:00-10:00)\n" +
                        "Thứ 4: Mạng máy tính (14:00-16:00)\n" +
                        "Thứ 5: Phân tích thiết kế hệ thống (8:00-10:00)\n" +
                        "Thứ 6: Thực hành Java (14:00-16:00)")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showNewsDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("📰 Tin tức")
                .setMessage("Tin tức mới nhất:\n\n" +
                        "• Lịch thi học kỳ 1 năm học 2024-2025\n" +
                        "• Thông báo đăng ký môn học tự chọn\n" +
                        "• Hướng dẫn thực tập tốt nghiệp\n" +
                        "• Chương trình học bổng sinh viên xuất sắc\n\n" +
                        "Chi tiết xem tại website trường.")
                .setPositiveButton("OK", null)
                .show();
    }

    // Loại bỏ helper admin

}
