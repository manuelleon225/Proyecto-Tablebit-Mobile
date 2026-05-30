package com.tablebit.mobile.session;

import android.content.Context;
import android.content.Intent;

import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.ui.auth.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionManager {

    private final TokenManager tokenManager;

    public SessionManager(Context context) {
        this.tokenManager = new TokenManager(context);
    }

    public String getToken() {
        return tokenManager.getToken();
    }

    public void saveToken(String token) {
        tokenManager.saveToken(token);
    }

    public boolean isLoggedIn() {
        return tokenManager.isLoggedIn();
    }

    public String getUserName() {
        return tokenManager.getUserName();
    }

    public String getUserEmail() {
        return tokenManager.getUserEmail();
    }

    public int getUserId() {
        return tokenManager.getUserId();
    }

    public void saveUserInfo(int userId, String name, String email) {
        tokenManager.saveUserInfo(userId, name, email);
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void performLogout(Context context) {
        if (!isLoggedIn()) {
            forceLogout(context);
            return;
        }

        Call<ApiResponse<Void>> call = RetrofitClient.getInstance(tokenManager).getApiService().logout();
        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                forceLogout(context);
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                forceLogout(context);
            }
        });
    }

    public void forceLogout(Context context) {
        tokenManager.clearSession();
        RetrofitClient.resetInstance();
        if (context != null) {
            redirectToLogin(context);
        }
    }

    public static void redirectToLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).finish();
        }
    }
}
