package com.tablebit.mobile.api;

import com.tablebit.mobile.data.api.ErrorUtils;

import org.junit.Test;

import static org.junit.Assert.*;

import retrofit2.Response;

public class ErrorUtilsTest {

    @Test
    public void getErrorMessage_nullResponse_returnsDesconocido() {
        String msg = ErrorUtils.getErrorMessage(null);
        assertEquals("Error desconocido", msg);
    }

    @Test
    public void isSessionExpired_nullResponse_returnsFalse() {
        assertFalse(ErrorUtils.isSessionExpired(null));
    }

    @Test
    public void isSessionExpired_non401_returnsFalse() {
        assertFalse(ErrorUtils.isSessionExpired(Response.error(403, okhttp3.ResponseBody.create(null, ""))));
    }

    @Test
    public void parseValidationError_nullErrorBody_returnsInvalidos() {
        retrofit2.Response<?> response = Response.error(422, null);
        String msg = ErrorUtils.parseValidationError(response);
        assertTrue(msg.contains("inv") || msg.equals("Datos inv\u00e1lidos."));
    }
}
