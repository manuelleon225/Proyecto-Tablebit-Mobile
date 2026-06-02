package com.tablebit.mobile.data.repository;

import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.LoginRequest;
import com.tablebit.mobile.data.model.LoginResponse;
import com.tablebit.mobile.data.model.RegisterRequest;
import com.tablebit.mobile.session.SessionManager;

import java.util.Map;

import retrofit2.Call;

public class AuthRepository {

    private final RetrofitClient client;
    private final SessionManager sessionManager;

    public AuthRepository(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.client = RetrofitClient.getInstance(sessionManager);
    }

    public Call<LoginResponse> login(String email, String password) {
        return client.getApiService().login(new LoginRequest(email, password));
    }

    public Call<LoginResponse> register(String name, String email, String password) {
        return client.getApiService().register(new RegisterRequest(name, email, password));
    }

    public Call<Map<String, Object>> logout() {
        return client.getApiService().logout();
    }

    public void saveSession(LoginResponse response) {
        sessionManager.saveToken(response.getToken());
        if (response.getUser() != null) {
            sessionManager.saveUserInfo(
                    response.getUser().getId(),
                    response.getUser().getName(),
                    response.getUser().getEmail(),
                    response.getUser().getRole()
            );
        }
    }

    public void clearSession() {
        sessionManager.forceLogout(null);
    }
}
