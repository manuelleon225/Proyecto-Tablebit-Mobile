package com.tablebit.mobile.ui.admin;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HorariosRestauranteActivity extends AppCompatActivity {

    private int restauranteId;
    private SessionManager sessionManager;
    private LinearLayout containerHorarios;
    private MaterialButton btnGuardar;
    private final List<Map<String, Object>> horariosList = new ArrayList<>();
    private final String[] dias = {"Lunes", "Martes", "Mi\u00e9rcoles", "Jueves", "Viernes", "S\u00e1bado", "Domingo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios_restaurante);

        restauranteId = getIntent().getIntExtra("restaurante_id", -1);
        sessionManager = new SessionManager(this);
        containerHorarios = findViewById(R.id.containerHorarios);
        btnGuardar = findViewById(R.id.btnGuardarHorarios);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());
        btnGuardar.setOnClickListener(v -> guardarHorarios());

        cargarHorarios();
    }

    private void cargarHorarios() {
        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .getHorarios(restauranteId)
                .enqueue(new Callback<List<Map<String, Object>>>() {
                    @Override
                    public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                        Log.e("HORARIOS", "C\u00f3digo: " + response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            Log.e("HORARIOS", "Cantidad: " + response.body().size());
                            for (Map<String, Object> h : response.body()) {
                                Log.e("HORARIOS", "Dia=" + h.get("day_of_week") + " open=" + h.get("open_time") + " close=" + h.get("close_time") + " closed=" + h.get("is_closed"));
                            }
                            horariosList.clear();
                            horariosList.addAll(response.body());
                            renderizarHorarios();
                        } else {
                            try {
                                if (response.errorBody() != null) Log.e("HORARIOS", "Error: " + response.errorBody().string());
                            } catch (Exception ignored) {}
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {}
                });
    }

    private void renderizarHorarios() {
        containerHorarios.removeAllViews();
        for (int i = 0; i < 7; i++) {
            boolean hasData = i < horariosList.size();
            Map<String, Object> h = hasData ? horariosList.get(i) : new HashMap<>();
            boolean isClosed = Boolean.TRUE.equals(h.get("is_closed"));

            com.google.android.material.card.MaterialCardView card = new com.google.android.material.card.MaterialCardView(this);
            card.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            card.setRadius(12);
            card.setContentPadding(16, 12, 16, 12);
            android.view.ViewGroup.MarginLayoutParams mp = (android.view.ViewGroup.MarginLayoutParams) card.getLayoutParams();
            mp.bottomMargin = 8;

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.VERTICAL);

            TextView tvDia = new TextView(this);
            tvDia.setText(dias[i]);
            tvDia.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_TitleMedium);

            LinearLayout switches = new LinearLayout(this);
            switches.setOrientation(LinearLayout.HORIZONTAL);
            switches.setPadding(0, 8, 0, 0);

            MaterialSwitch swActivo = new MaterialSwitch(this);
            swActivo.setText("Abierto");
            swActivo.setChecked(!isClosed);

            String openTime = hasData && h.get("open_time") != null ? h.get("open_time").toString().substring(0, 5) : "";
            String closeTime = hasData && h.get("close_time") != null ? h.get("close_time").toString().substring(0, 5) : "";

            com.google.android.material.card.MaterialCardView cardTimeApertura = new com.google.android.material.card.MaterialCardView(this);
            cardTimeApertura.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            cardTimeApertura.setRadius(8);
            cardTimeApertura.setContentPadding(12, 8, 12, 8);
            cardTimeApertura.setCardBackgroundColor(getResources().getColor(R.color.surface_variant));
            String aperturaText = isClosed ? "Cerrado" : (openTime.isEmpty() ? "09:00" : openTime);
            Log.e("HORARIOS_UI", "Dia=" + i + " isClosed=" + isClosed + " openTime='" + openTime + "' aperturaText='" + aperturaText + "'");
            TextView tvApertura = new TextView(this);
            tvApertura.setText(aperturaText);
            tvApertura.setTextColor(getResources().getColor(R.color.text_primary));
            tvApertura.setTextSize(18);
            tvApertura.setOnClickListener(v -> {
                if (!swActivo.isChecked()) return;
                String[] parts = tvApertura.getText().toString().split(":");
                int hour = parts.length > 0 ? Integer.parseInt(parts[0]) : 9;
                int minute = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
                new TimePickerDialog(this, (view, hr, min) ->
                        tvApertura.setText(String.format("%02d:%02d", hr, min)), hour, minute, true).show();
            });
            cardTimeApertura.addView(tvApertura);

            String cierreText = isClosed ? "Cerrado" : (closeTime.isEmpty() ? "22:00" : closeTime);
            com.google.android.material.card.MaterialCardView cardTimeCierre = new com.google.android.material.card.MaterialCardView(this);
            cardTimeCierre.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            cardTimeCierre.setRadius(8);
            cardTimeCierre.setContentPadding(12, 8, 12, 8);
            cardTimeCierre.setCardBackgroundColor(getResources().getColor(R.color.surface_variant));
            TextView tvCierre = new TextView(this);
            tvCierre.setText(cierreText);
            tvCierre.setTextColor(getResources().getColor(R.color.text_primary));
            tvCierre.setTextSize(18);
            tvCierre.setOnClickListener(v -> {
                if (!swActivo.isChecked()) return;
                String[] parts = tvCierre.getText().toString().split(":");
                int hour = parts.length > 0 ? Integer.parseInt(parts[0]) : 22;
                int minute = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
                new TimePickerDialog(this, (view, hr, min) ->
                        tvCierre.setText(String.format("%02d:%02d", hr, min)), hour, minute, true).show();
            });
            cardTimeCierre.addView(tvCierre);

            switches.addView(cardTimeApertura);
            switches.addView(cardTimeCierre);
            switches.addView(swActivo);

            row.addView(tvDia);
            row.addView(switches);
            card.addView(row);
            containerHorarios.addView(card);
        }
    }

    private void guardarHorarios() {
        List<Map<String, Object>> updated = new ArrayList<>();
        for (int i = 0; i < containerHorarios.getChildCount(); i++) {
            com.google.android.material.card.MaterialCardView card = (com.google.android.material.card.MaterialCardView) containerHorarios.getChildAt(i);
            LinearLayout row = (LinearLayout) card.getChildAt(0);
            LinearLayout switches = (LinearLayout) row.getChildAt(1);

            com.google.android.material.card.MaterialCardView cardA = (com.google.android.material.card.MaterialCardView) switches.getChildAt(0);
            com.google.android.material.card.MaterialCardView cardC = (com.google.android.material.card.MaterialCardView) switches.getChildAt(1);
            MaterialSwitch sw = (MaterialSwitch) switches.getChildAt(2);

            String open = ((TextView) cardA.getChildAt(0)).getText().toString().trim();
            String close = ((TextView) cardC.getChildAt(0)).getText().toString().trim();

            Map<String, Object> h = new HashMap<>();
            h.put("day_of_week", i);
            h.put("open_time", open.isEmpty() ? null : open + ":00");
            h.put("close_time", close.isEmpty() ? null : close + ":00");
            h.put("is_closed", !sw.isChecked());
            updated.add(h);
        }

        btnGuardar.setEnabled(false);
        btnGuardar.setText("Guardando...");

        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .updateHorarios(restauranteId, updated)
                .enqueue(new Callback<List<Map<String, Object>>>() {
                    @Override
                    public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                        btnGuardar.setEnabled(true);
                        btnGuardar.setText("Guardar horarios");
                        if (response.isSuccessful()) {
                            Snackbar.make(btnGuardar, "Horarios actualizados", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(btnGuardar, "Error al guardar", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                        btnGuardar.setEnabled(true);
                        btnGuardar.setText("Guardar horarios");
                        Snackbar.make(btnGuardar, "Error de conexi\u00f3n", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
}
