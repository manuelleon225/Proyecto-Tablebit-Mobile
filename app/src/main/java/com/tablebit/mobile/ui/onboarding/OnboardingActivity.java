package com.tablebit.mobile.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.tablebit.mobile.MainActivity;
import com.tablebit.mobile.R;
import com.tablebit.mobile.session.SessionManager;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 vpOnboarding;
    private LinearLayout indicadorDots;
    private MaterialButton btnSkip, btnNext;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_onboarding);

        vpOnboarding = findViewById(R.id.vpOnboarding);
        indicadorDots = findViewById(R.id.indicadorDots);
        btnSkip = findViewById(R.id.btnSkip);
        btnNext = findViewById(R.id.btnNext);

        vpOnboarding.setAdapter(new OnboardingAdapter());
        crearIndicadores();

        vpOnboarding.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                actualizarIndicadores(position);
                btnNext.setText(position == 2 ? "Comenzar" : "Siguiente");
            }
        });

        btnNext.setOnClickListener(v -> {
            if (vpOnboarding.getCurrentItem() < 2) {
                vpOnboarding.setCurrentItem(vpOnboarding.getCurrentItem() + 1);
            } else {
                startActivity(new Intent(this, com.tablebit.mobile.ui.auth.LoginActivity.class));
                finish();
            }
        });

        btnSkip.setOnClickListener(v -> {
            startActivity(new Intent(this, com.tablebit.mobile.ui.auth.LoginActivity.class));
            finish();
        });
    }

    private void crearIndicadores() {
        for (int i = 0; i < 3; i++) {
            TextView dot = new TextView(this);
            dot.setText("\u2B24");
            dot.setTextSize(10);
            dot.setPadding(8, 0, 8, 0);
            dot.setTextColor(ContextCompat.getColor(this, i == 0 ? R.color.primary : R.color.text_disabled));
            indicadorDots.addView(dot);
        }
    }

    private void actualizarIndicadores(int pos) {
        for (int i = 0; i < indicadorDots.getChildCount(); i++) {
            ((TextView) indicadorDots.getChildAt(i)).setTextColor(
                    ContextCompat.getColor(this, i == pos ? R.color.primary : R.color.text_disabled));
        }
    }
}
