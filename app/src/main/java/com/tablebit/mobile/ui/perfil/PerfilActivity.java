package com.tablebit.mobile.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.tablebit.mobile.MainActivity;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.data.model.Usuario;
import com.tablebit.mobile.session.TokenManager;
import com.tablebit.mobile.ui.auth.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {

    private TokenManager tokenManager;
    private TextView tvName, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        tokenManager = new TokenManager(this);

        initViews();
        loadPerfil();
    }

    private void initViews() {
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);

        String name = tokenManager.getUserName();
        String email = tokenManager.getUserEmail();
        if (name != null) tvName.setText(name);
        if (email != null) tvEmail.setText(email);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        CardView cardLogout = findViewById(R.id.cardLogout);
        cardLogout.setOnClickListener(v -> performLogout());
    }

    private void loadPerfil() {
        Call<ApiResponse<Usuario>> call = RetrofitClient.getInstance(tokenManager).getApiService().getPerfil();
        call.enqueue(new Callback<ApiResponse<Usuario>>() {
            @Override
            public void onResponse(Call<ApiResponse<Usuario>> call, Response<ApiResponse<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    Usuario user = response.body().getData();
                    tvName.setText(user.getName());
                    tvEmail.setText(user.getEmail());

                    tokenManager.saveUserInfo(user.getId(), user.getName(), user.getEmail());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Usuario>> call, Throwable t) {
                Toast.makeText(PerfilActivity.this, R.string.error_red, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performLogout() {
        Call<ApiResponse<Void>> call = RetrofitClient.getInstance(tokenManager).getApiService().logout();
        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                tokenManager.clearSession();
                RetrofitClient.resetInstance();
                goToLogin();
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                tokenManager.clearSession();
                RetrofitClient.resetInstance();
                goToLogin();
            }
        });
    }

    private void goToLogin() {
        Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
