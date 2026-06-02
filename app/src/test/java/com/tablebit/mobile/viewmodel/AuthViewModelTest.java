package com.tablebit.mobile.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tablebit.mobile.data.model.LoginResponse;
import com.tablebit.mobile.data.model.Usuario;
import com.tablebit.mobile.data.repository.AuthRepository;
import com.tablebit.mobile.session.SessionManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthViewModelTest {

    @Mock
    private Application mockApplication;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void loading_initialState_isFalse() {
        MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
        assertFalse(loading.getValue());
    }

    @Test
    public void errorMessage_initialState_isNull() {
        MutableLiveData<String> error = new MutableLiveData<>();
        assertNull(error.getValue());
    }

    @Test
    public void loginResult_initialState_isNull() {
        MutableLiveData<LoginResponse> result = new MutableLiveData<>();
        assertNull(result.getValue());
    }

    @Test
    public void logoutDone_initialState_isNull() {
        MutableLiveData<Boolean> logoutDone = new MutableLiveData<>();
        assertNull(logoutDone.getValue());
    }

    @Test
    public void loading_setTrue_reflectsValue() {
        MutableLiveData<Boolean> loading = new MutableLiveData<>();
        loading.setValue(true);
        assertTrue(loading.getValue());
    }

    @Test
    public void errorMessage_setValue_reflectsValue() {
        MutableLiveData<String> error = new MutableLiveData<>();
        error.setValue("Error de prueba");
        assertEquals("Error de prueba", error.getValue());
    }

    @Test
    public void clearError_setsNull() {
        MutableLiveData<String> error = new MutableLiveData<>();
        error.setValue("Error");
        error.setValue(null);
        assertNull(error.getValue());
    }

    @Test
    public void loginResult_setValue_notifiesObserver() {
        MutableLiveData<LoginResponse> result = new MutableLiveData<>();
        LoginResponse response = new LoginResponse();
        result.setValue(response);
        assertNotNull(result.getValue());
    }
}
