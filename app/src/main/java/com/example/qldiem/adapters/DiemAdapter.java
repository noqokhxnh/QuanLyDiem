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
import com.example.qldiem.models.Diem;
import com.example.qldiem.utils.SessionManager;
import java.util.List;

public class DiemAdapter extends RecyclerView.Adapter<DiemAdapter.ViewHolder> {
    private Context context;
    private List<Diem> list;
    private OnItemClickListener listener;
    private SessionManager session;

    public interface OnItemClickListener {
        void onEditClick(Diem diem);
        void onDeleteClick(Diem diem);
    }

    public DiemAdapter(Context context, List<Diem> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.session = new SessionManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_diem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Diem d = list.get(position);
        holder.tvSV.setText(d.getMaSv() + " - " + d.getTenSV());
        holder.tvMH.setText(d.getMaMH() + " - " + d.getTenMonHoc() + 
                           " (HK" + d.getHocKy() + "/" + d.getNamHoc() + ")");
        
        // Hiển thị 3 loại điểm + tổng kết
        String diemText = String.format("CC: %.1f | GK: %.1f | CK: %.1f\n" +
                                        "📊 Tổng kết: %.2f | Hệ 4: %.2f | Chữ: %s",
                d.getDiemChuyenCan(), d.getDiemGiuaKy(), d.getDiemCuoiKy(),
                d.getDiemTongKet(), d.getDiemHe4(), d.getDiemChu());
        
        holder.tvDiem.setText(diemText);
        
        // Màu sắc theo điểm tổng kết
        if (d.getDiemTongKet() >= 8.5) {
            holder.tvDiem.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else if (d.getDiemTongKet() >= 5.0) {
            holder.tvDiem.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
        } else {
            holder.tvDiem.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }
        
        // Ẩn nút edit/delete nếu là Student
        if (session.isStudent()) {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        } else {
            holder.btnEdit.setOnClickListener(v -> listener.onEditClick(d));
            holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(d));
        }
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSV, tvMH, tvDiem;
        ImageView btnEdit, btnDelete;
        ViewHolder(View v) {
            super(v);
            tvSV = v.findViewById(R.id.tvSinhVien);
            tvMH = v.findViewById(R.id.tvMonHoc);
            tvDiem = v.findViewById(R.id.tvDiem);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
