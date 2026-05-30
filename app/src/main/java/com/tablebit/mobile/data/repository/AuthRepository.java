package com.tablebit.mobile.data.repository;

import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.data.model.LoginRequest;
import com.tablebit.mobile.data.model.LoginResponse;
import com.tablebit.mobile.data.model.RegisterRequest;
import com.tablebit.mobile.session.TokenManager;

import retrofit2.Call;

public class AuthRepository {

    private final RetrofitClient client;
    private final TokenManager tokenManager;

    public AuthRepository(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
        this.client = RetrofitClient.getInstance(tokenManager);
    }

    public Call<LoginResponse> login(String email, String password) {
        return client.getApiService().login(new LoginRequest(email, password));
    }

    public Call<LoginResponse> register(String name, String email, String password) {
        return client.getApiService().register(new RegisterRequest(name, email, password));
    }

    public Call<ApiResponse<Void>> logout() {
        return client.getApiService().logout();
    }

    public void saveSession(LoginResponse response) {
        tokenManager.saveToken(response.getToken());
        if (response.getUser() != null) {
            tokenManager.saveUserInfo(
                    response.getUser().getId(),
                    response.getUser().getName(),
                    response.getUser().getEmail()
            );
        }
    }

    public void clearSession() {
        tokenManager.clearSession();
        RetrofitClient.resetInstance();
    }
}
