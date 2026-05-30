package com.tablebit.mobile.ui.mesas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tablebit.mobile.R;
import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.data.model.Mesa;
import com.tablebit.mobile.data.repository.MesaRepository;
import com.tablebit.mobile.session.TokenManager;
import com.tablebit.mobile.ui.reservas.CrearReservaActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MesasActivity extends AppCompatActivity {

    private RecyclerView rvMesas;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private MesaAdapter adapter;
    private List<Mesa> mesaList = new ArrayList<>();
    private int restauranteId;
    private String restauranteNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas);

        restauranteId = getIntent().getIntExtra("restaurante_id", -1);
        restauranteNombre = getIntent().getStringExtra("restaurante_nombre");

        TokenManager tokenManager = new TokenManager(this);
        MesaRepository repository = new MesaRepository(tokenManager);

        initViews();
        loadMesas(repository);
    }

    private void initViews() {
        rvMesas = findViewById(R.id.rvMesas);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);

        rvMesas.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MesaAdapter(mesaList, this::onReservarClick);
        rvMesas.setAdapter(adapter);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());
    }

    private void onReservarClick(Mesa mesa) {
        Intent intent = new Intent(this, CrearReservaActivity.class);
        intent.putExtra("restaurante_id", restauranteId);
        intent.putExtra("restaurante_nombre", restauranteNombre);
        intent.putExtra("mesa_id", mesa.getId());
        intent.putExtra("mesa_numero", mesa.getNumeroMesa());
        startActivity(intent);
    }

    private void loadMesas(MesaRepository repository) {
        showLoading(true);

        repository.getMesasByRestaurante(restauranteId).enqueue(new Callback<ApiResponse<List<Mesa>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Mesa>>> call, Response<ApiResponse<List<Mesa>>> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    mesaList.clear();
                    mesaList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    tvEmpty.setVisibility(mesaList.isEmpty() ? View.VISIBLE : View.GONE);
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Mesa>>> call, Throwable t) {
                showLoading(false);
                tvEmpty.setText(R.string.error_red);
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvMesas.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
