package com.tablebit.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.tablebit.mobile.session.SessionManager;
import com.tablebit.mobile.ui.auth.LoginActivity;
import com.tablebit.mobile.ui.perfil.PerfilActivity;
import com.tablebit.mobile.ui.reservas.ReservasActivity;
import com.tablebit.mobile.ui.restaurantes.RestauranteListActivity;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            sessionManager.forceLogout(this);
            return;
        }

        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        String name = sessionManager.getUserName();
        tvWelcome.setText(name != null
                ? getString(R.string.bienvenido) + ", " + name + "!"
                : getString(R.string.bienvenido) + "!");

        CardView cardRestaurantes = findViewById(R.id.cardRestaurantes);
        CardView cardReservas = findViewById(R.id.cardReservas);
        CardView cardPerfil = findViewById(R.id.cardPerfil);
        CardView cardLogout = findViewById(R.id.cardLogout);

        cardRestaurantes.setOnClickListener(v ->
                startActivity(new Intent(this, RestauranteListActivity.class))
        );
        cardReservas.setOnClickListener(v ->
                startActivity(new Intent(this, ReservasActivity.class))
        );
        cardPerfil.setOnClickListener(v ->
                startActivity(new Intent(this, PerfilActivity.class))
        );
        cardLogout.setOnClickListener(v -> confirmLogout());
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.menu_cerrar_sesion))
                .setMessage(getString(R.string.confirmar_cerrar_sesion))
                .setPositiveButton("Sí", (dialog, which) ->
                        sessionManager.performLogout(MainActivity.this))
                .setNegativeButton("No", null)
                .show();
    }
}
