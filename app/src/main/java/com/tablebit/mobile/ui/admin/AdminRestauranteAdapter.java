package com.tablebit.mobile.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.tablebit.mobile.R;
import com.tablebit.mobile.data.model.Restaurante;

import java.util.List;

public class AdminRestauranteAdapter extends RecyclerView.Adapter<AdminRestauranteAdapter.ViewHolder> {

    private final List<Restaurante> restaurantes;
    private final OnAccionListener listener;

    public interface OnAccionListener {
        void onVerDashboard(Restaurante r);
        void onGestionarMesas(Restaurante r);
        void onVerReservas(Restaurante r);
        void onVerCalendario(Restaurante r);
    }

    public AdminRestauranteAdapter(List<Restaurante> restaurantes, OnAccionListener listener) {
        this.restaurantes = restaurantes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_restaurante, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurante r = restaurantes.get(position);
        holder.tvNombre.setText(r.getNombre());
        holder.tvCiudad.setText(r.getCiudad() != null ? r.getCiudad() : "");

        holder.itemView.setOnClickListener(v -> listener.onVerDashboard(r));

        holder.ivOverflow.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.ivOverflow);
            popup.getMenu().add("Ver Dashboard");
            popup.getMenu().add("Gestionar Mesas");
            popup.getMenu().add("Ver Reservas");
            popup.getMenu().add("Ver Calendario");
            popup.setOnMenuItemClickListener(item -> {
                String title = item.getTitle().toString();
                switch (title) {
                    case "Ver Dashboard": listener.onVerDashboard(r); break;
                    case "Gestionar Mesas": listener.onGestionarMesas(r); break;
                    case "Ver Reservas": listener.onVerReservas(r); break;
                    case "Ver Calendario": listener.onVerCalendario(r); break;
                }
                return true;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() { return restaurantes.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCiudad;
        ImageView ivOverflow;
        ViewHolder(View v) {
            super(v);
            tvNombre = v.findViewById(R.id.tvNombre);
            tvCiudad = v.findViewById(R.id.tvCiudad);
            ivOverflow = v.findViewById(R.id.ivOverflow);
        }
    }
}
