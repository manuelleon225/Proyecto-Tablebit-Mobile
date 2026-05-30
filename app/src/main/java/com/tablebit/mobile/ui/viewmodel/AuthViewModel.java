package com.tablebit.mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tablebit.mobile.data.model.LoginResponse;
import com.tablebit.mobile.data.repository.AuthRepository;
import com.tablebit.mobile.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final SessionManager sessionManager;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<LoginResponse> loginResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logoutDone = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        this.sessionManager = new SessionManager(application);
        this.authRepository = new AuthRepository(sessionManager);
    }

    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<LoginResponse> getLoginResult() { return loginResult; }
    public LiveData<Boolean> getLogoutDone() { return logoutDone; }

    public void login(String email, String password) {
        loading.setValue(true);
        errorMessage.setValue(null);

        authRepository.login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    authRepository.saveSession(response.body());
                    loginResult.setValue(response.body());
                } else {
                    errorMessage.setValue("Credenciales inv\u00e1lidas");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loading.setValue(false);
                errorMessage.setValue("Error de conexi\u00f3n");
            }
        });
    }

    public void register(String name, String email, String password) {
        loading.setValue(true);
        errorMessage.setValue(null);

        authRepository.register(name, email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    authRepository.saveSession(response.body());
                    loginResult.setValue(response.body());
                } else {
                    errorMessage.setValue("Error al registrarse");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
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
