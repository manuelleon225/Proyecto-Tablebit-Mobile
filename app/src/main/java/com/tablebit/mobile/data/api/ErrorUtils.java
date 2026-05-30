package com.tablebit.mobile.data.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tablebit.mobile.session.SessionManager;

import java.util.Map;

import retrofit2.Response;

public class ErrorUtils {

    private static final String TAG = "ErrorUtils";

    public static String getErrorMessage(Response<?> response) {
        if (response == null) return "Error desconocido";

        int code = response.code();

        switch (code) {
            case 401:
                return "Sesión expirada. Inicia sesión nuevamente.";
            case 403:
                return "No tienes permisos para esta acción.";
            case 404:
                return "Recurso no encontrado.";
            case 422:
                return parseValidationError(response);
            case 429:
                return "Demasiadas solicitudes. Intenta más tarde.";
            case 500:
                return "Error interno del servidor.";
            default:
                if (code >= 400 && code < 500) {
                    return "Error de solicitud (" + code + ").";
                }
                if (code >= 500) {
                    return "Error del servidor (" + code + ").";
                }
                return null;
        }
    }

    public static String parseValidationError(Response<?> response) {
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : null;
            if (errorBody == null) return "Datos inválidos.";

            JsonObject json = new Gson().fromJson(errorBody, JsonObject.class);

            if (json.has("message")) {
                return json.get("message").getAsString();
            }

            if (json.has("errors")) {
                JsonObject errors = json.getAsJsonObject("errors");
                for (Map.Entry<String, com.google.gson.JsonElement> entry : errors.entrySet()) {
                    if (entry.getValue().isJsonArray() && entry.getValue().getAsJsonArray().size() > 0) {
                        return entry.getValue().getAsJsonArray().get(0).getAsString();
                    }
                }
            }

            return "Datos inválidos.";
        } catch (Exception e) {
            Log.e(TAG, "Error parsing validation error", e);
            return "Datos inválidos.";
        }
    }

    public static boolean isSessionExpired(Response<?> response) {
        return response != null && response.code() == 401;
    }

    public static void handleSessionExpired(Context context, Response<?> response) {
        if (isSessionExpired(response)) {
            new SessionManager(context).forceLogout(context);
        }
    }
}
