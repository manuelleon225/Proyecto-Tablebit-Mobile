package com.tablebit.mobile.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tablebit.mobile.data.model.Reserva;
import com.tablebit.mobile.data.repository.ReservaRepository;
import com.tablebit.mobile.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservaViewModel extends AndroidViewModel {

    private final ReservaRepository repository;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Reserva>> reservas = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> reservaCreada = new MutableLiveData<>();
    private final MutableLiveData<Boolean> reservaCancelada = new MutableLiveData<>();

    public ReservaViewModel(@NonNull Application application) {
        super(application);
        SessionManager sessionManager = new SessionManager(application);
        this.repository = new ReservaRepository(sessionManager.getTokenManager());
    }

    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<List<Reserva>> getReservas() { return reservas; }
    public LiveData<Boolean> getReservaCreada() { return reservaCreada; }
    public LiveData<Boolean> getReservaCancelada() { return reservaCancelada; }

    public void loadReservas() {
        loading.setValue(true);
        errorMessage.setValue(null);

        repository.getMisReservas().enqueue(new Callback<List<Reserva>>() {
            @Override
            public void onResponse(Call<List<Reserva>> call, Response<List<Reserva>> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    reservas.setValue(response.body());
                } else {
                    reservas.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Reserva>> call, Throwable t) {
                loading.setValue(false);
                errorMessage.setValue("Error de conexi\u00f3n");
            }
        });
    }

    public void crearReserva(int restauranteId, String fecha, String hora, int cantidadPersonas) {
        loading.setValue(true);
        errorMessage.setValue(null);

        repository.crearReserva(restauranteId, fecha, hora, cantidadPersonas)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        loading.setValue(false);
                        if (response.isSuccessful()) {
                            reservaCreada.setValue(true);
                        } else {
                            try {
                                if (response.errorBody() != null) {
                                    String body = response.errorBody().string();
                                    Log.e("RESERVA_ERROR", "C\u00f3digo: " + response.code() + " | Cuerpo: " + body);
                                    JSONObject json = new JSONObject(body);
                                    if (json.has("message")) {
                                        errorMessage.setValue(json.getString("message"));
                                        return;
                                    }
                                }
                            } catch (JSONException | IOException ignored) {}
                            errorMessage.setValue("Error al crear reserva");
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        loading.setValue(false);
                        errorMessage.setValue("Error de conexi\u00f3n");
                    }
                });
    }

    public void cancelarReserva(int reservaId) {
        loading.setValue(true);
        errorMessage.setValue(null);

        repository.cancelarReserva(reservaId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                loading.setValue(false);
                if (response.isSuccessful()) {
                    reservaCancelada.setValue(true);
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String body = response.errorBody().string();
                            JSONObject json = new JSONObject(body);
                            if (json.has("message")) {
                                errorMessage.setValue(json.getString("message"));
                                return;
                            }
                        }
                    } catch (JSONException | IOException ignored) {}
                    errorMessage.setValue("Error al cancelar reserva");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                loading.setValue(false);
                errorMessage.setValue("Error de conexi\u00f3n");
            }
        });
    }
}
