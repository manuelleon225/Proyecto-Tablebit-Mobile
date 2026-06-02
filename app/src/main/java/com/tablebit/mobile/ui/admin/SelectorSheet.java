package com.tablebit.mobile.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.model.Restaurante;

import java.util.List;

public class SelectorSheet extends BottomSheetDialogFragment {

    private final List<Restaurante> restaurantes;
    private final OnRestauranteSelectedListener listener;

    public interface OnRestauranteSelectedListener {
        void onRestauranteSelected(Restaurante r);
    }

    public SelectorSheet(List<Restaurante> restaurantes, OnRestauranteSelectedListener listener) {
        this.restaurantes = restaurantes;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_selector_restaurante, container, false);

        LinearLayout containerLayout = view.findViewById(R.id.containerLista);

        for (Restaurante r : restaurantes) {
            MaterialCardView card = (MaterialCardView) LayoutInflater.from(getContext())
                    .inflate(R.layout.item_restaurante_selector, containerLayout, false);

            TextView tvInicial = card.findViewById(R.id.tvInicial);
            TextView tvNombre = card.findViewById(R.id.tvNombre);
            TextView tvEstado = card.findViewById(R.id.tvEstado);

            tvInicial.setText(r.getNombre() != null ? String.valueOf(r.getNombre().charAt(0)).toUpperCase() : "?");
            tvNombre.setText(r.getNombre());
            tvEstado.setText(r.getCiudad() != null ? r.getCiudad() : "Activo");
            tvEstado.setTextColor(getResources().getColor(R.color.estado_confirmada));

            card.setOnClickListener(v -> {
                if (listener != null) listener.onRestauranteSelected(r);
                dismiss();
            });

            containerLayout.addView(card);
        }

        MaterialButton btnAdmin = view.findViewById(R.id.btnAdminRestaurantes);
        btnAdmin.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Administrar restaurantes", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        return view;
    }
}
