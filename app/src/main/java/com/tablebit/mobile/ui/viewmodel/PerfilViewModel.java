package com.tablebit.mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.data.model.Usuario;
import com.tablebit.mobile.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private final SessionManager sessionManager;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Usuario> usuario = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logoutDone = new MutableLiveData<>();

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        this.sessionManager = new SessionManager(application);
    }

    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Usuario> getUsuario() { return usuario; }
    public LiveData<Boolean> getLogoutDone() { return logoutDone; }
    public String getUserName() { return sessionManager.getUserName(); }
    public String getUserEmail() { return sessionManager.getUserEmail(); }

    public void loadPerfil() {
        loading.setValue(true);
        errorMessage.setValue(null);

        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService().getPerfil()
                .enqueue(new Callback<ApiResponse<Usuario>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Usuario>> call, Response<ApiResponse<Usuario>> response) {
                        loading.setValue(false);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            Usuario user = response.body().getData();
                            sessionManager.saveUserInfo(user.getId(), user.getName(), user.getEmail());
                            usuario.setValue(user);
                        } else {
                            errorMessage.setValue("Error al cargar perfil");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Usuario>> call, Throwable t) {
                        loading.setValue(false);
                        errorMessage.setValue("Error de conexi\u00f3n");
                    }
                });
    }

    public void logout() {
        loading.setValue(true);
        sessionManager.performLogout(getApplication());
        loading.setValue(false);
        logoutDone.setValue(true);
    }
}
