package com.tablebit.mobile.ui.cliente;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.Reserva;
import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.session.SessionManager;
import com.tablebit.mobile.ui.perfil.PerfilActivity;
import com.tablebit.mobile.ui.reservas.CrearReservaActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeClienteActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private RestauranteRecomendadoAdapter adapter;
    private final List<Restaurante> restaurantesFull = new ArrayList<>();
    private CategoriaAdapter categoriaAdapter;
    private TextView tvGreeting, tvDate, tvAvatar;
    private RecyclerView rvCategorias, rvRecomendados;
    private ViewPager2 vpDestacados;
    private LinearLayout indicadorPuntos;
    private BottomNavigationView bottomNav;
    private View searchCard;
    private android.widget.EditText etSearch;
    private ImageView ivSearchClear;
    private ProgressBar progressSearch;
    private View layoutSearchEmpty;
    private TextView tvSearchEmptyTitle, tvSearchEmptySub;
    private CardView cardProximaReserva;
    private TextView tvProxRestaurante, tvProxFecha, tvProxPersonas;
    private SwipeRefreshLayout swipeRefresh;

    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Call<List<Restaurante>> pendingSearchCall;
    private static final int SEARCH_DEBOUNCE_MS = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);

        sessionManager = new SessionManager(this);
        initViews();
        loadRestaurantes();
        loadProximaReserva();
    }

    private void initViews() {
        tvGreeting = findViewById(R.id.tvGreeting);
        tvDate = findViewById(R.id.tvDate);
        tvAvatar = findViewById(R.id.tvAvatarHome);
        rvCategorias = findViewById(R.id.rvCategorias);
        vpDestacados = findViewById(R.id.vpDestacados);
        indicadorPuntos = findViewById(R.id.indicadorPuntos);
        rvRecomendados = findViewById(R.id.rvRecomendados);
        bottomNav = findViewById(R.id.bottomNav);
        searchCard = findViewById(R.id.searchCard);
        etSearch = findViewById(R.id.etSearch);
        ivSearchClear = findViewById(R.id.ivSearchClear);
        progressSearch = findViewById(R.id.progressSearch);
        layoutSearchEmpty = findViewById(R.id.layoutSearchEmpty);
        tvSearchEmptyTitle = findViewById(R.id.tvSearchEmptyTitle);
        tvSearchEmptySub = findViewById(R.id.tvSearchEmptySub);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(() -> {
            loadRestaurantes();
            loadProximaReserva();
        });
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.primary));
        cardProximaReserva = findViewById(R.id.cardProximaReserva);
        tvProxRestaurante = findViewById(R.id.tvProxRestaurante);
        tvProxFecha = findViewById(R.id.tvProxFecha);
        tvProxPersonas = findViewById(R.id.tvProxPersonas);

        String name = sessionManager.getUserName();
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String saludo;
        if (hour < 12) saludo = "Buenos d\u00edas";
        else if (hour < 18) saludo = "Buenas tardes";
        else saludo = "Buenas noches";
        tvGreeting.setText(saludo + (name != null ? ", " + name : "") + " \uD83D\uDC4B");

        String date = new SimpleDateFormat("EEEE d 'de' MMMM", new Locale("es", "ES")).format(new Date());
        tvDate.setText(date.substring(0, 1).toUpperCase() + date.substring(1));

        if (name != null && name.length() > 0) {
            tvAvatar.setText(String.valueOf(name.charAt(0)).toUpperCase());
        }

        rvCategorias.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCategorias.setHasFixedSize(true);
        categoriaAdapter = new CategoriaAdapter(new ArrayList<>(), cat -> {
            List<Restaurante> filtrados = new ArrayList<>();
            if (cat == null || cat.isEmpty()) {
                filtrados.addAll(restaurantesFull);
            } else {
                for (Restaurante r : restaurantesFull) {
                    if (r.getTipoComida() != null && r.getTipoComida().equalsIgnoreCase(cat)) {
                        filtrados.add(r);
                    }
                }
            }
            adapter.updateData(filtrados);
        });
        rvCategorias.setAdapter(categoriaAdapter);

        rvRecomendados.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RestauranteRecomendadoAdapter(new ArrayList<>(), r -> {
            hideKeyboard();
            Intent i = new Intent(this, CrearReservaActivity.class);
            i.putExtra("restaurante_id", r.getId());
            startActivity(i);
        });
        rvRecomendados.setAdapter(adapter);

        // Search: debounce + híbrida + API
        etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                ivSearchClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                layoutSearchEmpty.setVisibility(View.GONE);
                searchHandler.removeCallbacksAndMessages(null);
                searchHandler.postDelayed(() -> performSearch(s.toString().trim()), SEARCH_DEBOUNCE_MS);
                // Filtro local instantáneo
                filterLocal(s.toString().trim());
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });

        ivSearchClear.setOnClickListener(v -> {
            etSearch.setText("");
            adapter.updateData(new ArrayList<>(restaurantesFull));
            layoutSearchEmpty.setVisibility(View.GONE);
            progressSearch.setVisibility(View.GONE);
            ivSearchClear.setVisibility(View.GONE);
            etSearch.clearFocus();
            hideKeyboard();
        });

        searchCard.setOnClickListener(v -> etSearch.requestFocus());

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) return true;
            if (id == R.id.nav_reservas) {
                startActivity(new Intent(this, MisReservasActivity.class));
                return true;
            }
            if (id == R.id.nav_favoritos) {
                startActivity(new Intent(this, FavoritosActivity.class));
                return true;
            }
            if (id == R.id.nav_perfil) {
                startActivity(new Intent(this, PerfilActivity.class));
                return true;
            }
            return false;
        });
    }

    private void filterLocal(String query) {
        if (query.isEmpty()) {
            adapter.updateData(new ArrayList<>(restaurantesFull));
            layoutSearchEmpty.setVisibility(View.GONE);
            return;
        }
        List<Restaurante> result = new ArrayList<>();
        for (Restaurante r : restaurantesFull) {
            if (matchesQuery(r, query)) result.add(r);
        }
        adapter.updateData(result);
        layoutSearchEmpty.setVisibility(result.isEmpty() && !query.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private boolean matchesQuery(Restaurante r, String q) {
        String lower = q.toLowerCase();
        return (r.getNombre() != null && r.getNombre().toLowerCase().contains(lower))
            || (r.getTipoComida() != null && r.getTipoComida().toLowerCase().contains(lower))
            || (r.getCiudad() != null && r.getCiudad().toLowerCase().contains(lower));
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            progressSearch.setVisibility(View.GONE);
            layoutSearchEmpty.setVisibility(View.GONE);
            return;
        }

        if (pendingSearchCall != null && !pendingSearchCall.isCanceled()) {
            pendingSearchCall.cancel();
        }

        progressSearch.setVisibility(View.VISIBLE);

        pendingSearchCall = RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .buscarRestaurantes(query);

        pendingSearchCall.enqueue(new Callback<List<Restaurante>>() {
            @Override
            public void onResponse(Call<List<Restaurante>> call, Response<List<Restaurante>> response) {
                progressSearch.setVisibility(View.GONE);
                if (call.isCanceled()) return;
                if (!response.isSuccessful() || response.body() == null) return;

                HashSet<Integer> ids = new HashSet<>();
                List<Restaurante> merged = new ArrayList<>();

                // Primero los resultados locales que coinciden
                for (Restaurante r : restaurantesFull) {
                    if (matchesQuery(r, query) && ids.add(r.getId())) merged.add(r);
                }
                // Luego los resultados de la API (sin duplicar)
                for (Restaurante r : response.body()) {
                    if (ids.add(r.getId())) merged.add(r);
                }

                adapter.updateData(merged);
                boolean empty = merged.isEmpty();
                layoutSearchEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
                if (!empty) {
                    tvSearchEmptyTitle.setText("No encontramos restaurantes");
                    tvSearchEmptySub.setText("Intenta con otro nombre, tipo de comida o ciudad");
                }
            }

            @Override
            public void onFailure(Call<List<Restaurante>> call, Throwable t) {
                progressSearch.setVisibility(View.GONE);
            }
        });
    }

    private void hideKeyboard() {
        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager)
                getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
    }

    private void loadProximaReserva() {
        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .getMisReservas()
                .enqueue(new Callback<List<Reserva>>() {
                    @Override
                    public void onResponse(Call<List<Reserva>> call, Response<List<Reserva>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String hoy = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
                            for (Reserva r : response.body()) {
                                String estado = r.getEstado();
                                if (estado != null && (estado.equalsIgnoreCase("cancelada") || estado.equalsIgnoreCase("completada")))
                                    continue;
                                if (r.getFecha() != null && r.getFecha().compareTo(hoy) >= 0) {
                                    String nombre = r.getRestaurante() != null ? r.getRestaurante().getNombre() : "Restaurante";
                                    tvProxRestaurante.setText(nombre);
                                    tvProxFecha.setText(r.getFecha() + " \u00b7 " + r.getHora());
                                    tvProxPersonas.setText(r.getCantidadPersonas() + " personas");
                                    cardProximaReserva.setVisibility(View.VISIBLE);
                                    cardProximaReserva.setOnClickListener(v ->
                                            startActivity(new Intent(HomeClienteActivity.this, MisReservasActivity.class)));
                                    break;
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Reserva>> call, Throwable t) {}
                });
    }

    private void loadRestaurantes() {
        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .getRestaurantes()
                .enqueue(new Callback<List<Restaurante>>() {
                    @Override
                    public void onResponse(Call<List<Restaurante>> call, Response<List<Restaurante>> response) {
                        swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful() && response.body() != null) {
                            restaurantesFull.clear();
                            restaurantesFull.addAll(response.body());
                            adapter.updateData(restaurantesFull);

                            List<String> categorias = new ArrayList<>();
                            LinkedHashSet<String> unique = new LinkedHashSet<>();
                            for (Restaurante r : restaurantesFull) {
                                if (r.getTipoComida() != null) unique.add(r.getTipoComida());
                            }
                            categorias.addAll(unique);
                            categoriaAdapter.updateNombres(categorias);

                            RestauranteDestacadoAdapter destacadoAdapter = new RestauranteDestacadoAdapter(
                                    restaurantesFull.size() > 5 ? restaurantesFull.subList(0, 5) : restaurantesFull,
                                    r -> {
                                        Intent i = new Intent(HomeClienteActivity.this, CrearReservaActivity.class);
                                        i.putExtra("restaurante_id", r.getId());
                                        startActivity(i);
                                    });
                            vpDestacados.setAdapter(destacadoAdapter);

                            int count = Math.min(restaurantesFull.size(), 5);
                            for (int i = 0; i < count; i++) {
                                TextView dot = new TextView(HomeClienteActivity.this);
                                dot.setText("\u2B24");
                                dot.setTextSize(8);
                                dot.setTextColor(getResources().getColor(R.color.text_disabled));
                                dot.setPadding(6, 0, 6, 0);
                                indicadorPuntos.addView(dot);
                            }
                            if (count > 0) {
                                ((TextView) indicadorPuntos.getChildAt(0)).setTextColor(
                                        getResources().getColor(R.color.primary));
                            }
                            vpDestacados.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                @Override
                                public void onPageSelected(int pos) {
                                    for (int i = 0; i < indicadorPuntos.getChildCount(); i++) {
                                        ((TextView) indicadorPuntos.getChildAt(i)).setTextColor(
                                                i == pos ? getResources().getColor(R.color.primary)
                                                        : getResources().getColor(R.color.text_disabled));
                                    }
                                }
                            });
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Restaurante>> call, Throwable t) {
                        swipeRefresh.setRefreshing(false);
                    }
                });
    }
}
