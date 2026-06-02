package com.tablebit.mobile.ui.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.Reserva;
import com.tablebit.mobile.data.model.ReservasResponse;
import com.tablebit.mobile.session.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListadoReservasActivity extends AppCompatActivity {

    private RecyclerView rvReservas;
    private ProgressBar progressBar;
    private TextView tvEmpty, tvTotal, tvConfirmadas, tvPendientes, tvCanceladas;
    private View statsRow;
    private ChipGroup chipGroupFiltros;
    private ReservaAdminAdapter adapter;
    private final List<Reserva> reservasOriginales = new ArrayList<>();
    private final List<Reserva> reservasFiltradas = new ArrayList<>();
    private SessionManager sessionManager;
    private int restauranteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_reservas);

        restauranteId = getIntent().getIntExtra("restaurante_id", -1);
        sessionManager = new SessionManager(this);

        initViews();
        cargarReservas();
    }

    private void initViews() {
        rvReservas = findViewById(R.id.rvReservas);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);
        statsRow = findViewById(R.id.statsRow);
        tvTotal = findViewById(R.id.tvTotal);
        tvConfirmadas = findViewById(R.id.tvConfirmadas);
        tvPendientes = findViewById(R.id.tvPendientes);
        tvCanceladas = findViewById(R.id.tvCanceladas);
        chipGroupFiltros = findViewById(R.id.chipGroupFiltros);

        rvReservas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReservaAdminAdapter(reservasFiltradas, new ReservaAdminAdapter.OnAccionListener() {
            @Override
            public void onCheckIn(Reserva reserva) {
                Toast.makeText(ListadoReservasActivity.this, "Check-in: " + reserva.getCliente().getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelar(Reserva reserva) {
                cancelarReserva(reserva.getId());
            }
        });
        rvReservas.setAdapter(adapter);

        chipGroupFiltros.setOnCheckedStateChangeListener((group, checkedIds) -> {
            aplicarFiltro();
        });
    }

    private void cargarReservas() {
        showLoading(true);

        String hoy = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());

        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .getReservasAdmin(restauranteId, hoy)
                .enqueue(new Callback<ReservasResponse>() {
                    @Override
                    public void onResponse(Call<ReservasResponse> call, Response<ReservasResponse> response) {
                        showLoading(false);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            reservasOriginales.clear();
                            reservasOriginales.addAll(response.body().getData());
                            actualizarStats();
                            aplicarFiltro();
                        } else {
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ReservasResponse> call, Throwable t) {
                        showLoading(false);
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void actualizarStats() {
        statsRow.setVisibility(View.VISIBLE);
        int total = reservasOriginales.size();
        int confirmadas = 0, pendientes = 0, canceladas = 0;
        for (Reserva r : reservasOriginales) {
            if (r.getEstado() == null) continue;
            switch (r.getEstado().toLowerCase()) {
                case "confirmada": confirmadas++; break;
                case "pendiente": pendientes++; break;
                case "cancelada": canceladas++; break;
            }
        }
        tvTotal.setText(String.valueOf(total));
        tvConfirmadas.setText(String.valueOf(confirmadas));
        tvPendientes.setText(String.valueOf(pendientes));
        tvCanceladas.setText(String.valueOf(canceladas));
    }

    private void aplicarFiltro() {
        reservasFiltradas.clear();
        int id = chipGroupFiltros.getCheckedChipId();

        if (id == R.id.chipTodas || id == View.NO_ID) {
            reservasFiltradas.addAll(reservasOriginales);
        } else {
            String filtro = "";
            if (id == R.id.chipConfirmadas) filtro = "confirmada";
            else if (id == R.id.chipPendientes) filtro = "pendiente";
            else if (id == R.id.chipCanceladas) filtro = "cancelada";

            for (Reserva r : reservasOriginales) {
                if (r.getEstado() != null && r.getEstado().equalsIgnoreCase(filtro)) {
                    reservasFiltradas.add(r);
                }
            }
        }

        adapter.updateData(reservasFiltradas);
        tvEmpty.setVisibility(reservasFiltradas.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void cancelarReserva(int reservaId) {
        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .cancelarReserva(reservaId)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        Toast.makeText(ListadoReservasActivity.this, R.string.reserva_cancelada, Toast.LENGTH_SHORT).show();
                        cargarReservas();
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast.makeText(ListadoReservasActivity.this, R.string.error_red, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvReservas.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
