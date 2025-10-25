package com.example.qldiem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qldiem.R;
import com.example.qldiem.database.DatabaseHelper;
import com.example.qldiem.models.TaiKhoan;
import com.example.qldiem.models.SinhVien;
import java.util.List;

public class TaiKhoanAdapter extends RecyclerView.Adapter<TaiKhoanAdapter.ViewHolder> {
    private Context context;
    private List<TaiKhoan> list;
    private OnItemClickListener listener;
    private DatabaseHelper db;

    public interface OnItemClickListener {
        void onEditClick(TaiKhoan taiKhoan);
        void onDeleteClick(TaiKhoan taiKhoan);
    }

    public TaiKhoanAdapter(Context context, List<TaiKhoan> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tai_khoan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaiKhoan tk = list.get(position);
        holder.tvUsername.setText(tk.getTenTaiKhoan());
        holder.tvFullName.setText(tk.getHoTen());
        
        // Hiển thị thông tin sinh viên nếu là Student
        if ("Student".equals(tk.getVaiTro())) {
            SinhVien sv = db.getSinhVienByMa(tk.getTenTaiKhoan());
            if (sv != null) {
                holder.tvRole.setText("Vai trò: " + tk.getVaiTro() + 
                    "\n📧 " + sv.getEmail() +
                    "\n🏫 Lớp: " + sv.getMaLop() +
                    "\n🎓 Ngành: " + sv.getMaChuyenNganh());
                holder.tvRole.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
            } else {
                holder.tvRole.setText("Vai trò: " + tk.getVaiTro() + " ⚠️ Chưa có hồ sơ");
                holder.tvRole.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
            }
        } else if ("Admin".equals(tk.getVaiTro())) {
            holder.tvRole.setText("Vai trò: " + tk.getVaiTro());
            holder.tvRole.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            holder.btnDelete.setVisibility(View.GONE); // Không cho xóa admin
        } else {
            holder.tvRole.setText("Vai trò: " + tk.getVaiTro());
            holder.tvRole.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        }

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(tk));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(tk));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvFullName, tvRole;
        ImageView btnEdit, btnDelete;

        ViewHolder(View v) {
            super(v);
            tvUsername = v.findViewById(R.id.tvUsername);
            tvFullName = v.findViewById(R.id.tvFullName);
            tvRole = v.findViewById(R.id.tvRole);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
