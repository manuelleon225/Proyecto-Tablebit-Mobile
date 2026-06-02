package com.tablebit.mobile.ui.cliente;

import com.tablebit.mobile.data.api.NetworkConfig;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.session.SessionManager;
import com.tablebit.mobile.ui.viewmodel.ReservaViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcesoReservaActivity extends AppCompatActivity {

    private int restauranteId;
    private String restauranteNombre;
    private SessionManager sessionManager;
    private ReservaViewModel viewModel;

    private int currentStep = 1;
    private String fechaSeleccionada;
    private String horaSeleccionada;
    private int personasSeleccionadas = 2;

    // Views
    private ShapeableImageView ivRestaurante;
    private TextView tvNombre, tvInfo;
    private View stepFecha, stepHora, stepPersonas, stepResumen;
    private TextView tvStepLabel, tvStepSub, tvFechaLabel;
    private CalendarView calendarView;
    private ChipGroup chipHoras;
    private TextView tvCantidadPersonas;
    private TextView tvResumenRest, tvResumenFecha, tvResumenHora, tvResumenPersonas;
    private MaterialButton btnAtras, btnSiguiente, btnConfirmar, btnMenos, btnMas;
    private View btnClose;
    private LinearLayout stepIndicator;
    private TextView s1, s2, s3;
    private View l1, l2;
    private FrameLayout successOverlay;
    private TextView tvConfirmNumero, tvConfirmDetalle;
    private MaterialButton btnVerReservas;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_reserva);

        restauranteId = getIntent().getIntExtra("restaurante_id", -1);
        restauranteNombre = getIntent().getStringExtra("restaurante_nombre");
        sessionManager = new SessionManager(this);
        viewModel = new ViewModelProvider(this).get(ReservaViewModel.class);

        initViews();
        observeViewModel();
        loadRestauranteInfo();
        actualizarStep();
    }

    private void initViews() {
        rootView = findViewById(android.R.id.content);
        ivRestaurante = findViewById(R.id.ivRestauranteReserva);
        tvNombre = findViewById(R.id.tvNombreReserva);
        tvInfo = findViewById(R.id.tvInfoReserva);
        btnClose = findViewById(R.id.btnCloseReserva);

        stepIndicator = findViewById(R.id.stepIndicator);
        s1 = findViewById(R.id.step1);
        s2 = findViewById(R.id.step2);
        s3 = findViewById(R.id.step3);
        l1 = findViewById(R.id.line1);
        l2 = findViewById(R.id.line2);

        tvStepLabel = findViewById(R.id.tvStepLabel);
        tvStepSub = findViewById(R.id.tvStepSubLabel);

        stepFecha = findViewById(R.id.stepFechaContainer);
        stepHora = findViewById(R.id.stepHoraContainer);
        stepPersonas = findViewById(R.id.stepPersonasContainer);
        stepResumen = findViewById(R.id.stepResumenContainer);

        calendarView = findViewById(R.id.calendarView);
        tvFechaLabel = findViewById(R.id.tvFechaSeleccionada);
        chipHoras = findViewById(R.id.chipGroupHoras);
        tvCantidadPersonas = findViewById(R.id.tvCantidadPersonas);
        tvResumenRest = findViewById(R.id.tvResumenRestaurante);
        tvResumenFecha = findViewById(R.id.tvResumenFecha);
        tvResumenHora = findViewById(R.id.tvResumenHora);
        tvResumenPersonas = findViewById(R.id.tvResumenPersonas);

        btnAtras = findViewById(R.id.btnAtras);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnConfirmar = findViewById(R.id.btnConfirmar);
        btnMenos = findViewById(R.id.btnMenos);
        btnMas = findViewById(R.id.btnMas);

        successOverlay = findViewById(R.id.successOverlay);
        tvConfirmNumero = findViewById(R.id.tvConfirmNumero);
        tvConfirmDetalle = findViewById(R.id.tvConfirmDetalle);
        btnVerReservas = findViewById(R.id.btnVerReservas);

        tvNombre.setText(restauranteNombre);

        btnClose.setOnClickListener(v -> finish());

        calendarView.setMinDate(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        fechaSeleccionada = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.getTime());
        actualizarFechaLabel(cal);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            fechaSeleccionada = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            actualizarFechaLabel(c);
        });

        chipHoras.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                horaSeleccionada = ((Chip) findViewById(checkedIds.get(0))).getText().toString();
            }
        });

        btnMenos.setOnClickListener(v -> {
            if (personasSeleccionadas > 1) {
                personasSeleccionadas--;
                tvCantidadPersonas.setText(String.valueOf(personasSeleccionadas));
            }
        });

        btnMas.setOnClickListener(v -> {
            if (personasSeleccionadas < 20) {
                personasSeleccionadas++;
                tvCantidadPersonas.setText(String.valueOf(personasSeleccionadas));
            }
        });

        btnAtras.setOnClickListener(v -> {
            currentStep--;
            actualizarStep();
        });

        btnSiguiente.setOnClickListener(v -> {
            switch (currentStep) {
                case 1: currentStep = 2; break;
                case 2:
                    if (horaSeleccionada == null) {
                        Snackbar.make(rootView, "Selecciona un horario", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    // Validar hora mínima si es hoy
                    String hoy = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new java.util.Date());
                    if (fechaSeleccionada != null && fechaSeleccionada.equals(hoy)) {
                        String horaActual = new SimpleDateFormat("HH:mm", Locale.US).format(new java.util.Date());
                        if (horaSeleccionada.compareTo(horaActual) < 0) {
                            Snackbar.make(rootView, "No puedes reservar para una hora que ya pas\u00f3", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                    }
                    currentStep = 3;
                    break;
                case 3: currentStep = 4; break;
            }
            actualizarStep();
        });

        btnConfirmar.setOnClickListener(v -> confirmarReserva());
        btnVerReservas.setOnClickListener(v -> {
            startActivity(new Intent(this, MisReservasActivity.class));
            finish();
        });
    }

    private void actualizarFechaLabel(Calendar cal) {
        String dia = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("es", "ES"));
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String mes = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("es", "ES"));
        tvFechaLabel.setText(capitalize(dia) + ", " + day + " de " + mes);
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private void actualizarStep() {
        stepFecha.setVisibility(currentStep == 1 ? View.VISIBLE : View.GONE);
        stepHora.setVisibility(currentStep == 2 ? View.VISIBLE : View.GONE);
        stepPersonas.setVisibility(currentStep == 3 ? View.VISIBLE : View.GONE);
        stepResumen.setVisibility(currentStep == 4 ? View.VISIBLE : View.GONE);

        btnAtras.setVisibility(currentStep > 1 ? View.VISIBLE : View.GONE);
        btnSiguiente.setVisibility(currentStep < 4 ? View.VISIBLE : View.GONE);
        btnConfirmar.setVisibility(currentStep == 4 ? View.VISIBLE : View.GONE);

        String[] labels = {"", "Selecciona la fecha", "Elige el horario", "N\u00famero de personas", "Confirma tu reserva"};
        String[] subs = {"", "\u00bfPara cu\u00e1ndo ser\u00e1 tu visita?", "\u00bfA qu\u00e9 hora prefieres ir?", "\u00bfCu\u00e1ntos comensales?", "Revisa los detalles antes de confirmar"};
        tvStepLabel.setText(labels[currentStep]);
        tvStepSub.setText(subs[currentStep]);

        // Actualizar indicador de pasos
        for (int i = 1; i <= 3; i++) {
            TextView step = i == 1 ? s1 : (i == 2 ? s2 : s3);
            View line = i == 1 ? l1 : l2;
            if (i < currentStep) {
                step.setBackgroundResource(R.drawable.circle_primary);
                step.setTextColor(ContextCompat.getColor(this, R.color.on_primary));
                if (line != null) line.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
            } else if (i == currentStep) {
                step.setBackgroundResource(R.drawable.circle_primary);
                step.setTextColor(ContextCompat.getColor(this, R.color.on_primary));
            } else {
                step.setBackgroundResource(R.drawable.circle_outline);
                step.setTextColor(ContextCompat.getColor(this, R.color.text_disabled));
                if (line != null) line.setBackgroundColor(ContextCompat.getColor(this, R.color.outline));
            }
        }

        // Actualizar resumen
        if (currentStep == 4) {
            tvResumenRest.setText("\uD83C\uDF7D " + restauranteNombre);
            tvResumenFecha.setText("\uD83D\uDCC5 " + tvFechaLabel.getText());
            tvResumenHora.setText("\uD83D\uDD52 " + (horaSeleccionada != null ? horaSeleccionada : ""));
            tvResumenPersonas.setText("\uD83D\uDC65 " + personasSeleccionadas + " personas");
        }
    }

    private void loadRestauranteInfo() {
        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .getRestaurante(restauranteId)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Map<String, Object> data = response.body();
                            Object restObj = data.get("restaurante");
                            if (restObj instanceof Map) {
                                Map<String, Object> r = (Map<String, Object>) restObj;
                                String nombre = (String) r.get("nombre");
                                String tipo = (String) r.get("tipo_comida");
                                String ciudad = (String) r.get("ciudad");
                                tvInfo.setText((tipo != null ? tipo + " \u00b7 " : "") + (ciudad != null ? ciudad : ""));
                                String img = (String) r.get("imagen");
                                if (img != null) {
                                    Glide.with(ProcesoReservaActivity.this).load(NetworkConfig.STORAGE_URL + img)
                                            .transform(new CenterCrop(), new RoundedCorners(8))
                                            .placeholder(android.R.color.darker_gray).into(ivRestaurante);
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {}
                });
    }

    private void observeViewModel() {
        viewModel.getLoading().observe(this, loading -> {
            btnConfirmar.setEnabled(!loading);
            btnConfirmar.setText(loading ? "Reservando..." : "Confirmar Reserva");
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) Snackbar.make(rootView, error, Snackbar.LENGTH_LONG).show();
        });

        viewModel.getReservaCreada().observe(this, creada -> {
            if (Boolean.TRUE.equals(creada)) {
                mostrarConfirmacion();
            }
        });
    }

    private void confirmarReserva() {
        viewModel.crearReserva(restauranteId, fechaSeleccionada, horaSeleccionada, personasSeleccionadas);
    }

    private void mostrarConfirmacion() {
        successOverlay.setVisibility(View.VISIBLE);
        successOverlay.setAlpha(0f);
        successOverlay.animate().alpha(1f).setDuration(400).start();

        String[] parts = fechaSeleccionada.split("-");
        String fecha = parts[2] + "/" + parts[1] + "/" + parts[0];
        tvConfirmDetalle.setText(restauranteNombre + " \u00b7 " + fecha + " \u00b7 " + horaSeleccionada + " \u00b7 " + personasSeleccionadas + " pers.");
    }

    private void mostrarError(String msg) {
        Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG)
                .setAction("Reintentar", v -> confirmarReserva()).show();
    }
}


