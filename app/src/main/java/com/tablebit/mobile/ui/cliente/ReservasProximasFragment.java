package com.tablebit.mobile.ui.cliente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservasProximasFragment extends Fragment {

    private RecyclerView rvReservas;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private ReservaClienteAdapter adapter;
    private final List<Reserva> reservasFiltradas = new ArrayList<>();
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservas_proximas, container, false);

        rvReservas = view.findViewById(R.id.rvReservas);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        sessionManager = new SessionManager(requireContext());

        rvReservas.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReservaClienteAdapter(reservasFiltradas, true, this::confirmarCancelacion);
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
                            filtrarProximas(response.body());
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

    private void filtrarProximas(List<Reserva> todas) {
        reservasFiltradas.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date hoy = new Date();

        for (Reserva r : todas) {
            String estado = r.getEstado();
            if (estado != null && estado.equalsIgnoreCase("cancelada")) continue;

            try {
                Date fechaReserva = sdf.parse(r.getFecha());
                if (fechaReserva != null && !fechaReserva.before(hoy)) {
                    reservasFiltradas.add(r);
                }
            } catch (ParseException e) {
                if (estado != null && !estado.equalsIgnoreCase("cancelada")) {
                    reservasFiltradas.add(r);
                }
            }
        }

        adapter.updateData(reservasFiltradas);
        tvEmpty.setVisibility(reservasFiltradas.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void confirmarCancelacion(Reserva reserva) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.btn_cancelar_reserva)
                .setMessage(R.string.confirmar_cancelar)
                .setPositiveButton("S\u00ed", (dialog, which) ->
                        cancelarReserva(reserva.getId()))
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelarReserva(int reservaId) {
        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .cancelarReserva(reservaId)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), R.string.reserva_cancelada, Toast.LENGTH_SHORT).show();
                            cargarReservas();
                        } else {
                            Toast.makeText(getContext(), R.string.error_general, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast.makeText(getContext(), R.string.error_red, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvReservas.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
