package com.tablebit.mobile.ui.mesas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.model.Mesa;

import java.util.List;

public class MesaAdapter extends RecyclerView.Adapter<MesaAdapter.ViewHolder> {

    private List<Mesa> mesas;
    private OnReservarClickListener listener;

    public interface OnReservarClickListener {
        void onReservarClick(Mesa mesa);
    }

    public MesaAdapter(List<Mesa> mesas, OnReservarClickListener listener) {
        this.mesas = mesas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mesa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mesa mesa = mesas.get(position);
        holder.tvMesaInfo.setText("Mesa #" + mesa.getNumero());
        holder.tvCapacidad.setText("Capacidad: " + mesa.getCapacidad() + " personas");
        holder.btnReservar.setOnClickListener(v -> listener.onReservarClick(mesa));
    }

    @Override
    public int getItemCount() {
        return mesas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMesaInfo, tvCapacidad;
        MaterialButton btnReservar;

        ViewHolder(View itemView) {
            super(itemView);
            tvMesaInfo = itemView.findViewById(R.id.tvMesaInfo);
            tvCapacidad = itemView.findViewById(R.id.tvCapacidad);
            btnReservar = itemView.findViewById(R.id.btnReservar);
        }
    }
}
