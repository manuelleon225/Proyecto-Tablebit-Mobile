package com.tablebit.mobile.ui.cliente;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tablebit.mobile.R;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {

    private List<String> nombres;
    private final OnCategoriaClickListener listener;

    public interface OnCategoriaClickListener {
        void onCategoriaClick(String categoria);
    }

    public CategoriaAdapter(List<String> nombres, OnCategoriaClickListener listener) {
        this.nombres = nombres;
        this.listener = listener;
    }

    public void updateNombres(List<String> nuevosNombres) {
        this.nombres = nuevosNombres;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String nombre = nombres.get(position);
        holder.tvIcono.setText(getEmoji(nombre));
        holder.tvNombre.setText(nombre);
        holder.itemView.setOnClickListener(v -> listener.onCategoriaClick(nombre));
    }

    @Override
    public int getItemCount() { return nombres.size(); }

    private String getEmoji(String categoria) {
        if (categoria == null) return "🍽️";
        String c = categoria.toLowerCase();
        if (c.contains("italian") || c.contains("pasta") || c.contains("pizza")) return "🍕";
        if (c.contains("japones") || c.contains("sushi") || c.contains("japanese")) return "🍣";
        if (c.contains("parrill") || c.contains("asado") || c.contains("carne") || c.contains("steak")) return "🥩";
        if (c.contains("mexican")) return "🌮";
        if (c.contains("español") || c.contains("spanish")) return "🥘";
        if (c.contains("internacional") || c.contains("world")) return "🌍";
        if (c.contains("fast food") || c.contains("rapida") || c.contains("hamburg")) return "🍔";
        if (c.contains("cafe") || c.contains("coffee")) return "☕";
        if (c.contains("salud") || c.contains("healthy") || c.contains("veggie")) return "🥗";
        if (c.contains("marisc") || c.contains("pesc") || c.contains("seafood")) return "🐟";
        if (c.contains("colombian") || c.contains("local") || c.contains("tipico")) return "🌯";
        if (c.contains("postre") || c.contains("dulce")) return "🍰";
        return "🍽️";
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIcono, tvNombre;
        ViewHolder(View v) {
            super(v);
            tvIcono = v.findViewById(R.id.tvCategoriaIcono);
            tvNombre = v.findViewById(R.id.tvCategoriaNombre);
        }
    }
}
