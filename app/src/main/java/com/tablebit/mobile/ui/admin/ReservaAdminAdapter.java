package com.tablebit.mobile.ui.admin;

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

public class ReservaAdminAdapter extends RecyclerView.Adapter<ReservaAdminAdapter.ViewHolder> {

    private List<Reserva> reservas;
    private final OnAccionListener listener;

    public interface OnAccionListener {
        void onCheckIn(Reserva reserva);
        void onCancelar(Reserva reserva);
    }

    public ReservaAdminAdapter(List<Reserva> reservas, OnAccionListener listener) {
        this.reservas = reservas;
        this.listener = listener;
    }

    public void updateData(List<Reserva> nuevos) {
        this.reservas = nuevos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserva_admin, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reserva r = reservas.get(position);

        holder.tvHora.setText(r.getHora());
        String cliente = r.getCliente() != null ? r.getCliente().getName() : "Cliente #" + r.getClienteId();
        holder.tvCliente.setText(cliente);

        String mesaInfo = r.getMesa() != null
                ? "Mesa #" + r.getMesa().getNumero() + " \u00b7 " + r.getCantidadPersonas() + " pers."
                : "Mesa #" + r.getMesaId() + " \u00b7 " + r.getCantidadPersonas() + " pers.";
        holder.tvDetalle.setText(mesaInfo);

        String estado = r.getEstado() != null ? r.getEstado().toLowerCase() : "";
        int color;
        String label;
        switch (estado) {
            case "pendiente":
                color = R.color.estado_pendiente; label = "Pendiente"; break;
            case "confirmada":
                color = R.color.estado_confirmada; label = "Confirmada"; break;
            case "cancelada":
                color = R.color.estado_cancelada; label = "Cancelada"; break;
            default:
                color = R.color.text_secondary; label = estado; break;
        }
        holder.tvEstado.setText(label);
        holder.tvEstado.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), color));

        boolean accionable = estado.equals("pendiente") || estado.equals("confirmada");
        holder.btnCheckIn.setVisibility(accionable ? View.VISIBLE : View.GONE);
        holder.btnCancelar.setVisibility(accionable ? View.VISIBLE : View.GONE);

        holder.btnCheckIn.setOnClickListener(v -> listener.onCheckIn(r));
        holder.btnCancelar.setOnClickListener(v -> listener.onCancelar(r));
    }

    @Override
    public int getItemCount() { return reservas != null ? reservas.size() : 0; }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHora, tvEstado, tvCliente, tvDetalle;
        MaterialButton btnCheckIn, btnCancelar;

        ViewHolder(View v) {
            super(v);
            tvHora = v.findViewById(R.id.tvHora);
            tvEstado = v.findViewById(R.id.tvEstado);
            tvCliente = v.findViewById(R.id.tvCliente);
            tvDetalle = v.findViewById(R.id.tvDetalle);
            btnCheckIn = v.findViewById(R.id.btnCheckIn);
            btnCancelar = v.findViewById(R.id.btnCancelar);
        }
    }
}
