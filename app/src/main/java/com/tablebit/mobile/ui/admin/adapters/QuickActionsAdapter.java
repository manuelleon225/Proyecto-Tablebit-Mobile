package com.tablebit.mobile.ui.admin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tablebit.mobile.R;

public class QuickActionsAdapter extends RecyclerView.Adapter<QuickActionsAdapter.ViewHolder> {

    private final String[] iconos;
    private final String[] labels;
    private final OnAccionClickListener listener;

    public interface OnAccionClickListener {
        void onAccionClick(int position, String label);
    }

    public QuickActionsAdapter(String[] iconos, String[] labels, OnAccionClickListener listener) {
        this.iconos = iconos;
        this.labels = labels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_accion_rapida, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvIcono.setText(iconos[position]);
        holder.tvLabel.setText(labels[position]);
        holder.itemView.setOnClickListener(v -> listener.onAccionClick(position, labels[position]));
    }

    @Override
    public int getItemCount() { return labels.length; }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIcono, tvLabel;
        ViewHolder(View v) {
            super(v);
            tvIcono = v.findViewById(R.id.tvIcono);
            tvLabel = v.findViewById(R.id.tvLabel);
        }
    }
}
