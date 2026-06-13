package com.tablebit.mobile.data.mock;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MockCall<T> implements Call<T> {

    private final T data;
    private final int statusCode;
    private final boolean isError;
    private boolean executed;
    private boolean canceled;

    public MockCall(T data) {
        this(data, 200, false);
    }

    public MockCall(T data, int statusCode) {
        this(data, statusCode, statusCode < 200 || statusCode >= 300);
    }

    private MockCall(T data, int statusCode, boolean isError) {
        this.data = data;
        this.statusCode = statusCode;
        this.isError = isError;
    }

    private Response<T> buildResponse() {
        if (isError) {
            String json = "{\"message\":\"" + getErrorMessage() + "\"}";
            ResponseBody errorBody = ResponseBody.create(MediaType.parse("application/json"), json);
            return Response.error(statusCode, errorBody);
        }
        return Response.success(data);
    }

    private String getErrorMessage() {
        switch (statusCode) {
            case 401: return "Credenciales inv\u00e1lidas";
            case 403: return "No tienes permisos para esta acci\u00f3n";
            case 404: return "Recurso no encontrado";
            case 422: return "Datos inv\u00e1lidos";
            case 500: return "Error interno del servidor";
            default: return "Error (" + statusCode + ")";
        }
    }

    @Override
    public Response<T> execute() throws IOException {
        executed = true;
        return buildResponse();
    }

    @Override
    public void enqueue(Callback<T> callback) {
        executed = true;
        if (!canceled && callback != null) {
            callback.onResponse(this, buildResponse());
        }
    }

    @Override
    public boolean isExecuted() { return executed; }

    @Override
    public void cancel() { canceled = true; }

    @Override
    public boolean isCanceled() { return canceled; }

    @Override
    public Call<T> clone() { return new MockCall<>(data, statusCode); }

    @Override
    public Request request() { return new Request.Builder().url("http://mock").tag("mock").build(); }

    @Override
    public Timeout timeout() { return Timeout.NONE; }
}
