package com.tablebit.mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.data.model.Reserva;
import com.tablebit.mobile.data.repository.ReservaRepository;
import com.tablebit.mobile.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservaViewModel extends AndroidViewModel {

    private final ReservaRepository repository;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Reserva>> reservas = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Reserva> reservaCreada = new MutableLiveData<>();
    private final MutableLiveData<Boolean> reservaCancelada = new MutableLiveData<>();

    public ReservaViewModel(@NonNull Application application) {
        super(application);
        SessionManager sessionManager = new SessionManager(application);
        this.repository = new ReservaRepository(sessionManager.getTokenManager());
    }

    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<List<Reserva>> getReservas() { return reservas; }
    public LiveData<Reserva> getReservaCreada() { return reservaCreada; }
    public LiveData<Boolean> getReservaCancelada() { return reservaCancelada; }

    public void loadReservas() {
        loading.setValue(true);
        errorMessage.setValue(null);

        repository.getMisReservas().enqueue(new Callback<ApiResponse<List<Reserva>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Reserva>>> call, Response<ApiResponse<List<Reserva>>> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    reservas.setValue(response.body().getData());
                } else {
                    reservas.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Reserva>>> call, Throwable t) {
                loading.setValue(false);
                errorMessage.setValue("Error de conexi\u00f3n");
            }
        });
    }

    public void crearReserva(int restauranteId, int mesaId, String fecha, String hora, int personas) {
        loading.setValue(true);
        errorMessage.setValue(null);

        repository.crearReserva(restauranteId, mesaId, fecha, hora, personas)
                .enqueue(new Callback<ApiResponse<Reserva>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Reserva>> call, Response<ApiResponse<Reserva>> response) {
                        loading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            reservaCreada.setValue(response.body().getData());
                        } else {
                            errorMessage.setValue("Error al crear reserva");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Reserva>> call, Throwable t) {
                        loading.setValue(false);
                        errorMessage.setValue("Error de conexi\u00f3n");
                    }
                });
    }

    public void cancelarReserva(int reservaId) {
        loading.setValue(true);
        errorMessage.setValue(null);

        repository.cancelarReserva(reservaId).enqueue(new Callback<ApiResponse<Reserva>>() {
            @Override
            public void onResponse(Call<ApiResponse<Reserva>> call, Response<ApiResponse<Reserva>> response) {
                loading.setValue(false);
                if (response.isSuccessful()) {
                    reservaCancelada.setValue(true);
                } else {
                    errorMessage.setValue("Error al cancelar reserva");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Reserva>> call, Throwable t) {
                loading.setValue(false);
                errorMessage.setValue("Error de conexi\u00f3n");
            }
        });
    }
}
