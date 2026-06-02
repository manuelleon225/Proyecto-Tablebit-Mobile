package com.tablebit.mobile.ui.reservas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.model.Reserva;

import java.util.List;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ViewHolder> {

    private List<Reserva> reservas;
    private OnCancelarClickListener listener;

    public interface OnCancelarClickListener {
        void onCancelarClick(Reserva reserva);
    }

    public ReservaAdapter(List<Reserva> reservas, OnCancelarClickListener listener) {
        this.reservas = reservas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reserva, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reserva reserva = reservas.get(position);
        String nombreRestaurante = reserva.getRestaurante() != null
                ? reserva.getRestaurante().getNombre() : "Restaurante #" + reserva.getRestauranteId();

        holder.tvRestaurante.setText(nombreRestaurante);
        holder.tvFechaHora.setText(reserva.getFecha() + " - " + reserva.getHora());

        String mesaInfo = reserva.getMesa() != null
                ? "Mesa #" + reserva.getMesa().getNumero() : "Mesa #" + reserva.getMesaId();
        holder.tvMesaPersonas.setText(mesaInfo + " | " + reserva.getCantidadPersonas() + " personas");

        String estado = reserva.getEstado();
        int color;
        String label;

        if (estado != null) {
            switch (estado.toLowerCase()) {
                case "pendiente":
                    color = R.color.estado_pendiente;
                    label = "Pendiente";
                    break;
                case "confirmada":
                    color = R.color.estado_confirmada;
                    label = "Confirmada";
                    break;
                case "cancelada":
                    color = R.color.estado_cancelada;
                    label = "Cancelada";
                    break;
                default:
                    color = R.color.text_secondary;
                    label = estado;
            }
        } else {
            color = R.color.text_secondary;
            label = "Desconocido";
        }

        holder.tvEstado.setText(label);
        holder.tvEstado.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), color));

        boolean isCancelable = estado != null && (estado.equalsIgnoreCase("pendiente") || estado.equalsIgnoreCase("confirmada"));
        holder.btnCancelar.setVisibility(isCancelable ? View.VISIBLE : View.GONE);
        holder.btnCancelar.setOnClickListener(v -> listener.onCancelarClick(reserva));
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRestaurante, tvFechaHora, tvMesaPersonas, tvEstado;
        MaterialButton btnCancelar;

        ViewHolder(View itemView) {
            super(itemView);
            tvRestaurante = itemView.findViewById(R.id.tvRestaurante);
            tvFechaHora = itemView.findViewById(R.id.tvFechaHora);
            tvMesaPersonas = itemView.findViewById(R.id.tvMesaPersonas);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            btnCancelar = itemView.findViewById(R.id.btnCancelar);
        }
    }
}
