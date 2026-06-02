package com.tablebit.mobile.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tablebit.mobile.R;
import com.tablebit.mobile.data.model.Mesa;

import java.util.List;

public class MesaAdminAdapter extends RecyclerView.Adapter<MesaAdminAdapter.ViewHolder> {

    private final List<Mesa> mesas;
    private final OnMesaClickListener listener;

    public interface OnMesaClickListener {
        void onMesaClick(Mesa mesa);
    }

    public MesaAdminAdapter(List<Mesa> mesas, OnMesaClickListener listener) {
        this.mesas = mesas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mesa_admin, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mesa mesa = mesas.get(position);
        holder.tvNumero.setText("Mesa " + mesa.getNumero());
        holder.tvCapacidad.setText(mesa.getCapacidad() + " pers.");

        String estado = mesa.getEstado() != null ? mesa.getEstado().toLowerCase() : "disponible";
        int bgColor, textColor;

        switch (estado) {
            case "ocupada":
                bgColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.estado_cancelada);
                textColor = android.graphics.Color.WHITE;
                holder.cardMesa.setAlpha(0.15f);
                break;
            case "mantenimiento":
                bgColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.estado_pendiente);
                textColor = android.graphics.Color.WHITE;
                holder.cardMesa.setAlpha(0.25f);
                break;
            default:
                bgColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.estado_confirmada);
                textColor = android.graphics.Color.WHITE;
                holder.cardMesa.setAlpha(1.0f);
                break;
        }

        holder.cardMesa.setCardBackgroundColor(bgColor);
        holder.tvNumero.setTextColor(textColor);
        holder.tvCapacidad.setTextColor(textColor);

        holder.itemView.setOnClickListener(v -> listener.onMesaClick(mesa));
    }

    @Override
    public int getItemCount() { return mesas.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardMesa;
        TextView tvNumero, tvCapacidad;

        ViewHolder(View v) {
            super(v);
            cardMesa = v.findViewById(R.id.cardMesa);
            tvNumero = v.findViewById(R.id.tvNumero);
            tvCapacidad = v.findViewById(R.id.tvCapacidad);
        }
    }
}
