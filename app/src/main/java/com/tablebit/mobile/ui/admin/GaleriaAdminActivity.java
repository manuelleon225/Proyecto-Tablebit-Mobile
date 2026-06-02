package com.tablebit.mobile.ui.admin;

import com.tablebit.mobile.data.api.NetworkConfig;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.session.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GaleriaAdminActivity extends AppCompatActivity {

    private int restauranteId;
    private SessionManager sessionManager;
    private RecyclerView rvGaleria;
    private TextView tvEmpty;
    private GaleriaAdminAdapter adapter;
    private final List<Map<String, Object>> imagenesList = new ArrayList<>();
    private static final int PICK_IMAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_admin);

        restauranteId = getIntent().getIntExtra("restaurante_id", -1);
        sessionManager = new SessionManager(this);
        rvGaleria = findViewById(R.id.rvGaleria);
        tvEmpty = findViewById(R.id.tvEmpty);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        rvGaleria.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GaleriaAdminAdapter(imagenesList, this::confirmarEliminar);
        rvGaleria.setAdapter(adapter);

        findViewById(R.id.btnSubirImagen).setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "Seleccionar imagen"), PICK_IMAGE);
        });

        cargarImagenes();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            subirImagen(data.getData());
        }
    }

    private void cargarImagenes() {
        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .getImagenes(restauranteId)
                .enqueue(new Callback<List<Map<String, Object>>>() {
                    @Override
                    public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            imagenesList.clear();
                            imagenesList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                            tvEmpty.setVisibility(imagenesList.isEmpty() ? View.VISIBLE : View.GONE);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {}
                });
    }

    private void subirImagen(Uri uri) {
        try {
            File file = new File(uri.getPath());
            RequestBody req = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("imagen", file.getName(), req);

            Snackbar.make(rvGaleria, "Subiendo imagen...", Snackbar.LENGTH_SHORT).show();

            RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                    .subirImagen(restauranteId, part)
                    .enqueue(new Callback<Map<String, Object>>() {
                        @Override
                        public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                            if (response.isSuccessful()) {
                                Snackbar.make(rvGaleria, "Imagen subida", Snackbar.LENGTH_SHORT).show();
                                cargarImagenes();
                            } else {
                                Snackbar.make(rvGaleria, "Error al subir", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Map<String, Object>> call, Throwable t) {}
                    });
        } catch (Exception e) {
            Snackbar.make(rvGaleria, "Error al procesar imagen", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void confirmarEliminar(Map<String, Object> img) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar imagen")
                .setMessage("\u00bfEst\u00e1s seguro?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarImagen(img))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarImagen(Map<String, Object> img) {
        Object idObj = img.get("id");
        int imgId = idObj instanceof Double ? ((Double) idObj).intValue() : 0;

        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .eliminarImagen(imgId)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful()) {
                            Snackbar.make(rvGaleria, "Imagen eliminada", Snackbar.LENGTH_SHORT).show();
                            cargarImagenes();
                        } else {
                            Snackbar.make(rvGaleria, "Error al eliminar", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {}
                });
    }

    static class GaleriaAdminAdapter extends RecyclerView.Adapter<GaleriaAdminAdapter.ViewHolder> {
        private final List<Map<String, Object>> items;
        private final OnDeleteListener listener;
        interface OnDeleteListener { void onDelete(Map<String, Object> img); }

        GaleriaAdminAdapter(List<Map<String, Object>> items, OnDeleteListener l) {
            this.items = items;
            this.listener = l;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_galeria_restaurante, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Map<String, Object> img = items.get(position);
            String ruta = (String) img.get("ruta");
            String url = ruta != null ? NetworkConfig.STORAGE_URL + ruta : null;
            if (url != null) {
                Glide.with(holder.ivImagen.getContext()).load(url)
                        .placeholder(android.R.color.darker_gray).into(holder.ivImagen);
            }
            holder.ivImagen.setOnLongClickListener(v -> {
                listener.onDelete(img);
                return true;
            });
        }

        @Override
        public int getItemCount() { return items.size(); }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivImagen;
            ViewHolder(View v) {
                super(v);
                ivImagen = v.findViewById(R.id.ivGaleria);
            }
        }
    }
}


