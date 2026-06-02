package com.tablebit.mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.data.repository.RestauranteRepository;
import com.tablebit.mobile.session.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestauranteViewModel extends AndroidViewModel {

    private final RestauranteRepository repository;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Restaurante>> restaurantes = new MutableLiveData<>(new ArrayList<>());

    public RestauranteViewModel(@NonNull Application application) {
        super(application);
        SessionManager sessionManager = new SessionManager(application);
        this.repository = new RestauranteRepository(sessionManager.getTokenManager());
    }

    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<List<Restaurante>> getRestaurantes() { return restaurantes; }

    public void loadRestaurantes() {
        loading.setValue(true);
        errorMessage.setValue(null);

        repository.getRestaurantes().enqueue(new Callback<List<Restaurante>>() {
            @Override
            public void onResponse(Call<List<Restaurante>> call, Response<List<Restaurante>> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    restaurantes.setValue(response.body());
                } else {
                    restaurantes.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Restaurante>> call, Throwable t) {
                loading.setValue(false);
                errorMessage.setValue("Error de conexi\u00f3n");
            }
        });
    }
}
