package com.tablebit.mobile.ui.restaurantes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.tablebit.mobile.R;
import com.tablebit.mobile.ui.BaseActivity;
import com.tablebit.mobile.ui.mesas.MesasActivity;

public class RestauranteDetalleActivity extends BaseActivity {

    private int restauranteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante_detalle);

        restauranteId = getIntent().getIntExtra("restaurante_id", -1);
        String nombre = getIntent().getStringExtra("restaurante_nombre");
        String direccion = getIntent().getStringExtra("restaurante_direccion");
        String telefono = getIntent().getStringExtra("restaurante_telefono");
        String descripcion = getIntent().getStringExtra("restaurante_descripcion");

        initViews(nombre, direccion, telefono, descripcion);
    }

    private void initViews(String nombre, String direccion, String telefono, String descripcion) {
        setupToolbarWithBack(getString(R.string.detalle_restaurante));

        TextView tvNombre = findViewById(R.id.tvNombre);
        TextView tvDireccion = findViewById(R.id.tvDireccion);
        TextView tvTelefono = findViewById(R.id.tvTelefono);
        TextView tvDescripcion = findViewById(R.id.tvDescripcion);
        MaterialButton btnVerMesas = findViewById(R.id.btnVerMesas);

        tvNombre.setText(nombre);
        tvDireccion.setText(direccion);
        tvTelefono.setText(telefono);
        tvDescripcion.setText(descripcion != null ? descripcion : "");

        btnVerMesas.setOnClickListener(v -> {
            Intent intent = new Intent(this, MesasActivity.class);
            intent.putExtra("restaurante_id", restauranteId);
            intent.putExtra("restaurante_nombre", nombre);
            startActivity(intent);
        });
    }
}
