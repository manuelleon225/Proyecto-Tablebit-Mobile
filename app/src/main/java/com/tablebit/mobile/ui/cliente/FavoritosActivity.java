package com.tablebit.mobile.ui.cliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tablebit.mobile.R;
import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.session.SessionManager;
import com.tablebit.mobile.ui.reservas.CrearReservaActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritosActivity extends AppCompatActivity {

    private RecyclerView rvFavoritos;
    private TextView tvEmpty;
    private RestauranteRecomendadoAdapter adapter;
    private final List<Restaurante> items = new ArrayList<>();
    private SessionManager sessionManager;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        sessionManager = new SessionManager(this);
        rvFavoritos = findViewById(R.id.rvFavoritos);
        tvEmpty = findViewById(R.id.tvEmpty);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this::loadFavoritos);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.primary));

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        rvFavoritos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RestauranteRecomendadoAdapter(items, r -> {
            Intent i = new Intent(this, CrearReservaActivity.class);
            i.putExtra("restaurante_id", r.getId());
            startActivity(i);
        });
        rvFavoritos.setAdapter(adapter);

        loadFavoritos();
    }

    private void loadFavoritos() {
        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .getFavoritos()
                .enqueue(new Callback<List<Map<String, Object>>>() {
                    @Override
                    public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                        swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful() && response.body() != null) {
                            items.clear();
                            for (Map<String, Object> fav : response.body()) {
                                Object rObj = fav.get("restaurante");
                                if (rObj instanceof Map) {
                                    Map<String, Object> r = (Map<String, Object>) rObj;
                                    Restaurante rest = new Restaurante();
                                    Object idObj = r.get("id");
                                    if (idObj instanceof Double) rest.setId(((Double) idObj).intValue());
                                    else if (idObj instanceof Integer) rest.setId((Integer) idObj);
                                    rest.setNombre((String) r.get("nombre"));
                                    rest.setTipoComida((String) r.get("tipo_comida"));
                                    rest.setCiudad((String) r.get("ciudad"));
                                    rest.setImagen((String) r.get("imagen"));
                                    Object rateObj = r.get("resenas_avg_rating");
                                    if (rateObj instanceof Double) rest.setResenasAvgRating((Double) rateObj);
                                    Object abiertoObj = r.get("abierto_ahora");
                                    if (abiertoObj instanceof Boolean) rest.setAbiertoAhora((Boolean) abiertoObj);
                                    items.add(rest);
                                }
                            }
                            adapter.updateData(items);
                            tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                        swipeRefresh.setRefreshing(false);
                    }
                });
    }
}
