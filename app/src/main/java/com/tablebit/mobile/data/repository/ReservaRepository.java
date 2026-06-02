package com.tablebit.mobile.data.repository;

import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.Reserva;
import com.tablebit.mobile.data.model.ReservaRequest;
import com.tablebit.mobile.session.TokenManager;

import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class ReservaRepository {

    private final RetrofitClient client;

    public ReservaRepository(TokenManager tokenManager) {
        this.client = RetrofitClient.getInstance(tokenManager);
    }

    public Call<Map<String, Object>> crearReserva(int restauranteId, String fecha, String hora, int cantidadPersonas) {
        ReservaRequest request = new ReservaRequest(restauranteId, fecha, hora, cantidadPersonas);
        return client.getApiService().crearReserva(request);
    }

    public Call<List<Reserva>> getMisReservas() {
        return client.getApiService().getMisReservas();
    }

    public Call<Map<String, Object>> cancelarReserva(int reservaId) {
        return client.getApiService().cancelarReserva(reservaId);
    }
}
