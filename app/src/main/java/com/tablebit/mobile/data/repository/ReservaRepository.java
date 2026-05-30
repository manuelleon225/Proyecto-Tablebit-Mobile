package com.tablebit.mobile.data.repository;

import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.data.model.Reserva;
import com.tablebit.mobile.data.model.ReservaRequest;
import com.tablebit.mobile.session.TokenManager;

import java.util.List;

import retrofit2.Call;

public class ReservaRepository {

    private final RetrofitClient client;

    public ReservaRepository(TokenManager tokenManager) {
        this.client = RetrofitClient.getInstance(tokenManager);
    }

    public Call<ApiResponse<Reserva>> crearReserva(int restauranteId, int mesaId, String fecha, String hora, int personas) {
        ReservaRequest request = new ReservaRequest(restauranteId, mesaId, fecha, hora, personas);
        return client.getApiService().crearReserva(request);
    }

    public Call<ApiResponse<List<Reserva>>> getMisReservas() {
        return client.getApiService().getMisReservas();
    }

    public Call<ApiResponse<Reserva>> cancelarReserva(int reservaId) {
        return client.getApiService().cancelarReserva(reservaId);
    }
}
