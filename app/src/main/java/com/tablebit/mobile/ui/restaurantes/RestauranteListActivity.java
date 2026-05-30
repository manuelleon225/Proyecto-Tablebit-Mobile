package com.tablebit.mobile.ui.restaurantes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tablebit.mobile.R;
import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.data.repository.RestauranteRepository;
import com.tablebit.mobile.session.SessionManager;
import com.tablebit.mobile.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestauranteListActivity extends BaseActivity {

    private RecyclerView rvRestaurantes;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private RestauranteAdapter adapter;
    private RestauranteRepository repository;
    private List<Restaurante> restauranteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante_list);

        SessionManager sessionManager = new SessionManager(this);
        repository = new RestauranteRepository(sessionManager.getTokenManager());

        initViews();
        loadRestaurantes();
    }

    private void initViews() {
        rvRestaurantes = findViewById(R.id.rvRestaurantes);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);

        rvRestaurantes.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RestauranteAdapter(restauranteList, this::onRestauranteClick);
        rvRestaurantes.setAdapter(adapter);

        setupToolbarWithBack(getString(R.string.titulo_restaurantes));
    }

    private void onRestauranteClick(Restaurante restaurante) {
        Intent intent = new Intent(this, RestauranteDetalleActivity.class);
        intent.putExtra("restaurante_id", restaurante.getId());
        intent.putExtra("restaurante_nombre", restaurante.getNombre());
        intent.putExtra("restaurante_direccion", restaurante.getDireccion());
        intent.putExtra("restaurante_telefono", restaurante.getTelefono());
        intent.putExtra("restaurante_descripcion", restaurante.getDescripcion());
        startActivity(intent);
    }

    private void loadRestaurantes() {
        showLoading(true);

        repository.getRestaurantes().enqueue(new Callback<ApiResponse<List<Restaurante>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Restaurante>>> call, Response<ApiResponse<List<Restaurante>>> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    restauranteList.clear();
                    restauranteList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    tvEmpty.setVisibility(restauranteList.isEmpty() ? View.VISIBLE : View.GONE);
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Restaurante>>> call, Throwable t) {
                showLoading(false);
                tvEmpty.setText(R.string.error_red);
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvRestaurantes.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
