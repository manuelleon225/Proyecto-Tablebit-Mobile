package com.tablebit.mobile.ui.cliente;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.tablebit.mobile.R;

public class GaleriaFullscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_fullscreen);

        String[] imagenes = getIntent().getStringArrayExtra("imagenes");
        int posicion = getIntent().getIntExtra("posicion", 0);

        if (imagenes == null || imagenes.length == 0) {
            finish();
            return;
        }

        ViewPager2 vpGaleria = findViewById(R.id.vpGaleria);
        TextView tvIndicador = findViewById(R.id.tvIndicador);
        TextView btnCerrar = findViewById(R.id.btnCerrarGaleria);

        GaleriaFullscreenAdapter adapter = new GaleriaFullscreenAdapter(imagenes);
        vpGaleria.setAdapter(adapter);
        vpGaleria.setCurrentItem(posicion, false);
        tvIndicador.setText((posicion + 1) + "/" + imagenes.length);

        vpGaleria.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tvIndicador.setText((position + 1) + "/" + imagenes.length);
            }
        });

        btnCerrar.setOnClickListener(v -> finish());
    }
}
