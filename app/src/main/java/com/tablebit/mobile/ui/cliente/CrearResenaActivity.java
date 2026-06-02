package com.tablebit.mobile.ui.cliente;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.session.SessionManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearResenaActivity extends AppCompatActivity {

    private int restauranteId;
    private RatingBar ratingBar;
    private TextInputEditText etComentario;
    private MaterialButton btnPublicar;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_resena);

        restauranteId = getIntent().getIntExtra("restaurante_id", -1);
        sessionManager = new SessionManager(this);

        ratingBar = findViewById(R.id.ratingBar);
        etComentario = findViewById(R.id.etComentario);
        btnPublicar = findViewById(R.id.btnPublicar);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        btnPublicar.setOnClickListener(v -> publicarResena());
    }

    private void publicarResena() {
        int rating = (int) ratingBar.getRating();
        String comentario = etComentario.getText().toString().trim();

        if (rating == 0) {
            Snackbar.make(btnPublicar, "Selecciona una calificaci\u00f3n", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (comentario.isEmpty()) {
            Snackbar.make(btnPublicar, "Escribe un comentario", Snackbar.LENGTH_SHORT).show();
            return;
        }

        btnPublicar.setEnabled(false);
        btnPublicar.setText("Publicando...");

        Map<String, Object> body = new HashMap<>();
        body.put("rating", rating);
        body.put("comentario", comentario);

        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .crearResena(restauranteId, body)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        btnPublicar.setEnabled(true);
                        btnPublicar.setText("Publicar rese\u00f1a");
                        if (response.isSuccessful()) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Snackbar.make(btnPublicar, "Error al publicar rese\u00f1a", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        btnPublicar.setEnabled(true);
                        btnPublicar.setText("Publicar rese\u00f1a");
                        Snackbar.make(btnPublicar, "Error de conexi\u00f3n", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
}
