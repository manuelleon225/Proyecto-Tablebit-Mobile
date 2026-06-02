package com.tablebit.mobile.ui.cliente;

import android.os.Bundle;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tablebit.mobile.R;
import com.tablebit.mobile.ui.BaseActivity;

public class MisReservasActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_reservas);

        setupToolbarWithBack(getString(R.string.titulo_reservas));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        MisReservasPagerAdapter adapter = new MisReservasPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            String[] titles = {"Pr\u00f3ximas", "Pasadas", "Canceladas"};
            tab.setText(titles[position]);
        }).attach();
    }
}
