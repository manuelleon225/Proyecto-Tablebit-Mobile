package com.tablebit.mobile.ui.restaurantes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.tablebit.mobile.R;
import com.tablebit.mobile.session.SessionManager;
import com.tablebit.mobile.ui.BaseActivity;
import com.tablebit.mobile.ui.mesas.MesasActivity;
import com.tablebit.mobile.ui.reservas.CrearReservaActivity;

public class RestauranteDetalleActivity extends BaseActivity {

    private int restauranteId;
    private String restauranteNombre;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante_detalle);

        sessionManager = new SessionManager(this);
        restauranteId = getIntent().getIntExtra("restaurante_id", -1);
        restauranteNombre = getIntent().getStringExtra("restaurante_nombre");
        String direccion = getIntent().getStringExtra("restaurante_direccion");
        String telefono = getIntent().getStringExtra("restaurante_telefono");
        String descripcion = getIntent().getStringExtra("restaurante_descripcion");

        initViews(restauranteNombre, direccion, telefono, descripcion);
    }

    private void initViews(String nombre, String direccion, String telefono, String descripcion) {
        setupToolbarWithBack(getString(R.string.detalle_restaurante));

        TextView tvNombre = findViewById(R.id.tvNombre);
        TextView tvDireccion = findViewById(R.id.tvDireccion);
        TextView tvTelefono = findViewById(R.id.tvTelefono);
        TextView tvDescripcion = findViewById(R.id.tvDescripcion);
        MaterialButton btnAccion = findViewById(R.id.btnVerMesas);

        tvNombre.setText(nombre);
        tvDireccion.setText(direccion);
        tvTelefono.setText(telefono);
        tvDescripcion.setText(descripcion != null ? descripcion : "");

        if (sessionManager.isAdmin()) {
            btnAccion.setText(R.string.btn_ver_mesas);
            btnAccion.setOnClickListener(v -> {
                Intent intent = new Intent(this, MesasActivity.class);
                intent.putExtra("restaurante_id", restauranteId);
                intent.putExtra("restaurante_nombre", restauranteNombre);
                startActivity(intent);
            });
        } else {
            btnAccion.setText(R.string.btn_reservar);
            btnAccion.setOnClickListener(v -> {
                Intent intent = new Intent(this, CrearReservaActivity.class);
                intent.putExtra("restaurante_id", restauranteId);
                intent.putExtra("restaurante_nombre", restauranteNombre);
                startActivity(intent);
            });
        }
    }
}
