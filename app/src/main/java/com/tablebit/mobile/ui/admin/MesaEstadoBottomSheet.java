package com.tablebit.mobile.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.tablebit.mobile.R;
import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.Mesa;
import com.tablebit.mobile.data.model.MesaEstadoRequest;
import com.tablebit.mobile.session.SessionManager;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MesaEstadoBottomSheet extends BottomSheetDialogFragment {

    private Mesa mesa;
    private SessionManager sessionManager;
    private OnEstadoChangedListener listener;

    public interface OnEstadoChangedListener {
        void onEstadoChanged();
    }

    public MesaEstadoBottomSheet(Mesa mesa, SessionManager sm, OnEstadoChangedListener l) {
        this.mesa = mesa;
        this.sessionManager = sm;
        this.listener = l;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_mesa_estado, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvInfo = view.findViewById(R.id.tvMesaInfo);
        tvInfo.setText("Mesa #" + mesa.getNumero() + " - Cap. " + mesa.getCapacidad());

        view.findViewById(R.id.btnDisponible).setOnClickListener(v -> cambiarEstado("disponible"));
        view.findViewById(R.id.btnOcupada).setOnClickListener(v -> cambiarEstado("ocupada"));
        view.findViewById(R.id.btnMantenimiento).setOnClickListener(v -> cambiarEstado("mantenimiento"));
    }

    private void cambiarEstado(String nuevoEstado) {
        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .updateMesa(mesa.getId(), new MesaEstadoRequest(nuevoEstado))
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Mesa " + mesa.getNumero() + ": " + nuevoEstado, Toast.LENGTH_SHORT).show();
                            if (listener != null) listener.onEstadoChanged();
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), "Error al cambiar estado", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast.makeText(getContext(), R.string.error_red, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
