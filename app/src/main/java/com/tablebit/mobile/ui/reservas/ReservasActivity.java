package com.tablebit.mobile.ui.reservas;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tablebit.mobile.R;
import com.tablebit.mobile.data.model.Reserva;
import com.tablebit.mobile.ui.BaseActivity;
import com.tablebit.mobile.ui.viewmodel.ReservaViewModel;

public class ReservasActivity extends BaseActivity {

    private RecyclerView rvReservas;
    private View progressBar, tvEmpty;
    private ReservaAdapter adapter;
    private ReservaViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        viewModel = new ViewModelProvider(this).get(ReservaViewModel.class);
        initViews();
        observeViewModel();
        viewModel.loadReservas();
    }

    private void initViews() {
        rvReservas = findViewById(R.id.rvReservas);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);

        rvReservas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReservaAdapter(viewModel.getReservas().getValue(), this::onCancelarClick);
        rvReservas.setAdapter(adapter);

        setupToolbarWithBack(getString(R.string.titulo_reservas));
    }

    private void observeViewModel() {
        viewModel.getLoading().observe(this, loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            rvReservas.setVisibility(loading ? View.GONE : View.VISIBLE);
        });

        viewModel.getReservas().observe(this, reservas -> {
            adapter = new ReservaAdapter(reservas, this::onCancelarClick);
            rvReservas.setAdapter(adapter);
            tvEmpty.setVisibility(reservas.isEmpty() ? View.VISIBLE : View.GONE);
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getReservaCancelada().observe(this, cancelada -> {
            if (Boolean.TRUE.equals(cancelada)) {
                Toast.makeText(this, R.string.reserva_cancelada, Toast.LENGTH_SHORT).show();
                viewModel.loadReservas();
            }
        });
    }

    private void onCancelarClick(Reserva reserva) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.btn_cancelar_reserva)
                .setMessage(R.string.confirmar_cancelar)
                .setPositiveButton("S\u00ed", (dialog, which) ->
                        viewModel.cancelarReserva(reserva.getId()))
                .setNegativeButton("No", null)
                .show();
    }
}
