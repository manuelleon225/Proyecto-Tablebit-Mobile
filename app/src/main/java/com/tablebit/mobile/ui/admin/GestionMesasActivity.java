package com.tablebit.mobile.ui.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.Mesa;
import com.tablebit.mobile.session.SessionManager;
import com.tablebit.mobile.ui.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GestionMesasActivity extends BaseActivity {

    private RecyclerView rvMesas;
    private MesaAdminAdapter adapter;
    private final List<Mesa> mesaList = new ArrayList<>();
    private SessionManager sessionManager;
    private int restauranteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_mesas);

        restauranteId = getIntent().getIntExtra("restaurante_id", -1);
        String nombre = getIntent().getStringExtra("restaurante_nombre");
        sessionManager = new SessionManager(this);

        setupToolbarWithBack(nombre != null ? nombre : "Mesas");

        rvMesas = findViewById(R.id.rvMesas);
        rvMesas.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new MesaAdminAdapter(mesaList, mesa -> {
            new AlertDialog.Builder(this)
                    .setTitle("Mesa #" + mesa.getNumero())
                    .setItems(new CharSequence[]{"Cambiar estado", "Eliminar mesa"}, (dialog, which) -> {
                        if (which == 0) {
                            MesaEstadoBottomSheet bottomSheet = new MesaEstadoBottomSheet(
                                    mesa, sessionManager, this::cargarMesas);
                            bottomSheet.show(getSupportFragmentManager(), "MesaEstado");
                        } else {
                            confirmarEliminar(mesa);
                        }
                    }).show();
        });
        rvMesas.setAdapter(adapter);

        FloatingActionButton fab = new FloatingActionButton(this);
        fab.setImageResource(android.R.drawable.ic_input_add);
        fab.setOnClickListener(v -> mostrarDialogoCrear());
        ((android.widget.FrameLayout) findViewById(android.R.id.content)).addView(fab,
                new android.widget.FrameLayout.LayoutParams(
                        android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                        android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                        android.view.Gravity.BOTTOM | android.view.Gravity.END));
        ((android.widget.FrameLayout.LayoutParams) fab.getLayoutParams()).setMargins(0, 0, 16, 16);

        cargarMesas();
    }

    private void mostrarDialogoCrear() {
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(32, 16, 32, 16);

        com.google.android.material.textfield.TextInputLayout inputNum = new com.google.android.material.textfield.TextInputLayout(this);
        inputNum.setHint("N\u00famero de mesa");
        com.google.android.material.textfield.TextInputEditText etNum = new com.google.android.material.textfield.TextInputEditText(this);
        inputNum.addView(etNum);

        com.google.android.material.textfield.TextInputLayout inputCap = new com.google.android.material.textfield.TextInputLayout(this);
        inputCap.setHint("Capacidad");
        com.google.android.material.textfield.TextInputEditText etCap = new com.google.android.material.textfield.TextInputEditText(this);
        etCap.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        inputCap.addView(etCap);

        layout.addView(inputNum);
        layout.addView(inputCap);

        new AlertDialog.Builder(this)
                .setTitle("Nueva mesa")
                .setView(layout)
                .setPositiveButton("Crear", (dialog, which) -> {
                    String num = etNum.getText().toString().trim();
                    String cap = etCap.getText().toString().trim();
                    if (num.isEmpty() || cap.isEmpty()) {
                        Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Map<String, Object> body = new HashMap<>();
                    body.put("restaurante_id", restauranteId);
                    body.put("numero", Integer.parseInt(num));
                    body.put("capacidad", Integer.parseInt(cap));
                    body.put("estado", "disponible");
                    crearMesa(body);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void crearMesa(Map<String, Object> body) {
        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .createMesa(body)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(GestionMesasActivity.this, "Mesa creada", Toast.LENGTH_SHORT).show();
                            cargarMesas();
                        } else {
                            Toast.makeText(GestionMesasActivity.this, "Error al crear", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast.makeText(GestionMesasActivity.this, "Error de conexi\u00f3n", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void confirmarEliminar(Mesa mesa) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar mesa #" + mesa.getNumero())
                .setMessage("\u00bfEst\u00e1s seguro?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarMesa(mesa.getId()))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarMesa(int mesaId) {
        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .deleteMesa(mesaId)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(GestionMesasActivity.this, "Mesa eliminada", Toast.LENGTH_SHORT).show();
                            cargarMesas();
                        } else {
                            Toast.makeText(GestionMesasActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast.makeText(GestionMesasActivity.this, "Error de conexi\u00f3n", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cargarMesas() {
        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .getMesasByRestaurante(restauranteId)
                .enqueue(new Callback<List<Mesa>>() {
                    @Override
                    public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            mesaList.clear();
                            mesaList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Mesa>> call, Throwable t) {
                        Toast.makeText(GestionMesasActivity.this, R.string.error_red, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
