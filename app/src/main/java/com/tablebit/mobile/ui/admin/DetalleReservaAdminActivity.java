package com.tablebit.mobile.ui.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.session.SessionManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleReservaAdminActivity extends AppCompatActivity {

    private int reservaId;
    private SessionManager sessionManager;
    private TextView tvCliente, tvEmail, tvTelefono, tvRestaurante, tvMesa, tvFechaHora, tvPersonas, tvEstado, tvNotas;
    private MaterialButton btnConfirmar, btnCheckIn, btnCancelar;
    private String estadoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_reserva_admin);

        reservaId = getIntent().getIntExtra("reserva_id", -1);
        sessionManager = new SessionManager(this);

        tvCliente = findViewById(R.id.tvCliente);
        tvEmail = findViewById(R.id.tvEmail);
        tvTelefono = findViewById(R.id.tvTelefono);
        tvRestaurante = findViewById(R.id.tvRestaurante);
        tvMesa = findViewById(R.id.tvMesa);
        tvFechaHora = findViewById(R.id.tvFechaHora);
        tvPersonas = findViewById(R.id.tvPersonas);
        tvEstado = findViewById(R.id.tvEstado);
        tvNotas = findViewById(R.id.tvNotas);
        btnConfirmar = findViewById(R.id.btnConfirmar);
        btnCheckIn = findViewById(R.id.btnCheckIn);
        btnCancelar = findViewById(R.id.btnCancelar);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        btnConfirmar.setOnClickListener(v -> cambiarEstado("confirmada"));
        btnCheckIn.setOnClickListener(v -> cambiarEstado("completada"));
        btnCancelar.setOnClickListener(v -> cambiarEstado("cancelada"));

        cargarDetalle();
    }

    private void cargarDetalle() {
        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .getReservaDetail(reservaId)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            actualizarUI(response.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {}
                });
    }

    @SuppressWarnings("unchecked")
    private void actualizarUI(Map<String, Object> data) {
        Object clienteObj = data.get("cliente");
        if (clienteObj instanceof Map) {
            Map<String, Object> c = (Map<String, Object>) clienteObj;
            tvCliente.setText((String) c.get("name"));
            tvEmail.setText((String) c.get("email"));
            tvTelefono.setText((String) c.get("telefono"));
        }
        tvRestaurante.setText("Restaurante ID: " + data.get("restaurante_id"));
        tvMesa.setText("Mesa #" + data.get("mesa_id"));
        tvFechaHora.setText(data.get("fecha") + " \u00b7 " + data.get("hora"));
        tvPersonas.setText(data.get("cantidad_personas") + " personas");
        estadoActual = (String) data.get("estado");
        tvEstado.setText("Estado: " + (estadoActual != null ? estadoActual : "desconocido"));
        Object notas = data.get("notas");
        tvNotas.setText(notas != null ? "Notas: " + notas : "");

        actualizarBotones();
    }

    private void actualizarBotones() {
        boolean isPendiente = "pendiente".equalsIgnoreCase(estadoActual);
        boolean isConfirmada = "confirmada".equalsIgnoreCase(estadoActual);
        btnConfirmar.setVisibility(isPendiente ? View.VISIBLE : View.GONE);
        btnCheckIn.setVisibility(isConfirmada ? View.VISIBLE : View.GONE);
        btnCancelar.setVisibility(isPendiente || isConfirmada ? View.VISIBLE : View.GONE);
    }

    private void cambiarEstado(String nuevoEstado) {
        Map<String, Object> body = new HashMap<>();
        body.put("estado", nuevoEstado);

        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .cambiarEstadoReserva(reservaId, body)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful()) {
                            Snackbar.make(btnConfirmar, "Reserva " + nuevoEstado, Snackbar.LENGTH_SHORT).show();
                            estadoActual = nuevoEstado;
                            actualizarBotones();
                            setResult(RESULT_OK);
                        } else {
                            Snackbar.make(btnConfirmar, "Error al actualizar", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Snackbar.make(btnConfirmar, "Error de conexi\u00f3n", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
}
