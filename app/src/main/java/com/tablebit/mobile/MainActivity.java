package com.tablebit.mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.session.TokenManager;
import com.tablebit.mobile.ui.auth.LoginActivity;
import com.tablebit.mobile.ui.perfil.PerfilActivity;
import com.tablebit.mobile.ui.reservas.ReservasActivity;
import com.tablebit.mobile.ui.restaurantes.RestauranteListActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tokenManager = new TokenManager(this);

        if (!tokenManager.isLoggedIn()) {
            goToLogin();
            return;
        }

        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        String name = tokenManager.getUserName();
        tvWelcome.setText(name != null ? getString(R.string.bienvenido) + ", " + name + "!" : getString(R.string.bienvenido) + "!");

        CardView cardRestaurantes = findViewById(R.id.cardRestaurantes);
        CardView cardReservas = findViewById(R.id.cardReservas);
        CardView cardPerfil = findViewById(R.id.cardPerfil);
        CardView cardLogout = findViewById(R.id.cardLogout);

        cardRestaurantes.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RestauranteListActivity.class))
        );

        cardReservas.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ReservasActivity.class))
        );

        cardPerfil.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, PerfilActivity.class))
        );

        cardLogout.setOnClickListener(v -> confirmLogout());
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.menu_cerrar_sesion))
                .setMessage(getString(R.string.confirmar_cerrar_sesion))
                .setPositiveButton("Sí", (dialog, which) -> logout())
                .setNegativeButton("No", null)
                .show();
    }

    private void logout() {
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
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
