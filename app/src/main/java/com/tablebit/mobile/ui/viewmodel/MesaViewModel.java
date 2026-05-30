package com.tablebit.mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.data.model.Mesa;
import com.tablebit.mobile.data.repository.MesaRepository;
import com.tablebit.mobile.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MesaViewModel extends AndroidViewModel {

    private final MesaRepository repository;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Mesa>> mesas = new MutableLiveData<>(new ArrayList<>());

    public MesaViewModel(@NonNull Application application) {
        super(application);
        SessionManager sessionManager = new SessionManager(application);
        this.repository = new MesaRepository(sessionManager.getTokenManager());
    }

    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<List<Mesa>> getMesas() { return mesas; }

    public void loadMesas(int restauranteId) {
        loading.setValue(true);
        errorMessage.setValue(null);

        repository.getMesasByRestaurante(restauranteId).enqueue(new Callback<ApiResponse<List<Mesa>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Mesa>>> call, Response<ApiResponse<List<Mesa>>> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    mesas.setValue(response.body().getData());
                } else {
                    mesas.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Mesa>>> call, Throwable t) {
                loading.setValue(false);
                errorMessage.setValue("Error de conexi\u00f3n");
            }
        });
    }
}
