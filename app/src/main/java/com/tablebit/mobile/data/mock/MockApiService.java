package com.tablebit.mobile.data.mock;

import com.tablebit.mobile.data.api.ApiService;
import com.tablebit.mobile.data.model.CalendarioResponse;
import com.tablebit.mobile.data.model.DashboardData;
import com.tablebit.mobile.data.model.LoginRequest;
import com.tablebit.mobile.data.model.LoginResponse;
import com.tablebit.mobile.data.model.Mesa;
import com.tablebit.mobile.data.model.MesaEstadoRequest;
import com.tablebit.mobile.data.model.RegisterRequest;
import com.tablebit.mobile.data.model.Reserva;
import com.tablebit.mobile.data.model.ReservaRequest;
import com.tablebit.mobile.data.model.ReservasResponse;
import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.data.model.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;

public class MockApiService implements ApiService {

    @Override
    public Call<LoginResponse> login(LoginRequest request) {
        LoginResponse response = new LoginResponse();
        String email = request.getEmail() != null ? request.getEmail() : "";
        String password = request.getPassword() != null ? request.getPassword() : "";

        if (email.isEmpty() || password.isEmpty()) {
            return new MockCall<>(null, 422);
        }

        boolean passOk = (email.equalsIgnoreCase("carlos@tablebit.com") && password.equals(MockData.CARLOS_PASS))
                      || (email.equalsIgnoreCase("maria@email.com") && password.equals(MockData.MARIA_PASS))
                      || (!email.equalsIgnoreCase("carlos@tablebit.com") && !email.equalsIgnoreCase("maria@email.com") && password.equals("demo123"));

        if (!passOk) {
            return new MockCall<>(null, 401);
        }

        response.setToken("demo-token-" + email.replaceAll("[^a-zA-Z0-9]", ""));

        if (email.equalsIgnoreCase("carlos@tablebit.com")) {
            response.setUser(MockData.createCarlos());
        } else if (email.equalsIgnoreCase("maria@email.com")) {
            Usuario user = new Usuario();
            user.setId(2);
            user.setName("María García");
            user.setEmail("maria@email.com");
            user.setRole("cliente");
            user.setAvatar(null);
            user.setEstado("activo");
            user.setTelefono("3109876543");
            response.setUser(user);
        } else {
            Usuario user = new Usuario();
            user.setId(99);
            user.setName(email.split("@")[0]);
            user.setEmail(email);
            user.setRole("cliente");
            user.setAvatar(null);
            user.setEstado("activo");
            response.setUser(user);
        }
        return new MockCall<>(response);
    }

    @Override
    public Call<LoginResponse> register(RegisterRequest request) {
        LoginResponse response = new LoginResponse();
        response.setToken("demo-token-new-user");
        Usuario user = new Usuario();
        user.setId(10);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole("cliente");
        user.setEstado("activo");
        response.setUser(user);
        return new MockCall<>(response);
    }

