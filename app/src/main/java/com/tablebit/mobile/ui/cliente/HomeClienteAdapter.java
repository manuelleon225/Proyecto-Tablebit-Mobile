package com.tablebit.mobile.ui.cliente;

import com.tablebit.mobile.data.api.NetworkConfig;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.model.Restaurante;

import java.util.List;

public class HomeClienteAdapter extends RecyclerView.Adapter<HomeClienteAdapter.ViewHolder> {

    private List<Restaurante> restaurantes;
    private final OnRestauranteClickListener listener;

    public interface OnRestauranteClickListener {
        void onRestauranteClick(Restaurante restaurante);
    }

    public HomeClienteAdapter(List<Restaurante> restaurantes, OnRestauranteClickListener listener) {
        this.restaurantes = restaurantes;
        this.listener = listener;
    }

    public void updateData(List<Restaurante> nuevos) {
        this.restaurantes = nuevos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurante_hero, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurante r = restaurantes.get(position);

        holder.tvNombre.setText(r.getNombre());

        String tipo = r.getTipoComida() != null ? r.getTipoComida() : "";
        String rating = r.getResenasAvgRating() != null
                ? "\u2605 " + String.format("%.1f", r.getResenasAvgRating())
                : "\u2605 -";
        holder.tvRating.setText(rating);
        holder.tvTipoComida.setText(tipo);

        if (r.getAbiertoAhora() != null && r.getAbiertoAhora()) {
            holder.tvEstado.setVisibility(View.VISIBLE);
            holder.tvEstado.setText("\uD83D\uDD50 Abierto ahora");
        } else {
            holder.tvEstado.setVisibility(View.GONE);
        }

        String imageUrl = r.getImagen() != null && !r.getImagen().trim().isEmpty()
                ? NetworkConfig.STORAGE_URL + r.getImagen()
                : null;

        if (imageUrl != null) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .transform(new CenterCrop(), new RoundedCorners(12))
                    .placeholder(android.R.color.darker_gray)
                    .error(android.R.color.darker_gray)
                    .into(holder.ivHero);
        } else {
            holder.ivHero.setImageResource(android.R.color.darker_gray);
        }

        holder.itemView.setOnClickListener(v -> listener.onRestauranteClick(r));
    }

    @Override
    public int getItemCount() {
        return restaurantes != null ? restaurantes.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHero;
        TextView tvNombre, tvRating, tvTipoComida, tvEstado;

        ViewHolder(View itemView) {
            super(itemView);
            ivHero = itemView.findViewById(R.id.ivHero);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvTipoComida = itemView.findViewById(R.id.tvTipoComida);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}


