package com.tablebit.mobile.data.api;

import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.data.model.LoginRequest;
import com.tablebit.mobile.data.model.LoginResponse;
import com.tablebit.mobile.data.model.Mesa;
import com.tablebit.mobile.data.model.RegisterRequest;
import com.tablebit.mobile.data.model.Reserva;
import com.tablebit.mobile.data.model.ReservaRequest;
import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.data.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("api/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/register")
    Call<LoginResponse> register(@Body RegisterRequest request);

    @POST("api/logout")
    Call<ApiResponse<Void>> logout();

    @GET("api/restaurantes")
    Call<ApiResponse<List<Restaurante>>> getRestaurantes();

    @GET("api/restaurantes/{id}")
    Call<ApiResponse<Restaurante>> getRestaurante(@Path("id") int id);

    @GET("api/mesas/restaurante/{id}")
    Call<ApiResponse<List<Mesa>>> getMesasByRestaurante(@Path("id") int restauranteId);

    @POST("api/reserva-automatica")
    Call<ApiResponse<Reserva>> crearReserva(@Body ReservaRequest request);

    @GET("api/mis-reservas")
    Call<ApiResponse<List<Reserva>>> getMisReservas();

    @PATCH("api/reservas/{id}/cancelar")
    Call<ApiResponse<Reserva>> cancelarReserva(@Path("id") int reservaId);

    @GET("api/usuarios/me")
    Call<ApiResponse<Usuario>> getPerfil();
}
