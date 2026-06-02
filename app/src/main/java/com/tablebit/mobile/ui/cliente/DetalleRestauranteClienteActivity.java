package com.tablebit.mobile.ui.cliente;

import com.tablebit.mobile.data.api.NetworkConfig;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

public class DetalleRestauranteClienteActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private int restauranteId;
    private String nombreRest, ciudadRest, descripcionRest;
    private boolean esFavorito = false;
    private ImageView ivBanner, btnBack, btnFav, btnShare;
    private TextView tvNombre, tvTipo, tvCiudad, tvRating, tvEstado;
    private TextView tvDireccion, tvTelefono, tvCapacidad, tvDescripcion;
    private LinearLayout containerHorarios;
    private RecyclerView rvResenas, rvGaleria;
    private TextView tvGaleriaTitulo;
    private ExtendedFloatingActionButton fabReservar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_restaurante_cliente);

        restauranteId = getIntent().getIntExtra("restaurante_id", -1);
        sessionManager = new SessionManager(this);
        initViews();
        loadDetalle();
        verificarFavorito();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            loadDetalle();
        }
    }

    private void verificarFavorito() {
        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .verificarFavorito(restauranteId)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Object active = response.body().get("active");
                            esFavorito = Boolean.TRUE.equals(active);
                            btnFav.setColorFilter(ContextCompat.getColor(DetalleRestauranteClienteActivity.this,
                                    esFavorito ? android.R.color.holo_red_light : android.R.color.darker_gray));
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {}
                });
    }

    private void initViews() {
        ivBanner = findViewById(R.id.ivBanner);
        btnBack = findViewById(R.id.btnBack);
        btnFav = findViewById(R.id.btnFavorito);
        btnShare = findViewById(R.id.btnShare);
        tvNombre = findViewById(R.id.tvNombreDetalle);
        tvTipo = findViewById(R.id.tvTipoDetalle);
        tvCiudad = findViewById(R.id.tvCiudadDetalle);
        tvRating = findViewById(R.id.tvRatingDetalle);
        tvEstado = findViewById(R.id.tvEstadoDetalle);
        tvDireccion = findViewById(R.id.tvDireccionDetalle);
        tvTelefono = findViewById(R.id.tvTelefonoDetalle);
        tvCapacidad = findViewById(R.id.tvCapacidadDetalle);
        tvDescripcion = findViewById(R.id.tvDescripcionDetalle);
        containerHorarios = findViewById(R.id.containerHorarios);
        rvResenas = findViewById(R.id.rvResenas);
        rvGaleria = findViewById(R.id.rvGaleria);
        tvGaleriaTitulo = findViewById(R.id.tvGaleriaTitulo);
        fabReservar = findViewById(R.id.fabReservar);
        View btnEscribirResena = findViewById(R.id.btnEscribirResena);

        btnBack.setOnClickListener(v -> finish());

        btnFav.setOnClickListener(v -> {
            btnFav.setEnabled(false);
            RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                    .toggleFavorito(restauranteId)
                    .enqueue(new Callback<Map<String, Object>>() {
                        @Override
                        public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                            btnFav.setEnabled(true);
                            if (response.isSuccessful()) {
                                esFavorito = !esFavorito;
                                btnFav.setColorFilter(ContextCompat.getColor(DetalleRestauranteClienteActivity.this,
                                        esFavorito ? android.R.color.holo_red_light : android.R.color.darker_gray));
                                Snackbar.make(ivBanner,
                                        esFavorito ? "A\u00f1adido a favoritos" : "Eliminado de favoritos",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                            btnFav.setEnabled(true);
                        }
                    });
        });

        btnShare.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT,
                    "\uD83C\uDF7D Mira este restaurante en TableBit:\n"
                    + (nombreRest != null ? nombreRest : "") + "\n"
                    + (ciudadRest != null ? ciudadRest + " \u00b7 " : "")
                    + (descripcionRest != null && descripcionRest.length() > 100
                            ? descripcionRest.substring(0, 100) + "..." : descripcionRest != null ? descripcionRest : "")
                    + "\n\nhttps://tablebit.com/restaurante/" + restauranteId);
            startActivity(Intent.createChooser(share, "Compartir restaurante"));
        });

        btnEscribirResena.setOnClickListener(v -> {
            Intent i = new Intent(this, CrearResenaActivity.class);
            i.putExtra("restaurante_id", restauranteId);
            startActivityForResult(i, 1001);
        });

        fabReservar.setOnClickListener(v -> {
            Intent i = new Intent(this, CrearReservaActivity.class);
            i.putExtra("restaurante_id", restauranteId);
            startActivity(i);
        });
    }

    private void loadDetalle() {
        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .getRestaurante(restauranteId)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Map<String, Object> data = response.body();
                            Object restObj = data.get("restaurante");
                            if (restObj instanceof Map) {
                                Map<String, Object> r = (Map<String, Object>) restObj;
                                actualizarUI(r, data);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {}
                });
    }

    private void actualizarUI(Map<String, Object> r, Map<String, Object> data) {
        String nombre = (String) r.get("nombre");
        nombreRest = nombre;
        tvNombre.setText(nombre);

        String tipo = (String) r.get("tipo_comida");
        tvTipo.setText(tipo != null ? tipo : "");

        String ciudad = (String) r.get("ciudad");
        ciudadRest = ciudad;
        tvCiudad.setText(ciudad != null ? ciudad : "");

        Object ratingObj = data.get("rating_promedio");
        String ratingStr = ratingObj != null ? "\u2605 " + String.format("%.1f", Double.parseDouble(ratingObj.toString())) : "";
        tvRating.setText(ratingStr);

        Object abierto = data.get("abierto_ahora");
        if (Boolean.TRUE.equals(abierto)) {
            tvEstado.setText("\u25CF Abierto ahora");
            tvEstado.setTextColor(ContextCompat.getColor(this, R.color.success));
        } else {
            tvEstado.setText("\u25CF Cerrado");
            tvEstado.setTextColor(ContextCompat.getColor(this, R.color.error));
        }

        String dir = (String) r.get("direccion");
        tvDireccion.setText("\uD83D\uDCCD " + (dir != null ? dir : ""));

        String tel = (String) r.get("telefono");
        tvTelefono.setText("\uD83D\uDCDE " + (tel != null ? tel : ""));

        Object cap = r.get("capacidad_total");
        tvCapacidad.setText("\uD83D\uDC65 Capacidad: " + (cap != null ? cap.toString() : "N/A") + " personas");

        String desc = (String) r.get("descripcion");
        descripcionRest = desc;
        tvDescripcion.setText(desc != null ? desc : "Sin descripci\u00f3n");

        // Banner image
        String imagen = (String) r.get("imagen");
        if (imagen != null) {
            String url = NetworkConfig.STORAGE_URL + imagen;
            Glide.with(this).load(url)
                    .transform(new CenterCrop(), new RoundedCorners(16))
                    .placeholder(android.R.color.darker_gray).into(ivBanner);
        }

        // Galería
        List<String> imagenes = new ArrayList<>();
        if (imagen != null) imagenes.add(imagen);
        Object logoObj = r.get("logo");
        if (logoObj instanceof String && ((String) logoObj).length() > 0) imagenes.add((String) logoObj);
        Object bannerObj = r.get("banner");
        if (bannerObj instanceof String && ((String) bannerObj).length() > 0) imagenes.add((String) bannerObj);

        if (imagenes.size() > 1) {
            tvGaleriaTitulo.setVisibility(View.VISIBLE);
            rvGaleria.setVisibility(View.VISIBLE);
            rvGaleria.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvGaleria.setAdapter(new GaleriaAdapter(imagenes, pos -> {
                String[] arr = imagenes.toArray(new String[0]);
                Intent i = new Intent(this, GaleriaFullscreenActivity.class);
                i.putExtra("imagenes", arr);
                i.putExtra("posicion", pos);
                startActivity(i);
            }));
        }

        // Horarios con HOY destacado
        String apertura = (String) r.get("horario_apertura");
        String cierre = (String) r.get("horario_cierre");
        String[] dias = {"Lunes", "Martes", "Mi\u00e9rcoles", "Jueves", "Viernes", "S\u00e1bado", "Domingo"};
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int hoyIndex = (cal.get(java.util.Calendar.DAY_OF_WEEK) + 6) % 7; // Domingo=0 → Lunes=0
        if (apertura != null && cierre != null) {
            for (int i = 0; i < 7; i++) {
                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setPadding(0, 4, 0, 4);

                TextView tvDia = new TextView(this);
                tvDia.setLayoutParams(new LinearLayout.LayoutParams(0, 48, 1));
                if (i == hoyIndex) {
                    tvDia.setText("HOY \u00b7 " + dias[i]);
                    tvDia.setTextColor(ContextCompat.getColor(this, R.color.primary));
                    tvDia.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_TitleSmall);
                } else {
                    tvDia.setText(dias[i]);
                    tvDia.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyMedium);
                    tvDia.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
                }

                TextView tvHora = new TextView(this);
                tvHora.setLayoutParams(new LinearLayout.LayoutParams(0, 48, 1));
                tvHora.setText(apertura + " - " + cierre);
                tvHora.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodySmall);
                tvHora.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));

                row.addView(tvDia);
                row.addView(tvHora);
                containerHorarios.addView(row);
            }
        }

        // Reseñas
        Object resenasObj = r.get("resenas");
        List<Map<String, Object>> resenas = new ArrayList<>();
        if (resenasObj instanceof List) {
            resenas = (List<Map<String, Object>>) resenasObj;
        }
        rvResenas.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvResenas.setAdapter(new ReviewAdapter(resenas));
    }
}


