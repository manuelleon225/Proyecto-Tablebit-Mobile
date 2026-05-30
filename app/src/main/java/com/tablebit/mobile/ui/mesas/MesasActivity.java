package com.tablebit.mobile.ui.mesas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tablebit.mobile.R;
import com.tablebit.mobile.data.model.Mesa;
import com.tablebit.mobile.ui.BaseActivity;
import com.tablebit.mobile.ui.reservas.CrearReservaActivity;
import com.tablebit.mobile.ui.viewmodel.MesaViewModel;

public class MesasActivity extends BaseActivity {

    private RecyclerView rvMesas;
    private View progressBar, tvEmpty;
    private MesaAdapter adapter;
    private MesaViewModel viewModel;
    private int restauranteId;
    private String restauranteNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas);

        restauranteId = getIntent().getIntExtra("restaurante_id", -1);
        restauranteNombre = getIntent().getStringExtra("restaurante_nombre");

        viewModel = new ViewModelProvider(this).get(MesaViewModel.class);
        initViews();
        observeViewModel();
        viewModel.loadMesas(restauranteId);
    }

    private void initViews() {
        rvMesas = findViewById(R.id.rvMesas);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);

        rvMesas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MesaAdapter(viewModel.getMesas().getValue(), this::onReservarClick);
        rvMesas.setAdapter(adapter);

        setupToolbarWithBack(getString(R.string.titulo_mesas));
    }

    private void observeViewModel() {
        viewModel.getLoading().observe(this, loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            rvMesas.setVisibility(loading ? View.GONE : View.VISIBLE);
        });

        viewModel.getMesas().observe(this, mesas -> {
            adapter = new MesaAdapter(mesas, this::onReservarClick);
            rvMesas.setAdapter(adapter);
            tvEmpty.setVisibility(mesas.isEmpty() ? View.VISIBLE : View.GONE);
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                tvEmpty.setVisibility(View.VISIBLE);
                if (tvEmpty instanceof android.widget.TextView)
                    ((android.widget.TextView) tvEmpty).setText(error);
            }
        });
    }

    private void onReservarClick(Mesa mesa) {
        Intent intent = new Intent(this, CrearReservaActivity.class);
        intent.putExtra("restaurante_id", restauranteId);
        intent.putExtra("restaurante_nombre", restauranteNombre);
        intent.putExtra("mesa_id", mesa.getId());
        intent.putExtra("mesa_numero", mesa.getNumeroMesa());
        startActivity(intent);
    }
}
