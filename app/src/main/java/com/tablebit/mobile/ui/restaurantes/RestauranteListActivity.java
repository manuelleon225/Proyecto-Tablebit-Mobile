package com.tablebit.mobile.ui.restaurantes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tablebit.mobile.R;
import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.ui.BaseActivity;
import com.tablebit.mobile.ui.viewmodel.RestauranteViewModel;

public class RestauranteListActivity extends BaseActivity {

    private RecyclerView rvRestaurantes;
    private View progressBar, tvEmpty;
    private RestauranteAdapter adapter;
    private RestauranteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante_list);

        viewModel = new ViewModelProvider(this).get(RestauranteViewModel.class);
        initViews();
        observeViewModel();
        viewModel.loadRestaurantes();
    }

    private void initViews() {
        rvRestaurantes = findViewById(R.id.rvRestaurantes);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);

        rvRestaurantes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RestauranteAdapter(viewModel.getRestaurantes().getValue(), this::onRestauranteClick);
        rvRestaurantes.setAdapter(adapter);

        setupToolbarWithBack(getString(R.string.titulo_restaurantes));
    }

    private void observeViewModel() {
        viewModel.getLoading().observe(this, loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            rvRestaurantes.setVisibility(loading ? View.GONE : View.VISIBLE);
        });

        viewModel.getRestaurantes().observe(this, restaurantes -> {
            adapter = new RestauranteAdapter(restaurantes, this::onRestauranteClick);
            rvRestaurantes.setAdapter(adapter);
            tvEmpty.setVisibility(restaurantes.isEmpty() ? View.VISIBLE : View.GONE);
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                tvEmpty.setVisibility(View.VISIBLE);
                if (tvEmpty instanceof android.widget.TextView)
                    ((android.widget.TextView) tvEmpty).setText(error);
            }
        });
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
}