    @Override
    public Call<Map<String, Object>> logout() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Sesión cerrada exitosamente");
        return new MockCall<>(map);
    }

    @Override
    public Call<List<Restaurante>> getRestaurantes() {
        return new MockCall<>(MockData.createRestaurantes());
    }

    @Override
    public Call<List<Restaurante>> buscarRestaurantes(String query) {
        List<Restaurante> filtered = new ArrayList<>();
        for (Restaurante r : MockData.createRestaurantes()) {
            if (r.getNombre().toLowerCase().contains(query.toLowerCase()) ||
                r.getTipoComida().toLowerCase().contains(query.toLowerCase()) ||
                r.getCiudad().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(r);
            }
        }
        return new MockCall<>(filtered);
    }

    @Override
    public Call<List<Restaurante>> getMisRestaurantes() {
        return new MockCall<>(MockData.createRestaurantes());
    }

    @Override
    public Call<Map<String, Object>> getRestaurante(int id) {
        Map<String, Object> map = new HashMap<>();
        for (Restaurante r : MockData.createRestaurantes()) {
            if (r.getId() == id) {
                map.put("id", r.getId());
                map.put("nombre", r.getNombre());
                map.put("slug", r.getSlug());
                map.put("direccion", r.getDireccion());
                map.put("ciudad", r.getCiudad());
                map.put("telefono", r.getTelefono());
                map.put("descripcion", r.getDescripcion());
                map.put("tipo_comida", r.getTipoComida());
                map.put("capacidad_total", r.getCapacidadTotal());
                map.put("horario_apertura", r.getHorarioApertura());
                map.put("horario_cierre", r.getHorarioCierre());
                map.put("imagen", r.getImagen());
                map.put("logo", r.getLogo());
                map.put("banner", r.getBanner());
                map.put("estado", r.getEstado());
                map.put("resenas_avg_rating", r.getResenasAvgRating());
                map.put("resenas_count", r.getResenasCount());
                map.put("abierto_ahora", r.getAbiertoAhora());
                break;
            }
        }
        return new MockCall<>(map);
    }

    @Override
    public Call<List<Mesa>> getMesasByRestaurante(int restauranteId) {
        return new MockCall<>(MockData.createMesas(restauranteId));
    }

    @Override
    public Call<Map<String, Object>> updateMesa(int id, MesaEstadoRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("estado", request.getEstado());
        map.put("message", "Mesa actualizada exitosamente");
        return new MockCall<>(map);
    }

    @Override
    public Call<Map<String, Object>> createMesa(Map<String, Object> body) {
        Map<String, Object> map = new HashMap<>(body);
        map.put("id", 999);
        map.put("message", "Mesa creada exitosamente");
        return new MockCall<>(map);
    }

    @Override
    public Call<Map<String, Object>> deleteMesa(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Mesa eliminada exitosamente");
        return new MockCall<>(map);
    }

    @Override
    public Call<Map<String, Object>> getReservaDetail(int id) {
        return new MockCall<>(MockData.createReservaDetail(id));
    }

    @Override
    public Call<Map<String, Object>> cambiarEstadoReserva(int id, Map<String, Object> body) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("estado", body.get("estado"));
        map.put("message", "Estado de reserva actualizado");
        return new MockCall<>(map);
    }

    @Override
    public Call<Map<String, Object>> crearReserva(ReservaRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 100);
        map.put("restaurante_id", request.getRestauranteId());
        map.put("fecha", request.getFecha());
        map.put("hora", request.getHora());
        map.put("cantidad_personas", request.getCantidadPersonas());
        map.put("estado", "pendiente");
        map.put("message", "Reserva creada exitosamente");
        return new MockCall<>(map);
    }

    @Override
    public Call<List<Reserva>> getMisReservas() {
        return new MockCall<>(MockData.createReservas());
    }

    @Override
    public Call<Map<String, Object>> cancelarReserva(int reservaId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", reservaId);
        map.put("estado", "cancelada");
        map.put("message", "Reserva cancelada exitosamente");
        return new MockCall<>(map);
    }

    @Override
    public Call<Usuario> getPerfil() {
        return new MockCall<>(MockData.createCarlos());
    }

    @Override
    public Call<Usuario> updatePerfil(Map<String, Object> body) {
        Usuario user = MockData.createCarlos();
        if (body.containsKey("name")) user.setName((String) body.get("name"));
        if (body.containsKey("telefono")) user.setTelefono((String) body.get("telefono"));
        return new MockCall<>(user);
    }

    @Override
    public Call<Map<String, Object>> uploadAvatar(MultipartBody.Part avatar) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Avatar actualizado exitosamente");
        map.put("avatar_url", "https://i.pravatar.cc/150?u=demo");
        return new MockCall<>(map);
    }

    @Override
    public Call<Map<String, Object>> toggleFavorito(int restauranteId) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Favorito actualizado");
        map.put("favorito", true);
        return new MockCall<>(map);
    }

    @Override
    public Call<List<Map<String, Object>>> getFavoritos() {
        return new MockCall<>(MockData.createFavoritos());
    }

    @Override
    public Call<Map<String, Object>> verificarFavorito(int restauranteId) {
        Map<String, Object> map = new HashMap<>();
        map.put("favorito", restauranteId == 1 || restauranteId == 3);
        return new MockCall<>(map);
    }

    @Override
    public Call<DashboardData> getDashboard(int restauranteId) {
        return new MockCall<>(MockData.createDashboardData());
    }

    @Override
    public Call<CalendarioResponse> getCalendario(int restauranteId, String inicio, String fin) {
        return new MockCall<>(MockData.createCalendarioEventos());
    }

    @Override
    public Call<ReservasResponse> getReservasAdmin(int restauranteId, String fecha) {
        ReservasResponse response = new ReservasResponse();
        List<Reserva> reservas = MockData.createReservas();
        response.setData(reservas);
        response.setTotal(reservas.size());
        return new MockCall<>(response);
    }

    @Override
    public Call<List<Map<String, Object>>> getHorarios(int restauranteId) {
        return new MockCall<>(MockData.createHorarios());
    }

    @Override
    public Call<List<Map<String, Object>>> updateHorarios(int restauranteId, List<Map<String, Object>> horarios) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Horarios actualizados exitosamente");
        List<Map<String, Object>> result = new ArrayList<>();
        result.add(map);
        return new MockCall<>(result);
    }

    @Override
    public Call<Map<String, Object>> crearResena(int restauranteId, Map<String, Object> body) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 999);
        map.put("restaurante_id", restauranteId);
        map.put("message", "Reseña creada exitosamente");
        return new MockCall<>(map);
    }

    @Override
    public Call<Map<String, Object>> subirImagen(int restauranteId, MultipartBody.Part imagen) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 999);
        map.put("url", "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4");
        map.put("message", "Imagen subida exitosamente");
        return new MockCall<>(map);
    }

    @Override
    public Call<List<Map<String, Object>>> getImagenes(int restauranteId) {
        return new MockCall<>(MockData.createImagenes());
    }

    @Override
    public Call<Map<String, Object>> eliminarImagen(int imagenId) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Imagen eliminada exitosamente");
        return new MockCall<>(map);
    }
}
