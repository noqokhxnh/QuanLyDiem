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
import com.example.qldiem.models.MonHoc;
import java.util.List;

public class MonHocAdapter extends RecyclerView.Adapter<MonHocAdapter.ViewHolder> {
    private Context context;
    private List<MonHoc> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(MonHoc mh);
        void onDeleteClick(MonHoc mh);
    }

    public MonHocAdapter(Context context, List<MonHoc> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_mon_hoc, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MonHoc mh = list.get(position);
        holder.tvMa.setText(mh.getMaMH());
        holder.tvTen.setText(mh.getTenMonHoc());
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(mh));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(mh));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMa, tvTen;
        ImageView btnEdit, btnDelete;
        ViewHolder(View v) {
            super(v);
            tvMa = v.findViewById(R.id.tvMaMH);
            tvTen = v.findViewById(R.id.tvTenMH);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
