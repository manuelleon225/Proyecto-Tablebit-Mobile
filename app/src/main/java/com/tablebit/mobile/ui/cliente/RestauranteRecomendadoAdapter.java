package com.tablebit.mobile.ui.cliente;

import com.tablebit.mobile.data.api.NetworkConfig;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.model.Restaurante;

import java.util.List;

public class RestauranteRecomendadoAdapter extends RecyclerView.Adapter<RestauranteRecomendadoAdapter.ViewHolder> {

    private List<Restaurante> restaurantes;
    private final OnRecomendadoClickListener listener;

    public interface OnRecomendadoClickListener {
        void onReservarClick(Restaurante r);
    }

    public RestauranteRecomendadoAdapter(List<Restaurante> restaurantes, OnRecomendadoClickListener listener) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurante_recomendado, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurante r = restaurantes.get(position);

        String url = r.getImagen() != null ? NetworkConfig.STORAGE_URL + r.getImagen() : null;
        if (url != null) {
            Glide.with(holder.itemView.getContext()).load(url)
                    .transform(new CenterCrop(), new RoundedCorners(12))
                    .placeholder(android.R.color.darker_gray).into(holder.ivImagen);
        } else {
            Glide.with(holder.itemView.getContext()).clear(holder.ivImagen);
        }

        holder.tvNombre.setText(r.getNombre());
        String rating = r.getResenasAvgRating() != null ? "\u2605 " + String.format("%.1f", r.getResenasAvgRating()) : "";
        holder.tvRating.setText(rating);
        holder.tvTipo.setText(r.getTipoComida() != null ? r.getTipoComida() : "");
        holder.tvCiudad.setText(r.getCiudad() != null ? r.getCiudad() : "");
        holder.tvEstado.setText(Boolean.TRUE.equals(r.getAbiertoAhora()) ? "Abierto ahora" : "");
        holder.btnReservar.setOnClickListener(v -> listener.onReservarClick(r));
        holder.itemView.setOnClickListener(v -> listener.onReservarClick(r));
    }

    @Override
    public int getItemCount() { return restaurantes != null ? restaurantes.size() : 0; }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivImagen;
        TextView tvNombre, tvRating, tvTipo, tvCiudad, tvEstado;
        MaterialButton btnReservar;

        ViewHolder(View v) {
            super(v);
            ivImagen = v.findViewById(R.id.ivRecomendado);
            tvNombre = v.findViewById(R.id.tvNombreRecomendado);
            tvRating = v.findViewById(R.id.tvRatingRecomendado);
            tvTipo = v.findViewById(R.id.tvTipoRecomendado);
            tvCiudad = v.findViewById(R.id.tvCiudadRecomendado);
            tvEstado = v.findViewById(R.id.tvEstadoRecomendado);
            btnReservar = v.findViewById(R.id.btnReservarRecomendado);
        }
    }
}


