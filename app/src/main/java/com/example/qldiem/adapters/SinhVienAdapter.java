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
import com.example.qldiem.models.SinhVien;
import com.example.qldiem.utils.ImageUtils;
import java.util.List;

public class SinhVienAdapter extends RecyclerView.Adapter<SinhVienAdapter.ViewHolder> {
    private Context context;
    private List<SinhVien> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SinhVien sinhVien);
        void onDeleteClick(SinhVien sinhVien);
    }

    public SinhVienAdapter(Context context, List<SinhVien> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sinh_vien, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SinhVien sv = list.get(position);
        holder.tvMaSV.setText(sv.getMaSv());
        holder.tvTenSV.setText(sv.getTenSV());
        holder.tvEmail.setText(sv.getEmail());
        holder.tvLop.setText("Lớp: " + (sv.getTenLop() != null ? sv.getTenLop() : "N/A"));
        holder.tvChuyenNganh.setText("CN: " + (sv.getTenChuyenNganh() != null ? sv.getTenChuyenNganh() : "N/A"));
        
        if (sv.getHinh() != null && !sv.getHinh().isEmpty()) {
            holder.imgAvatar.setImageBitmap(ImageUtils.base64ToBitmap(sv.getHinh()));
        } else {
            holder.imgAvatar.setImageResource(R.drawable.ic_person);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(sv));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(sv));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaSV, tvTenSV, tvEmail, tvLop, tvChuyenNganh;
        ImageView imgAvatar, btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvMaSV = itemView.findViewById(R.id.tvMaSV);
            tvTenSV = itemView.findViewById(R.id.tvTenSV);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvLop = itemView.findViewById(R.id.tvLop);
            tvChuyenNganh = itemView.findViewById(R.id.tvChuyenNganh);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
