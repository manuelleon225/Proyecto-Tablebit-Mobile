package com.tablebit.mobile.data.repository;

import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.session.TokenManager;

import java.util.List;

import retrofit2.Call;

public class RestauranteRepository {

    private final RetrofitClient client;

    public RestauranteRepository(TokenManager tokenManager) {
        this.client = RetrofitClient.getInstance(tokenManager);
    }

    public Call<ApiResponse<List<Restaurante>>> getRestaurantes() {
        return client.getApiService().getRestaurantes();
    }

    public Call<ApiResponse<Restaurante>> getRestaurante(int id) {
        return client.getApiService().getRestaurante(id);
    }
}
