package com.tablebit.mobile.ui.admin;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tablebit.mobile.data.model.DashboardData;
import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.data.repository.DashboardRepository;
import com.tablebit.mobile.ui.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardViewModel extends BaseViewModel {

    private final DashboardRepository repository;

    private final MutableLiveData<List<Restaurante>> restaurantes = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<DashboardData> dashboardData = new MutableLiveData<>();
    private final MutableLiveData<Restaurante> restauranteSeleccionado = new MutableLiveData<>();

    private Call<DashboardData> pendingDashboardCall;
    private int retryCount = 0;
    private static final int MAX_RETRIES = 2;

    public AdminDashboardViewModel(@NonNull Application application) {
        super(application);
        this.repository = new DashboardRepository(sessionManager.getTokenManager());
    }

    public LiveData<List<Restaurante>> getRestaurantes() { return restaurantes; }
    public LiveData<DashboardData> getDashboardData() { return dashboardData; }
    public LiveData<Restaurante> getRestauranteSeleccionado() { return restauranteSeleccionado; }

    public void loadRestaurantes() {
        loading.setValue(true);

        repository.getMisRestaurantes().enqueue(new Callback<List<Restaurante>>() {
            @Override
            public void onResponse(Call<List<Restaurante>> call, Response<List<Restaurante>> response) {
                loading.setValue(false);
                if (!handleApiResponse(response)) return;
                restaurantes.setValue(response.body());
                if (!response.body().isEmpty()) {
                    seleccionarRestaurante(response.body().get(0));
                }
            }

            @Override
            public void onFailure(Call<List<Restaurante>> call, Throwable t) {
                loading.setValue(false);
                if (call.isCanceled()) return;
                if (retryCount < MAX_RETRIES) {
                    retryCount++;
                    loadRestaurantes();
                } else {
                    handleFailure(t, call);
                    retryCount = 0;
                }
            }
        });
    }

    public void seleccionarRestaurante(Restaurante r) {
        if (r == null) return;
        restauranteSeleccionado.setValue(r);
        retryCount = 0;
        loadDashboard(r.getId());
    }

    public void loadDashboard(int restauranteId) {
        // Cancelar petición anterior si existe
        if (pendingDashboardCall != null && !pendingDashboardCall.isCanceled()) {
            pendingDashboardCall.cancel();
        }

        loading.setValue(true);
        errorMessage.setValue(null);

        pendingDashboardCall = repository.getDashboard(restauranteId);

        pendingDashboardCall.enqueue(new Callback<DashboardData>() {
            @Override
            public void onResponse(Call<DashboardData> call, Response<DashboardData> response) {
                loading.setValue(false);
                if (call.isCanceled()) return;
                Log.e("DASHBOARD_ERR", "C\u00f3digo: " + response.code());
                if (response.errorBody() != null) {
                    try { Log.e("DASHBOARD_ERR", "Cuerpo: " + response.errorBody().string()); } catch (Exception ignored) {}
                }
                if (!handleApiResponse(response)) return;
                dashboardData.setValue(response.body());
                retryCount = 0;
            }

            @Override
            public void onFailure(Call<DashboardData> call, Throwable t) {
                loading.setValue(false);
                if (call.isCanceled()) return;
                Log.e("DASHBOARD_ERR", "Fallo: " + t.getMessage());
                if (retryCount < MAX_RETRIES) {
                    retryCount++;
                    loadDashboard(restauranteId);
                } else {
                    handleFailure(t, call);
                    retryCount = 0;
                }
            }
        });
    }
}
