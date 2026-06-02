package com.tablebit.mobile.data.repository;

import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.DashboardData;
import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.session.TokenManager;

import java.util.List;

import retrofit2.Call;

public class DashboardRepository {

    private final RetrofitClient client;

    public DashboardRepository(TokenManager tokenManager) {
        this.client = RetrofitClient.getInstance(tokenManager);
    }

    public Call<List<Restaurante>> getMisRestaurantes() {
        return client.getApiService().getMisRestaurantes();
    }

    public Call<DashboardData> getDashboard(int restauranteId) {
        return client.getApiService().getDashboard(restauranteId);
    }
}
