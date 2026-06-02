package com.tablebit.mobile.ui.cliente;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MisReservasPagerAdapter extends FragmentStateAdapter {

    public MisReservasPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new ReservasProximasFragment();
            case 1: return new ReservasPasadasFragment();
            case 2:
                ReservasPasadasFragment f = new ReservasPasadasFragment();
                f.setFilterCanceladas(true);
                return f;
            default: return new ReservasProximasFragment();
        }
    }

    @Override
    public int getItemCount() { return 3; }
}
