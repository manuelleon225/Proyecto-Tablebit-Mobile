package com.tablebit.mobile.ui.onboarding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tablebit.mobile.R;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder> {

    private final String[] iconos = {"🍽", "🔍", "✅"};
    private final String[] titulos = {
            "Reserva en los mejores restaurantes",
            "Descubre nuevas experiencias",
            "Gestiona tus reservas"
    };
    private final String[] descs = {
            "Encuentra y reserva mesa en los restaurantes m\u00e1s exclusivos de tu ciudad con solo unos clics.",
            "Explora restaurantes por tipo de cocina, rating y ubicaci\u00f3n. Encuentra tu pr\u00f3xima experiencia gastron\u00f3mica.",
            "Administra todas tus reservas en un solo lugar. Cancela, modifica o revisa tu historial f\u00e1cilmente."
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding_page, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvIcono.setText(iconos[position]);
        holder.tvTitulo.setText(titulos[position]);
        holder.tvDesc.setText(descs[position]);
    }

    @Override
    public int getItemCount() { return 3; }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIcono, tvTitulo, tvDesc;
        ViewHolder(View v) {
            super(v);
            tvIcono = v.findViewById(R.id.tvOnboardingIcon);
            tvTitulo = v.findViewById(R.id.tvOnboardingTitle);
            tvDesc = v.findViewById(R.id.tvOnboardingDesc);
        }
    }
}
