package com.tablebit.mobile.ui.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tablebit.mobile.data.api.RetrofitClient;
import com.tablebit.mobile.data.model.Usuario;
import com.tablebit.mobile.session.SessionManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private final SessionManager sessionManager;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Usuario> usuario = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logoutDone = new MutableLiveData<>();
    private final MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> avatarUploading = new MutableLiveData<>(false);

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        this.sessionManager = new SessionManager(application);
    }

    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Usuario> getUsuario() { return usuario; }
    public LiveData<Boolean> getLogoutDone() { return logoutDone; }
    public LiveData<Boolean> getSaveSuccess() { return saveSuccess; }
    public LiveData<Boolean> getAvatarUploading() { return avatarUploading; }
    public String getUserName() { return sessionManager.getUserName(); }
    public String getUserEmail() { return sessionManager.getUserEmail(); }

    public void loadPerfil() {
        loading.setValue(true);
        errorMessage.setValue(null);

        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService().getPerfil()
                .enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        loading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            Usuario user = response.body();
                            sessionManager.saveUserInfo(user.getId(), user.getName(), user.getEmail(), user.getRole());
                            usuario.setValue(user);
                        } else {
                            errorMessage.setValue("Error al cargar perfil");
                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        loading.setValue(false);
                        errorMessage.setValue("Error de conexi\u00f3n");
                    }
                });
    }

    public void updatePerfil(String name, String telefono) {
        if (name == null || name.trim().isEmpty()) {
            errorMessage.setValue("El nombre es requerido");
            return;
        }

        loading.setValue(true);
        errorMessage.setValue(null);
        saveSuccess.setValue(null);

        Map<String, Object> body = new HashMap<>();
        body.put("name", name.trim());
        if (telefono != null && !telefono.trim().isEmpty()) {
            body.put("telefono", telefono.trim());
        }

        RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                .updatePerfil(body)
                .enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        loading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            Usuario user = response.body();
                            sessionManager.saveUserInfo(user.getId(), user.getName(), user.getEmail(), user.getRole());
                            usuario.setValue(user);
                            saveSuccess.setValue(true);
                        } else {
                            errorMessage.setValue("Error al guardar cambios");
                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        loading.setValue(false);
                        errorMessage.setValue("Error de conexi\u00f3n");
                    }
                });
    }

    public void uploadAvatar(Uri imageUri) {
        avatarUploading.setValue(true);
        errorMessage.setValue(null);

        try {
            File file = new File(imageUri.getPath());
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

            RetrofitClient.getInstance(sessionManager.getTokenManager()).getApiService()
                    .uploadAvatar(body)
                    .enqueue(new Callback<Map<String, Object>>() {
                        @Override
                        public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                            avatarUploading.setValue(false);
                            if (response.isSuccessful()) {
                                loadPerfil();
                            } else {
                                errorMessage.setValue("Error al subir avatar");
                            }
                        }

                        @Override
                        public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                            avatarUploading.setValue(false);
                            errorMessage.setValue("Error de conexi\u00f3n");
                        }
                    });
        } catch (Exception e) {
            avatarUploading.setValue(false);
            errorMessage.setValue("No se pudo procesar la imagen");
        }
    }

    public void logout() {
        loading.setValue(true);
        sessionManager.performLogout(getApplication());
        loading.setValue(false);
        logoutDone.setValue(true);
    }
}
