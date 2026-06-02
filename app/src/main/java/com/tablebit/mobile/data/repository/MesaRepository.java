package com.tablebit.mobile.data.repository;

import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.Mesa;
import com.tablebit.mobile.session.TokenManager;

import java.util.List;

import retrofit2.Call;

public class MesaRepository {

    private final RetrofitClient client;

    public MesaRepository(TokenManager tokenManager) {
        this.client = RetrofitClient.getInstance(tokenManager);
    }

    public Call<List<Mesa>> getMesasByRestaurante(int restauranteId) {
        return client.getApiService().getMesasByRestaurante(restauranteId);
    }
}
