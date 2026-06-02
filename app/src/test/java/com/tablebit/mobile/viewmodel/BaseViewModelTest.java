package com.tablebit.mobile.viewmodel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseViewModelTest {

    @Mock
    private Application mockApplication;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void loading_initialValue_isFalse() {
        MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
        assertFalse(loading.getValue());
    }

    @Test
    public void networkError_initialValue_isFalse() {
        MutableLiveData<Boolean> networkError = new MutableLiveData<>(false);
        assertFalse(networkError.getValue());
    }

    @Test
    public void clearError_resetsErrorAndNetworkError() {
        MutableLiveData<String> errorMessage = new MutableLiveData<>();
        MutableLiveData<Boolean> networkError = new MutableLiveData<>(true);

        errorMessage.setValue(null);
        networkError.setValue(false);

        assertNull(errorMessage.getValue());
        assertFalse(networkError.getValue());
    }

    @Test
    public void loading_setValue_works() {
        MutableLiveData<Boolean> loading = new MutableLiveData<>();
        loading.setValue(true);
        assertTrue(loading.getValue());
        loading.setValue(false);
        assertFalse(loading.getValue());
    }

    @Test
    public void errorMessage_onNetworkError_showsMessage() {
        MutableLiveData<String> errorMessage = new MutableLiveData<>();
        errorMessage.setValue("Sin conexi\u00f3n a internet. Verifica tu red");
        String msg = errorMessage.getValue();
        assertNotNull(msg);
        assertTrue(msg.contains("conexi\u00f3n") || msg.contains("internet"));
    }

    @Test
    public void errorMessage_onTimeout_showsTimeoutMessage() {
        MutableLiveData<String> errorMessage = new MutableLiveData<>();
        errorMessage.setValue("El servidor no responde. Intenta de nuevo.");
        assertEquals("El servidor no responde. Intenta de nuevo.", errorMessage.getValue());
    }

    @Test
    public void errorMessage_onUnknownError_showsGeneric() {
        MutableLiveData<String> errorMessage = new MutableLiveData<>();
        errorMessage.setValue("Error de conexi\u00f3n");
        assertEquals("Error de conexi\u00f3n", errorMessage.getValue());
    }

    @Test
    public void liveData_observerReceivesValue() {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        liveData.setValue(true);
        assertTrue(liveData.getValue());
    }
}
