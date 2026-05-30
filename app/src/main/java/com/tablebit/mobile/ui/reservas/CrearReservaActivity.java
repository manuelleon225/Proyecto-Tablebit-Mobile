package com.tablebit.mobile.ui.reservas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.data.model.Reserva;
import com.tablebit.mobile.data.repository.ReservaRepository;
import com.tablebit.mobile.session.TokenManager;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearReservaActivity extends AppCompatActivity {

    private TextInputEditText etFecha, etHora, etPersonas;
    private MaterialButton btnConfirmar;
    private int restauranteId, mesaId;
    private String restauranteNombre;
    private int mesaNumero;
    private ReservaRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_reserva);

        restauranteId = getIntent().getIntExtra("restaurante_id", -1);
        mesaId = getIntent().getIntExtra("mesa_id", -1);
        restauranteNombre = getIntent().getStringExtra("restaurante_nombre");
        mesaNumero = getIntent().getIntExtra("mesa_numero", -1);

        TokenManager tokenManager = new TokenManager(this);
        repository = new ReservaRepository(tokenManager);

        initViews();
    }

    private void initViews() {
        etFecha = findViewById(R.id.etFecha);
        etHora = findViewById(R.id.etHora);
        etPersonas = findViewById(R.id.etPersonas);
        btnConfirmar = findViewById(R.id.btnConfirmarReserva);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        etFecha.setOnClickListener(v -> showDatePicker());
        etFecha.setKeyListener(null);

        etHora.setOnClickListener(v -> showTimePicker());
        etHora.setKeyListener(null);

        btnConfirmar.setOnClickListener(v -> crearReserva());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog picker = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String fecha = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    etFecha.setText(fecha);
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        picker.show();
    }

    private void showTimePicker() {
        Calendar cal = Calendar.getInstance();
        TimePickerDialog picker = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    String hora = String.format("%02d:%02d", hourOfDay, minute);
                    etHora.setText(hora);
                },
                cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        picker.show();
    }

    private void crearReserva() {
        String fecha = etFecha.getText().toString().trim();
        String hora = etHora.getText().toString().trim();
        String personasStr = etPersonas.getText().toString().trim();

        if (fecha.isEmpty() || hora.isEmpty() || personasStr.isEmpty()) {
            Toast.makeText(this, R.string.campo_requerido, Toast.LENGTH_SHORT).show();
            return;
        }

        int personas = Integer.parseInt(personasStr);

        btnConfirmar.setEnabled(false);
        btnConfirmar.setText(R.string.cargando);

        repository.crearReserva(restauranteId, mesaId, fecha, hora, personas)
                .enqueue(new Callback<ApiResponse<Reserva>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Reserva>> call, Response<ApiResponse<Reserva>> response) {
                        btnConfirmar.setEnabled(true);
                        btnConfirmar.setText(R.string.btn_confirmar_reserva);

                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(CrearReservaActivity.this, R.string.reserva_creada, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(CrearReservaActivity.this, R.string.error_general, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Reserva>> call, Throwable t) {
                        btnConfirmar.setEnabled(true);
                        btnConfirmar.setText(R.string.btn_confirmar_reserva);
                        Toast.makeText(CrearReservaActivity.this, R.string.error_red, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
