package com.tablebit.mobile.data.api;

import com.tablebit.mobile.data.model.CalendarioResponse;
import com.tablebit.mobile.data.model.DashboardData;
import com.tablebit.mobile.data.model.ReservasResponse;
import com.tablebit.mobile.data.model.LoginRequest;
import com.tablebit.mobile.data.model.LoginResponse;
import com.tablebit.mobile.data.model.Mesa;
import com.tablebit.mobile.data.model.MesaEstadoRequest;
import com.tablebit.mobile.data.model.RegisterRequest;
import com.tablebit.mobile.data.model.Reserva;
import com.tablebit.mobile.data.model.ReservaRequest;
import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.data.model.Usuario;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/register")
    Call<LoginResponse> register(@Body RegisterRequest request);

    @POST("api/logout")
    Call<Map<String, Object>> logout();

    @GET("api/restaurantes")
    Call<List<Restaurante>> getRestaurantes();

    @GET("api/buscar-restaurantes")
    Call<List<Restaurante>> buscarRestaurantes(@Query("q") String query);

    @GET("api/mis-restaurantes")
    Call<List<Restaurante>> getMisRestaurantes();

    @GET("api/restaurantes/{id}")
    Call<Map<String, Object>> getRestaurante(@Path("id") int id);

    @GET("api/mesas/restaurante/{restauranteId}")
    Call<List<Mesa>> getMesasByRestaurante(@Path("restauranteId") int restauranteId);

    @PUT("api/mesas/{id}")
    Call<Map<String, Object>> updateMesa(@Path("id") int id, @Body MesaEstadoRequest request);

    @POST("api/mesas")
    Call<Map<String, Object>> createMesa(@Body Map<String, Object> body);

    @DELETE("api/mesas/{id}")
    Call<Map<String, Object>> deleteMesa(@Path("id") int id);

    @GET("api/reservas/{id}")
    Call<Map<String, Object>> getReservaDetail(@Path("id") int id);

    @PATCH("api/reservas/{id}/estado")
    Call<Map<String, Object>> cambiarEstadoReserva(@Path("id") int id, @Body Map<String, Object> body);

    @POST("api/reserva-automatica")
    Call<Map<String, Object>> crearReserva(@Body ReservaRequest request);

    @GET("api/mis-reservas")
    Call<List<Reserva>> getMisReservas();

    @PATCH("api/reservas/{id}/cancelar")
    Call<Map<String, Object>> cancelarReserva(@Path("id") int reservaId);

    @GET("api/usuarios/me")
    Call<Usuario> getPerfil();

    @PUT("api/usuarios/me")
    Call<Usuario> updatePerfil(@Body Map<String, Object> body);

    @Multipart
    @POST("api/profile/avatar")
    Call<Map<String, Object>> uploadAvatar(@Part okhttp3.MultipartBody.Part avatar);

    @POST("api/favoritos/{restauranteId}")
    Call<Map<String, Object>> toggleFavorito(@Path("restauranteId") int restauranteId);

    @GET("api/favoritos")
    Call<List<Map<String, Object>>> getFavoritos();

    @GET("api/favoritos/verificar/{restauranteId}")
    Call<Map<String, Object>> verificarFavorito(@Path("restauranteId") int restauranteId);

    @GET("api/dashboard/restaurante/{id}")
    Call<DashboardData> getDashboard(@Path("id") int restauranteId);

    @GET("api/calendario/restaurante/{id}")
    Call<CalendarioResponse> getCalendario(@Path("id") int restauranteId, @Query("fecha_inicio") String inicio, @Query("fecha_fin") String fin);

    @GET("api/reservas")
    Call<ReservasResponse> getReservasAdmin(@Query("restaurante_id") int restauranteId, @Query("fecha") String fecha);

    @GET("api/restaurantes/{id}/hours")
    Call<List<Map<String, Object>>> getHorarios(@Path("id") int restauranteId);

    @PUT("api/restaurantes/{id}/hours")
    Call<List<Map<String, Object>>> updateHorarios(@Path("id") int restauranteId, @Body List<Map<String, Object>> horarios);

    @POST("api/restaurantes/{restauranteId}/resenas")
    Call<Map<String, Object>> crearResena(@Path("restauranteId") int restauranteId, @Body Map<String, Object> body);

    @Multipart
    @POST("api/restaurantes/{id}/imagenes")
    Call<Map<String, Object>> subirImagen(@Path("id") int restauranteId, @Part MultipartBody.Part imagen);

    @GET("api/restaurantes/{id}/imagenes")
    Call<List<Map<String, Object>>> getImagenes(@Path("id") int restauranteId);

    @DELETE("api/imagenes/{id}")
    Call<Map<String, Object>> eliminarImagen(@Path("id") int imagenId);
}
