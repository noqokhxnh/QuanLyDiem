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
import com.example.qldiem.models.EventCalendar;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private Context context;
    private List<EventCalendar> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(EventCalendar event);
        void onDeleteClick(EventCalendar event);
    }

    public EventAdapter(Context context, List<EventCalendar> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_event, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventCalendar e = list.get(position);
        holder.tvDate.setText(e.getDate());
        holder.tvEvent.setText(e.getEvent());
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(e));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(e));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvEvent;
        ImageView btnEdit, btnDelete;
        ViewHolder(View v) {
            super(v);
            tvDate = v.findViewById(R.id.tvEventDate);
            tvEvent = v.findViewById(R.id.tvEventName);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
