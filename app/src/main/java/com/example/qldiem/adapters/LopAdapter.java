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
import com.example.qldiem.models.Lop;
import java.util.List;

public class LopAdapter extends RecyclerView.Adapter<LopAdapter.ViewHolder> {
    private Context context;
    private List<Lop> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Lop lop);
        void onDeleteClick(Lop lop);
    }

    public LopAdapter(Context context, List<Lop> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_lop, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lop lop = list.get(position);
        holder.tvMa.setText(lop.getMaLop());
        holder.tvTen.setText(lop.getTenLop());
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(lop));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(lop));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMa, tvTen;
        ImageView btnEdit, btnDelete;
        ViewHolder(View v) {
            super(v);
            tvMa = v.findViewById(R.id.tvMaLop);
            tvTen = v.findViewById(R.id.tvTenLop);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
