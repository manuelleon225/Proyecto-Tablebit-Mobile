package com.tablebit.mobile.ui.cliente;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tablebit.mobile.R;

import java.util.List;
import java.util.Map;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private final List<Map<String, Object>> reviews;

    public ReviewAdapter(List<Map<String, Object>> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_restaurante, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> r = reviews.get(position);
        String name = r.containsKey("cliente") ? ((Map<String, Object>) r.get("cliente")).get("name").toString() : "A";
        holder.tvAvatar.setText(String.valueOf(name.charAt(0)).toUpperCase());
        holder.tvNombre.setText(name);
        Object rating = r.get("rating");
        holder.tvRating.setText(rating != null ? "\u2605 " + rating.toString() : "");
        holder.tvComentario.setText(r.get("comentario") != null ? r.get("comentario").toString() : "");
        holder.tvFecha.setText(r.get("created_at") != null ? r.get("created_at").toString().substring(0, 10) : "");
    }

    @Override
    public int getItemCount() { return reviews != null ? reviews.size() : 0; }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAvatar, tvNombre, tvRating, tvComentario, tvFecha;
        ViewHolder(View v) {
            super(v);
            tvAvatar = v.findViewById(R.id.tvReviewAvatar);
            tvNombre = v.findViewById(R.id.tvReviewNombre);
            tvRating = v.findViewById(R.id.tvReviewRating);
            tvComentario = v.findViewById(R.id.tvReviewComentario);
            tvFecha = v.findViewById(R.id.tvReviewFecha);
        }
    }
}
