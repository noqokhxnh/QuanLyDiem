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
import com.example.qldiem.models.ChuyenNganh;
import java.util.List;

public class ChuyenNganhAdapter extends RecyclerView.Adapter<ChuyenNganhAdapter.ViewHolder> {
    private Context context;
    private List<ChuyenNganh> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(ChuyenNganh cn);
        void onDeleteClick(ChuyenNganh cn);
    }

    public ChuyenNganhAdapter(Context context, List<ChuyenNganh> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chuyen_nganh, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChuyenNganh cn = list.get(position);
        holder.tvMa.setText(cn.getMaChuyenNganh());
        holder.tvTen.setText(cn.getTenChuyenNganh());
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(cn));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(cn));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMa, tvTen;
        ImageView btnEdit, btnDelete;
        ViewHolder(View v) {
            super(v);
            tvMa = v.findViewById(R.id.tvMaChuyenNganh);
            tvTen = v.findViewById(R.id.tvTenChuyenNganh);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
