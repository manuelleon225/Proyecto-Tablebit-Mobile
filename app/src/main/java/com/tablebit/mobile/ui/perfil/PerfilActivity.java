package com.tablebit.mobile.ui.perfil;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.tablebit.mobile.R;
import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.data.model.Usuario;
import com.tablebit.mobile.session.SessionManager;
import com.tablebit.mobile.ui.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends BaseActivity {

    private SessionManager sessionManager;
    private TextView tvName, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        sessionManager = new SessionManager(this);
        initViews();
        loadPerfil();
    }

    private void initViews() {
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);

        String name = sessionManager.getUserName();
        String email = sessionManager.getUserEmail();
        if (name != null) tvName.setText(name);
        if (email != null) tvEmail.setText(email);

        setupToolbarWithBack(getString(R.string.titulo_perfil));

        CardView cardLogout = findViewById(R.id.cardLogout);
        cardLogout.setOnClickListener(v -> confirmLogout());
    }

    private void confirmLogout() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.menu_cerrar_sesion))
                .setMessage(getString(R.string.confirmar_cerrar_sesion))
                .setPositiveButton("Sí", (dialog, which) ->
                        sessionManager.performLogout(PerfilActivity.this))
                .setNegativeButton("No", null)
                .show();
    }

    private void loadPerfil() {
        Call<ApiResponse<Usuario>> call = RetrofitClient
                .getInstance(sessionManager.getTokenManager()).getApiService().getPerfil();
        call.enqueue(new Callback<ApiResponse<Usuario>>() {
            @Override
            public void onResponse(Call<ApiResponse<Usuario>> call, Response<ApiResponse<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    Usuario user = response.body().getData();
                    tvName.setText(user.getName());
                    tvEmail.setText(user.getEmail());
                    sessionManager.saveUserInfo(user.getId(), user.getName(), user.getEmail());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Usuario>> call, Throwable t) {
                Toast.makeText(PerfilActivity.this, R.string.error_red, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
