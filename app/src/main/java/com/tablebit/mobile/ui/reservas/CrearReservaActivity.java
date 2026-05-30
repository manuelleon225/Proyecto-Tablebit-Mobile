package com.tablebit.mobile.ui.reservas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.tablebit.mobile.R;
import com.tablebit.mobile.ui.BaseActivity;
import com.tablebit.mobile.ui.viewmodel.ReservaViewModel;

import java.util.Calendar;

public class CrearReservaActivity extends BaseActivity {

    private TextInputEditText etFecha, etHora, etPersonas;
    private MaterialButton btnConfirmar;
    private int restauranteId, mesaId;
    private ReservaViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_reserva);

        restauranteId = getIntent().getIntExtra("restaurante_id", -1);
        mesaId = getIntent().getIntExtra("mesa_id", -1);

        viewModel = new ViewModelProvider(this).get(ReservaViewModel.class);
        initViews();
        observeViewModel();
    }

    private void initViews() {
        etFecha = findViewById(R.id.etFecha);
        etHora = findViewById(R.id.etHora);
        etPersonas = findViewById(R.id.etPersonas);
        btnConfirmar = findViewById(R.id.btnConfirmarReserva);

        setupToolbarWithBack(getString(R.string.titulo_crear_reserva));

        etFecha.setOnClickListener(v -> showDatePicker());
        etFecha.setKeyListener(null);

        etHora.setOnClickListener(v -> showTimePicker());
        etHora.setKeyListener(null);

        btnConfirmar.setOnClickListener(v -> crearReserva());
    }

    private void observeViewModel() {
        viewModel.getLoading().observe(this, loading -> {
            btnConfirmar.setEnabled(!loading);
            btnConfirmar.setText(loading ? R.string.cargando : R.string.btn_confirmar_reserva);
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });

        viewModel.getReservaCreada().observe(this, reserva -> {
            if (reserva != null) {
                Toast.makeText(this, R.string.reserva_creada, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
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
        viewModel.crearReserva(restauranteId, mesaId, fecha, hora, personas);
    }
}
