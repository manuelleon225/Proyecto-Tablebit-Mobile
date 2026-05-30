package com.tablebit.mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.data.repository.RestauranteRepository;
import com.tablebit.mobile.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestauranteViewModel extends AndroidViewModel {

    private final RestauranteRepository repository;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Restaurante>> restaurantes = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Restaurante> restauranteDetalle = new MutableLiveData<>();

    public RestauranteViewModel(@NonNull Application application) {
        super(application);
        SessionManager sessionManager = new SessionManager(application);
        this.repository = new RestauranteRepository(sessionManager.getTokenManager());
    }

    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<List<Restaurante>> getRestaurantes() { return restaurantes; }
    public LiveData<Restaurante> getRestauranteDetalle() { return restauranteDetalle; }

    public void loadRestaurantes() {
        loading.setValue(true);
        errorMessage.setValue(null);

        repository.getRestaurantes().enqueue(new Callback<ApiResponse<List<Restaurante>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Restaurante>>> call, Response<ApiResponse<List<Restaurante>>> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    restaurantes.setValue(response.body().getData());
                } else {
                    restaurantes.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Restaurante>>> call, Throwable t) {
                loading.setValue(false);
                errorMessage.setValue("Error de conexi\u00f3n");
            }
        });
    }

    public void loadDetalle(int id) {
        loading.setValue(true);
        errorMessage.setValue(null);

        repository.getRestaurante(id).enqueue(new Callback<ApiResponse<Restaurante>>() {
            @Override
            public void onResponse(Call<ApiResponse<Restaurante>> call, Response<ApiResponse<Restaurante>> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    restauranteDetalle.setValue(response.body().getData());
                } else {
                    errorMessage.setValue("Restaurante no encontrado");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Restaurante>> call, Throwable t) {
                loading.setValue(false);
                errorMessage.setValue("Error de conexi\u00f3n");
            }
        });
    }
}
