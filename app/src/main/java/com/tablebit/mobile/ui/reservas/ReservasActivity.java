package com.tablebit.mobile.ui.reservas;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tablebit.mobile.R;
import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.data.model.Reserva;
import com.tablebit.mobile.data.repository.ReservaRepository;
import com.tablebit.mobile.session.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservasActivity extends AppCompatActivity {

    private RecyclerView rvReservas;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private ReservaAdapter adapter;
    private List<Reserva> reservaList = new ArrayList<>();
    private ReservaRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        TokenManager tokenManager = new TokenManager(this);
        repository = new ReservaRepository(tokenManager);

        initViews();
        loadReservas();
    }

    private void initViews() {
        rvReservas = findViewById(R.id.rvReservas);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);

        rvReservas.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ReservaAdapter(reservaList, this::onCancelarClick);
        rvReservas.setAdapter(adapter);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());
    }

    private void onCancelarClick(Reserva reserva) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.btn_cancelar_reserva)
                .setMessage(R.string.confirmar_cancelar)
                .setPositiveButton("Sí", (dialog, which) -> cancelarReserva(reserva.getId()))
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelarReserva(int reservaId) {
        repository.cancelarReserva(reservaId).enqueue(new Callback<ApiResponse<Reserva>>() {
            @Override
            public void onResponse(Call<ApiResponse<Reserva>> call, Response<ApiResponse<Reserva>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReservasActivity.this, R.string.reserva_cancelada, Toast.LENGTH_SHORT).show();
                    loadReservas();
                } else {
                    Toast.makeText(ReservasActivity.this, R.string.error_general, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Reserva>> call, Throwable t) {
                Toast.makeText(ReservasActivity.this, R.string.error_red, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadReservas() {
        showLoading(true);

        repository.getMisReservas().enqueue(new Callback<ApiResponse<List<Reserva>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Reserva>>> call, Response<ApiResponse<List<Reserva>>> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    reservaList.clear();
                    reservaList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    tvEmpty.setVisibility(reservaList.isEmpty() ? View.VISIBLE : View.GONE);
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Reserva>>> call, Throwable t) {
                showLoading(false);
                tvEmpty.setText(R.string.error_red);
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvReservas.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
