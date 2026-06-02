package com.tablebit.mobile.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.model.DashboardData;
import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.session.SessionManager;
import com.tablebit.mobile.ui.admin.adapters.QuickActionsAdapter;
import com.tablebit.mobile.ui.cliente.ProcesoReservaActivity;

import java.util.List;
import java.util.Locale;

public class AdminDashboardActivity extends AppCompatActivity {

    private AdminDashboardViewModel viewModel;
    private SessionManager sessionManager;
    private ProgressBar progressBar;
    private View rootView;
    private boolean destroyed = false;

    private MaterialCardView cardSelector, cardLogout;
    private TextView tvAvatarInicial, tvRestauranteNombre;
    private View vStatusDot;

    private TextView tvReservasHoy, tvConfirmadasHoy, tvTendencia;
    private TextView tvOcupacion, tvOcupacionDetalle;
    private TextView tvMesasOcupadas, tvMesasLibres;
    private TextView tvEstadoTexto, tvEstadoSubtitulo;

    private LinearLayout containerGraficaHoras;
    private RecyclerView rvAcciones;

    private final String[] accionIconos = {"📅", "🪑", "🕐", "📊", "🖼"};
    private final String[] accionLabels = {
            "Nueva reserva", "Gestionar mesas",
            "Horarios", "Reservas", "Galer\u00eda"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        rootView = findViewById(android.R.id.content);
        sessionManager = new SessionManager(this);
        viewModel = new ViewModelProvider(this).get(AdminDashboardViewModel.class);

        initViews();
        observeViewModel();
        viewModel.loadRestaurantes();
    }

    @Override
    protected void onDestroy() {
        destroyed = true;
        super.onDestroy();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);

        setSupportActionBar(findViewById(R.id.toolbar));

        String name = sessionManager.getUserName();
        if (name != null && !name.isEmpty()) {
            setTitle("Panel de " + name);
        } else {
            setTitle("Panel de Administraci\u00f3n");
        }
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(true);

        cardSelector = findViewById(R.id.cardSelectorRestaurante);
        cardLogout = findViewById(R.id.cardLogout);
        tvAvatarInicial = findViewById(R.id.tvAvatarInicial);
        tvRestauranteNombre = findViewById(R.id.tvRestauranteNombre);
        vStatusDot = findViewById(R.id.vStatusDot);

        tvReservasHoy = findViewById(R.id.tvReservasHoy);
        tvConfirmadasHoy = findViewById(R.id.tvConfirmadasHoy);
        tvTendencia = findViewById(R.id.tvTendencia);
        tvOcupacion = findViewById(R.id.tvOcupacion);
        tvOcupacionDetalle = findViewById(R.id.tvOcupacionDetalle);
        tvMesasOcupadas = findViewById(R.id.tvMesasOcupadas);
        tvMesasLibres = findViewById(R.id.tvMesasLibres);
        tvEstadoTexto = findViewById(R.id.tvEstadoTexto);
        tvEstadoSubtitulo = findViewById(R.id.tvEstadoSubtitulo);

        containerGraficaHoras = findViewById(R.id.containerGraficaHoras);

        rvAcciones = findViewById(R.id.rvAcciones);
        rvAcciones.setLayoutManager(new GridLayoutManager(this, 2));
        rvAcciones.setAdapter(new QuickActionsAdapter(accionIconos, accionLabels, this::handleQuickAction));

        cardSelector.setOnClickListener(v -> {
            if (getSupportFragmentManager().findFragmentByTag("Selector") != null) return;
            List<Restaurante> lista = viewModel.getRestaurantes().getValue();
            if (lista == null || lista.isEmpty()) return;
            new SelectorSheet(lista, viewModel::seleccionarRestaurante)
                    .show(getSupportFragmentManager(), "Selector");
        });

