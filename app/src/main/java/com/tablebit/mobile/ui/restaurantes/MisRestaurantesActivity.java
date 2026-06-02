package com.tablebit.mobile.ui.restaurantes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tablebit.mobile.R;
import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.session.SessionManager;
import com.tablebit.mobile.ui.BaseActivity;
import com.tablebit.mobile.ui.mesas.MesasActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisRestaurantesActivity extends BaseActivity {

    private RecyclerView rvRestaurantes;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private RestauranteAdapter adapter;
    private List<Restaurante> restauranteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante_list);

        initViews();
        loadMisRestaurantes();
    }

    private void initViews() {
        rvRestaurantes = findViewById(R.id.rvRestaurantes);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);

        rvRestaurantes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RestauranteAdapter(restauranteList, this::onRestauranteClick);
        rvRestaurantes.setAdapter(adapter);

        setupToolbarWithBack(getString(R.string.menu_mis_restaurantes));
    }

    private void loadMisRestaurantes() {
        showLoading(true);
        SessionManager sm = new SessionManager(this);

        RetrofitClient.getInstance(sm.getTokenManager()).getApiService().getMisRestaurantes()
                .enqueue(new Callback<List<Restaurante>>() {
                    @Override
                    public void onResponse(Call<List<Restaurante>> call, Response<List<Restaurante>> response) {
                        showLoading(false);
                        if (response.isSuccessful() && response.body() != null) {
                            restauranteList.clear();
                            restauranteList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                            tvEmpty.setVisibility(restauranteList.isEmpty() ? View.VISIBLE : View.GONE);
                        } else {
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Restaurante>> call, Throwable t) {
                        showLoading(false);
                        tvEmpty.setText(R.string.error_red);
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void onRestauranteClick(Restaurante restaurante) {
        Intent intent = new Intent(this, MesasActivity.class);
        intent.putExtra("restaurante_id", restaurante.getId());
        intent.putExtra("restaurante_nombre", restaurante.getNombre());
        startActivity(intent);
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvRestaurantes.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
