package com.tablebit.mobile.ui.cliente;

import com.tablebit.mobile.data.api.NetworkConfig;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.imageview.ShapeableImageView;
import com.tablebit.mobile.R;

import java.util.List;

public class GaleriaAdapter extends RecyclerView.Adapter<GaleriaAdapter.ViewHolder> {

    private final List<String> imagenes;
    private final OnImageClickListener listener;

    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    public GaleriaAdapter(List<String> imagenes, OnImageClickListener listener) {
        this.imagenes = imagenes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_galeria_restaurante, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = imagenes.get(position);
        if (url != null && !url.isEmpty()) {
            String fullUrl = url.startsWith("http") ? url : NetworkConfig.STORAGE_URL + url;
            Glide.with(holder.itemView.getContext()).load(fullUrl)
                    .transform(new CenterCrop(), new RoundedCorners(8))
                    .placeholder(android.R.color.darker_gray).into(holder.ivImagen);
        }
        holder.itemView.setOnClickListener(v -> listener.onImageClick(position));
    }

    @Override
    public int getItemCount() { return imagenes.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivImagen;
        ViewHolder(View v) {
            super(v);
            ivImagen = v.findViewById(R.id.ivGaleria);
        }
    }
}


