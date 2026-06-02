package com.tablebit.mobile.ui.cliente;

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

public class ReservaClienteAdapter extends RecyclerView.Adapter<ReservaClienteAdapter.ViewHolder> {

    private List<Reserva> reservas;
    private final boolean mostrarCancelar;
    private OnCancelarClickListener listener;

    public interface OnCancelarClickListener {
        void onCancelarClick(Reserva reserva);
    }

    public ReservaClienteAdapter(List<Reserva> reservas, boolean mostrarCancelar, OnCancelarClickListener listener) {
        this.reservas = reservas;
        this.mostrarCancelar = mostrarCancelar;
        this.listener = listener;
    }

    public void updateData(List<Reserva> nuevos) {
        this.reservas = nuevos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reserva_cliente, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reserva r = reservas.get(position);

        String nombre = r.getRestaurante() != null
                ? r.getRestaurante().getNombre() : "Restaurante #" + r.getRestauranteId();
        holder.tvRestaurante.setText(nombre);
        holder.tvFechaHora.setText(r.getFecha() + " - " + r.getHora());

        String mesaInfo = r.getMesa() != null
                ? "Mesa #" + r.getMesa().getNumero() : "Mesa #" + r.getMesaId();
        holder.tvMesaPersonas.setText(mesaInfo + " | " + r.getCantidadPersonas() + " personas");

        String estado = r.getEstado();
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

        boolean isCancelable = mostrarCancelar && estado != null
                && (estado.equalsIgnoreCase("pendiente") || estado.equalsIgnoreCase("confirmada"));
        holder.btnCancelar.setVisibility(isCancelable ? View.VISIBLE : View.GONE);
        holder.btnCancelar.setOnClickListener(v -> listener.onCancelarClick(r));
    }

    @Override
    public int getItemCount() {
        return reservas != null ? reservas.size() : 0;
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
