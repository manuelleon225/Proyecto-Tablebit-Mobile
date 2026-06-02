package com.tablebit.mobile.ui.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tablebit.mobile.R;
import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.CalendarioResponse;
import com.tablebit.mobile.data.model.Reserva;
import com.tablebit.mobile.data.model.ReservasResponse;
import com.tablebit.mobile.session.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarioReservasActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView rvReservas;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private ReservaAdminAdapter adapter;
    private final List<Reserva> reservasDelDia = new ArrayList<>();
    private SessionManager sessionManager;
    private int restauranteId;
    private String fechaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_reservas);

        restauranteId = getIntent().getIntExtra("restaurante_id", -1);
        sessionManager = new SessionManager(this);

        initViews();
        cargarEventosCalendario();
    }

    private void initViews() {
        calendarView = findViewById(R.id.calendarView);
        rvReservas = findViewById(R.id.rvReservas);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);

        rvReservas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReservaAdminAdapter(reservasDelDia, new ReservaAdminAdapter.OnAccionListener() {
            @Override
            public void onCheckIn(Reserva reserva) {
                Toast.makeText(CalendarioReservasActivity.this, "Check-in: " + reserva.getCliente().getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelar(Reserva reserva) {
                cancelarReserva(reserva.getId());
            }
        });
        rvReservas.setAdapter(adapter);

        Calendar cal = Calendar.getInstance();
        fechaSeleccionada = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.getTime());

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            fechaSeleccionada = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            cargarReservasDelDia();
        });
    }

    private void cargarEventosCalendario() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        cal.set(year, month, 1);
        String inicio = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.getTime());
        cal.set(year, month, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String fin = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.getTime());

        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .getCalendario(restauranteId, inicio, fin)
                .enqueue(new Callback<CalendarioResponse>() {
                    @Override
                    public void onResponse(Call<CalendarioResponse> call, Response<CalendarioResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Calendar events loaded, now load today's reservations
                            cargarReservasDelDia();
                        }
                    }

                    @Override
                    public void onFailure(Call<CalendarioResponse> call, Throwable t) {
                        Toast.makeText(CalendarioReservasActivity.this, R.string.error_red, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cargarReservasDelDia() {
        showLoading(true);

        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .getReservasAdmin(restauranteId, fechaSeleccionada)
                .enqueue(new Callback<ReservasResponse>() {
                    @Override
                    public void onResponse(Call<ReservasResponse> call, Response<ReservasResponse> response) {
                        showLoading(false);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            reservasDelDia.clear();
                            reservasDelDia.addAll(response.body().getData());
                            adapter.updateData(reservasDelDia);
                            tvEmpty.setVisibility(reservasDelDia.isEmpty() ? View.VISIBLE : View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ReservasResponse> call, Throwable t) {
                        showLoading(false);
                        tvEmpty.setVisibility(View.VISIBLE);
                        tvEmpty.setText(R.string.error_red);
                    }
                });
    }

    private void cancelarReserva(int reservaId) {
        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .cancelarReserva(reservaId)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        Toast.makeText(CalendarioReservasActivity.this, R.string.reserva_cancelada, Toast.LENGTH_SHORT).show();
                        cargarReservasDelDia();
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast.makeText(CalendarioReservasActivity.this, R.string.error_red, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvReservas.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
