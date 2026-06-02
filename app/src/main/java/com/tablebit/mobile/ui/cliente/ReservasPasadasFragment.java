package com.tablebit.mobile.ui.cliente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tablebit.mobile.R;
import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.Reserva;
import com.tablebit.mobile.session.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservasPasadasFragment extends Fragment {

    private RecyclerView rvReservas;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private ReservaClienteAdapter adapter;
    private final List<Reserva> reservasFiltradas = new ArrayList<>();
    private SessionManager sessionManager;
    private boolean filterCanceladas = false;

    public void setFilterCanceladas(boolean filter) {
        this.filterCanceladas = filter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservas_pasadas, container, false);

        rvReservas = view.findViewById(R.id.rvReservas);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        sessionManager = new SessionManager(requireContext());

        rvReservas.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReservaClienteAdapter(reservasFiltradas, false, null);
        rvReservas.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarReservas();
    }

    private void cargarReservas() {
        showLoading(true);

        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService().getMisReservas()
                .enqueue(new Callback<List<Reserva>>() {
                    @Override
                    public void onResponse(Call<List<Reserva>> call, Response<List<Reserva>> response) {
                        showLoading(false);
                        if (response.isSuccessful() && response.body() != null) {
                            filtrarPasadas(response.body());
                        } else {
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Reserva>> call, Throwable t) {
                        showLoading(false);
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void filtrarPasadas(List<Reserva> todas) {
        reservasFiltradas.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date hoy = new Date();

        for (Reserva r : todas) {
            String estado = r.getEstado();
            boolean isCancelada = estado != null && estado.equalsIgnoreCase("cancelada");

            if (filterCanceladas) {
                if (isCancelada) reservasFiltradas.add(r);
                continue;
            }

            if (isCancelada) continue;
            try {
                Date fechaReserva = sdf.parse(r.getFecha());
                if (fechaReserva != null && fechaReserva.before(hoy)) {
                    reservasFiltradas.add(r);
                }
            } catch (ParseException e) {
                // skip
            }
        }

        adapter.updateData(reservasFiltradas);
        tvEmpty.setVisibility(reservasFiltradas.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvReservas.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
