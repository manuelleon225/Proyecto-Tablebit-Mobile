package com.tablebit.mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tablebit.mobile.data.api.ErrorUtils;
import com.tablebit.mobile.session.SessionManager;

import retrofit2.Call;
import retrofit2.Response;

public abstract class BaseViewModel extends AndroidViewModel {

    protected final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    protected final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    protected final MutableLiveData<Boolean> networkError = new MutableLiveData<>(false);
    protected SessionManager sessionManager;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        this.sessionManager = new SessionManager(application);
    }

    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getNetworkError() { return networkError; }

    protected <T> boolean handleApiResponse(Response<T> response) {
        loading.setValue(false);
        networkError.setValue(false);

        if (response.isSuccessful()) {
            T body = response.body();
            if (response.code() == 204 || body != null) return true;
        }

        if (ErrorUtils.isSessionExpired(response)) {
            ErrorUtils.handleSessionExpired(getApplication(), response);
            errorMessage.setValue("Sesi\u00f3n expirada. Inicia sesi\u00f3n nuevamente.");
        } else {
            errorMessage.setValue(ErrorUtils.getErrorMessage(response));
        }
        return false;
    }

    protected void handleFailure(Throwable t) {
        handleFailure(t, null);
    }

    protected void handleFailure(Throwable t, Call<?> call) {
        loading.setValue(false);

        // Si la llamada fue cancelada, NO mostrar error
        if (call != null && call.isCanceled()) return;

        String msg = t.getMessage();
        if (msg == null) msg = "";

        if (msg.contains("Canceled") || msg.contains("canceled") || msg.contains("cancelado")) {
            return;
        }

        networkError.setValue(true);

        if (msg.contains("Unable to resolve host") || t instanceof java.net.ConnectException) {
            errorMessage.setValue("Sin conexi\u00f3n a internet. Verifica tu red");
        } else if (msg.contains("timeout") || t instanceof java.net.SocketTimeoutException) {
            errorMessage.setValue("El servidor no responde. Intenta de nuevo.");
        } else {
            errorMessage.setValue("Error de conexi\u00f3n");
        }
    }

    public void clearError() {
        errorMessage.setValue(null);
        networkError.setValue(false);
    }
}
