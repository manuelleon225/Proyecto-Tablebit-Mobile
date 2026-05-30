package com.tablebit.mobile.ui.perfil;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.tablebit.mobile.R;
import com.tablebit.mobile.ui.BaseActivity;
import com.tablebit.mobile.ui.viewmodel.PerfilViewModel;

public class PerfilActivity extends BaseActivity {

    private PerfilViewModel viewModel;
    private TextView tvName, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        initViews();
        observeViewModel();
        viewModel.loadPerfil();
    }

    private void initViews() {
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);

        String name = viewModel.getUserName();
        String email = viewModel.getUserEmail();
        if (name != null) tvName.setText(name);
        if (email != null) tvEmail.setText(email);

        setupToolbarWithBack(getString(R.string.titulo_perfil));

        CardView cardLogout = findViewById(R.id.cardLogout);
        cardLogout.setOnClickListener(v -> confirmLogout());
    }

    private void observeViewModel() {
        viewModel.getUsuario().observe(this, user -> {
            if (user != null) {
                tvName.setText(user.getName());
                tvEmail.setText(user.getEmail());
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });

        viewModel.getLogoutDone().observe(this, done -> {
            if (Boolean.TRUE.equals(done)) {
                finish();
            }
        });
    }

    private void confirmLogout() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.menu_cerrar_sesion))
                .setMessage(getString(R.string.confirmar_cerrar_sesion))
                .setPositiveButton("S\u00ed", (dialog, which) ->
                        viewModel.logout())
                .setNegativeButton("No", null)
                .show();
    }
}