        cardLogout.setOnClickListener(v -> logout());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, "Cerrar sesión")
                .setIcon(android.R.drawable.ic_lock_power_off)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        sessionManager.forceLogout(this);
    }

    private void handleQuickAction(int position, String label) {
        Restaurante r = viewModel.getRestauranteSeleccionado().getValue();
        if (r == null) {
            Snackbar.make(rootView, "Cargando restaurante... espera un momento", Snackbar.LENGTH_SHORT).show();
            return;
        }
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(this, com.tablebit.mobile.ui.cliente.ProcesoReservaActivity.class);
                intent.putExtra("restaurante_id", r.getId());
                intent.putExtra("restaurante_nombre", r.getNombre());
                break;
            case 1:
                intent = new Intent(this, GestionMesasActivity.class)
                        .putExtra("restaurante_id", r.getId())
                        .putExtra("restaurante_nombre", r.getNombre());
                break;
            case 2:
                intent = new Intent(this, HorariosRestauranteActivity.class)
                        .putExtra("restaurante_id", r.getId());
                break;
            case 3:
                intent = new Intent(this, ListadoReservasActivity.class)
                        .putExtra("restaurante_id", r.getId());
                break;
            case 4:
                intent = new Intent(this, GaleriaAdminActivity.class)
                        .putExtra("restaurante_id", r.getId());
                break;
        }
        if (intent != null) startActivity(intent);
    }

    private void observeViewModel() {
        viewModel.getLoading().observe(this, loading -> {
            if (destroyed) return;
            progressBar.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE);
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (destroyed || error == null) return;
            Snackbar.make(rootView, error, Snackbar.LENGTH_LONG)
                    .setAction("Reintentar", v -> {
                        Restaurante r = viewModel.getRestauranteSeleccionado().getValue();
                        if (r != null) viewModel.loadDashboard(r.getId());
                        else viewModel.loadRestaurantes();
                    })
                    .show();
        });

        viewModel.getRestauranteSeleccionado().observe(this, r -> {
            if (destroyed || r == null) return;
            tvAvatarInicial.setText(String.valueOf(r.getNombre().charAt(0)).toUpperCase());
            tvRestauranteNombre.setText(r.getNombre());
        });

        viewModel.getDashboardData().observe(this, this::actualizarDashboard);
    }

    private void actualizarDashboard(DashboardData d) {
        if (destroyed || d == null) return;
        int reservasHoy = d.getReservasHoy();
        int ocupacion = (int) Math.round(d.getOcupacionHoy());
        int totalMesas = d.getMesasTotales();
        int ocupadas = d.getMesasOcupadasHoy();
        int libres = d.getMesasLibresHoy();

        tvReservasHoy.setText(String.valueOf(reservasHoy));
        tvConfirmadasHoy.setText(d.getConfirmadas() + " confirmadas");
        tvOcupacion.setText(String.format(Locale.US, "%d%%", ocupacion));
        tvOcupacionDetalle.setText(String.format(Locale.US, "%d/%d mesas hoy", ocupadas, totalMesas));
        tvMesasOcupadas.setText(ocupadas + "/" + totalMesas);
        tvMesasLibres.setText(libres + " libres");

        if (reservasHoy == 0) { tvEstadoTexto.setText("Tranquilo"); tvEstadoSubtitulo.setText("Sin reservas hoy"); }
        else if (reservasHoy <= 3) { tvEstadoTexto.setText("Normal"); tvEstadoSubtitulo.setText(reservasHoy + " reservas hoy"); }
        else { tvEstadoTexto.setText("Ocupado"); tvEstadoSubtitulo.setText(reservasHoy + " reservas programadas"); }

        renderizarGraficaHoras(d.getHorasPico());
    }

    private void renderizarGraficaHoras(List<DashboardData.HoraPico> horas) {
        if (destroyed || isFinishing() || isDestroyed() || containerGraficaHoras == null) return;
        containerGraficaHoras.removeAllViews();
        if (horas == null || horas.isEmpty()) {
            TextView t = new TextView(this);
            t.setText("Sin datos de horas pico");
            t.setTextColor(getResources().getColor(R.color.text_secondary));
            containerGraficaHoras.addView(t);
            return;
        }
        int maxTotal = 0;
        for (DashboardData.HoraPico h : horas) maxTotal = Math.max(maxTotal, h.getTotal());
        if (maxTotal == 0) maxTotal = 1;
        for (DashboardData.HoraPico h : horas) {
            View barRow = getLayoutInflater().inflate(R.layout.item_barra_hora, containerGraficaHoras, false);
            TextView tvLabel = barRow.findViewById(R.id.tvLabelHora);
            View bar = barRow.findViewById(R.id.vBarra);
            TextView tvValor = barRow.findViewById(R.id.tvValorHora);
            tvLabel.setText(String.format(Locale.US, "%02d:00", h.getHora()));
            int anchoBarra = (int) (((float) h.getTotal() / maxTotal) * 200);
            bar.getLayoutParams().width = dpToPx(Math.max(anchoBarra, 20));
            bar.setBackgroundColor(getResources().getColor(R.color.primary));
            tvValor.setText(String.valueOf(h.getTotal()));
            containerGraficaHoras.addView(barRow);
        }
    }

    private int dpToPx(int dp) { return (int) (dp * getResources().getDisplayMetrics().density); }
}
