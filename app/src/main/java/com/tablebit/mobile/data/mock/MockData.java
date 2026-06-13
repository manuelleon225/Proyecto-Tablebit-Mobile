package com.tablebit.mobile.data.mock;

import com.tablebit.mobile.data.model.CalendarioResponse;
import com.tablebit.mobile.data.model.DashboardData;
import com.tablebit.mobile.data.model.Mesa;
import com.tablebit.mobile.data.model.Reserva;
import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.data.model.Usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockData {

    public static final int CARLOS_USER_ID = 1;
    public static final String CARLOS_TOKEN = "demo-token-carlos-admin";
    public static final String CARLOS_PASS = "admin123";
    public static final String MARIA_PASS = "cliente123";
    public static final String DEMO_RES = "android.resource://com.tablebit.mobile/drawable/";

    public static Usuario createCarlos() {
        Usuario user = new Usuario();
        user.setId(CARLOS_USER_ID);
        user.setName("Carlos");
        user.setEmail("carlos@tablebit.com");
        user.setRole("admin_restaurante");
        user.setAvatar(null);
        user.setEstado("activo");
        user.setTelefono("3001234567");
        return user;
    }

    public static Map<String, Object> createCarlosLoginResponse() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", CARLOS_TOKEN);
        map.put("user", createCarlos());
        return map;
    }

    public static List<Restaurante> createRestaurantes() {
        String img = DEMO_RES;

        Restaurante r1 = new Restaurante();
        r1.setId(1);
        r1.setNombre("La Trattoria di Carlos");
        r1.setSlug("la-trattoria-di-carlos");
        r1.setDireccion("Carrera 5 # 12-34, Bogotá");
        r1.setCiudad("Bogotá");
        r1.setTelefono("6011234567");
        r1.setDescripcion("Auténtica cocina italiana con ingredientes importados. Chef Carlos trae las recetas tradicionales de la Toscana.");
        r1.setTipoComida("Italiana");
        r1.setCapacidadTotal(80);
        r1.setHorarioApertura("12:00");
        r1.setHorarioCierre("23:00");
        r1.setResenasAvgRating(4.8);
        r1.setResenasCount(156);
        r1.setAbiertoAhora(true);
        r1.setImagen(img + "placeholder_rest_1");
        r1.setLogo(img + "placeholder_rest_1");
        r1.setBanner(img + "placeholder_rest_1");
        r1.setEstado("activo");

        Restaurante r2 = new Restaurante();
        r2.setId(2);
        r2.setNombre("Carlos Steak House");
        r2.setSlug("carlos-steak-house");
        r2.setDireccion("Av. El Dorado # 45-67, Bogotá");
        r2.setCiudad("Bogotá");
        r2.setTelefono("6019876543");
        r2.setDescripcion("Carnes maduradas, parrilla argentina y los mejores cortes internacionales.");
        r2.setTipoComida("Argentina");
        r2.setCapacidadTotal(120);
        r2.setHorarioApertura("18:00");
        r2.setHorarioCierre("02:00");
        r2.setResenasAvgRating(4.6);
        r2.setResenasCount(98);
        r2.setAbiertoAhora(false);
        r2.setImagen(img + "placeholder_rest_2");
        r2.setLogo(img + "placeholder_rest_2");
        r2.setBanner(img + "placeholder_rest_2");
        r2.setEstado("activo");

        Restaurante r3 = new Restaurante();
        r3.setId(3);
        r3.setNombre("Sushi Carlos");
        r3.setSlug("sushi-carlos");
        r3.setDireccion("Calle 85 # 15-20, Bogotá");
        r3.setCiudad("Bogotá");
        r3.setTelefono("6014567890");
        r3.setDescripcion("Fusión japonesa con toque latino. Ingredientes frescos y presentación artística.");
        r3.setTipoComida("Japonesa");
        r3.setCapacidadTotal(60);
        r3.setHorarioApertura("12:00");
        r3.setHorarioCierre("22:00");
        r3.setResenasAvgRating(4.9);
        r3.setResenasCount(203);
        r3.setAbiertoAhora(true);
        r3.setImagen(img + "placeholder_rest_3");
        r3.setLogo(img + "placeholder_rest_3");
        r3.setBanner(img + "placeholder_rest_3");
        r3.setEstado("activo");

        Restaurante r4 = new Restaurante();
        r4.setId(4);
        r4.setNombre("El Fogón de Carlos");
        r4.setSlug("el-fogon-de-carlos");
        r4.setDireccion("Cra 7 # 32-10, Bogotá");
        r4.setCiudad("Bogotá");
        r4.setTelefono("6012345678");
        r4.setDescripcion("Comida típica colombiana con recetas de la abuela. Bandeja paisa, sancocho, ajiaco y más.");
        r4.setTipoComida("Colombiana");
        r4.setCapacidadTotal(100);
        r4.setHorarioApertura("07:00");
        r4.setHorarioCierre("22:00");
        r4.setResenasAvgRating(4.5);
        r4.setResenasCount(312);
        r4.setAbiertoAhora(true);
        r4.setImagen(img + "placeholder_rest_4");
        r4.setLogo(img + "placeholder_rest_4");
        r4.setBanner(img + "placeholder_rest_4");
        r4.setEstado("activo");

        Restaurante r5 = new Restaurante();
        r5.setId(5);
        r5.setNombre("Carlos Pizza & Pasta");
        r5.setSlug("carlos-pizza-pasta");
        r5.setDireccion("Av. Chile # 23-45, Medellín");
        r5.setCiudad("Medellín");
        r5.setTelefono("6045678901");
        r5.setDescripcion("Pizzas artesanales horneadas en horno de leña y pastas frescas hechas a mano.");
        r5.setTipoComida("Italiana");
        r5.setCapacidadTotal(75);
        r5.setHorarioApertura("12:00");
        r5.setHorarioCierre("23:00");
        r5.setResenasAvgRating(4.7);
        r5.setResenasCount(89);
        r5.setAbiertoAhora(false);
        r5.setImagen(img + "placeholder_rest_5");
        r5.setLogo(img + "placeholder_rest_5");
        r5.setBanner(img + "placeholder_rest_5");
        r5.setEstado("activo");

        Restaurante r6 = new Restaurante();
        r6.setId(6);
        r6.setNombre("Bar Carlos Tropical");
        r6.setSlug("bar-carlos-tropical");
        r6.setDireccion("Calle 26 # 50-12, Cali");
        r6.setCiudad("Cali");
        r6.setTelefono("6023456789");
        r6.setDescripcion("Cocktails tropicales, música en vivo y tapas caribeñas. El mejor ambiente de la ciudad.");
        r6.setTipoComida("Caribeña");
        r6.setCapacidadTotal(150);
        r6.setHorarioApertura("17:00");
        r6.setHorarioCierre("03:00");
        r6.setResenasAvgRating(4.3);
        r6.setResenasCount(67);
        r6.setAbiertoAhora(true);
        r6.setImagen(img + "placeholder_rest_6");
        r6.setLogo(img + "placeholder_rest_6");
        r6.setBanner(img + "placeholder_rest_6");
        r6.setEstado("activo");

        return Arrays.asList(r1, r2, r3, r4, r5, r6);
    }

    public static List<Mesa> createMesas(int restauranteId) {
        List<Mesa> mesas = new ArrayList<>();
        String[] estados = {"disponible", "ocupada", "reservada", "disponible", "ocupada", "disponible", "reservada", "disponible", "disponible", "ocupada"};

        for (int i = 1; i <= 10; i++) {
            Mesa mesa = new Mesa();
            mesa.setId((restauranteId * 100) + i);
            mesa.setRestauranteId(restauranteId);
            mesa.setNumero(i);
            mesa.setCapacidad((i % 3 == 0) ? 6 : (i % 2 == 0) ? 4 : 2);
            mesa.setEstado(estados[i - 1]);
            mesas.add(mesa);
        }
        return mesas;
    }

    public static List<Reserva> createReservas() {
        List<Reserva> reservas = new ArrayList<>();
        List<Restaurante> restaurantes = createRestaurantes();
        List<Mesa> mesasR1 = createMesas(1);
        List<Mesa> mesasR2 = createMesas(2);

        Reserva r1 = new Reserva();
        r1.setId(1);
        r1.setClienteId(2);
        r1.setRestauranteId(1);
        r1.setMesaId(101);
        r1.setFecha("2026-06-02");
        r1.setHora("20:00");
        r1.setCantidadPersonas(4);
        r1.setEstado("confirmada");
        r1.setNotas("Aniversario de bodas");
        r1.setRestaurante(restaurantes.get(0));
        r1.setMesa(mesasR1.get(0));
        r1.setCliente(createCliente());
        reservas.add(r1);

        Reserva r2 = new Reserva();
        r2.setId(2);
        r2.setClienteId(2);
        r2.setRestauranteId(1);
        r2.setMesaId(102);
        r2.setFecha("2026-06-02");
        r2.setHora("21:30");
        r2.setCantidadPersonas(2);
        r2.setEstado("pendiente");
        r2.setNotas("");
        r2.setRestaurante(restaurantes.get(0));
        r2.setMesa(mesasR1.get(1));
        r2.setCliente(createCliente());
        reservas.add(r2);

        Reserva r3 = new Reserva();
        r3.setId(3);
        r3.setClienteId(3);
        r3.setRestauranteId(1);
        r3.setMesaId(103);
        r3.setFecha("2026-06-01");
        r3.setHora("19:00");
        r3.setCantidadPersonas(6);
        r3.setEstado("completada");
        r3.setNotas("Cena de negocios");
        r3.setRestaurante(restaurantes.get(0));
        r3.setMesa(mesasR1.get(2));
        r3.setCliente(createOtroCliente());
        reservas.add(r3);

        Reserva r4 = new Reserva();
        r4.setId(4);
        r4.setClienteId(2);
        r4.setRestauranteId(1);
        r4.setMesaId(104);
        r4.setFecha("2026-05-30");
        r4.setHora("20:00");
        r4.setCantidadPersonas(4);
        r4.setEstado("cancelada");
        r4.setNotas("Cancelado por imprevisto");
        r4.setRestaurante(restaurantes.get(0));
        r4.setMesa(mesasR1.get(3));
        r4.setCliente(createCliente());
        reservas.add(r4);

        Reserva r5 = new Reserva();
        r5.setId(5);
        r5.setClienteId(3);
        r5.setRestauranteId(2);
        r5.setMesaId(205);
        r5.setFecha("2026-06-03");
        r5.setHora("21:00");
        r5.setCantidadPersonas(3);
        r5.setEstado("confirmada");
        r5.setNotas("");
        r5.setRestaurante(restaurantes.get(1));
        r5.setMesa(mesasR2.get(4));
        r5.setCliente(createOtroCliente());
        reservas.add(r5);

        return reservas;
    }

    private static Usuario createCliente() {
        Usuario user = new Usuario();
        user.setId(2);
        user.setName("María García");
        user.setEmail("maria@email.com");
        user.setRole("cliente");
        user.setAvatar(null);
        user.setEstado("activo");
        user.setTelefono("3109876543");
        return user;
    }

    private static Usuario createOtroCliente() {
        Usuario user = new Usuario();
        user.setId(3);
        user.setName("Pedro López");
        user.setEmail("pedro@email.com");
        user.setRole("cliente");
        user.setAvatar(null);
        user.setEstado("activo");
        user.setTelefono("3205678901");
        return user;
    }

    public static DashboardData createDashboardData() {
        DashboardData data = new DashboardData();
        data.setReservasHoy(8);
        data.setConfirmadas(5);
        data.setCanceladas(1);
        data.setOcupacionHoy(72.5);
        data.setMesasTotales(10);
        data.setMesasOcupadasHoy(7);
        data.setMesasLibresHoy(3);
        data.setPersonasPromedio(3.5);

        List<DashboardData.HoraPico> horasPico = new ArrayList<>();
        for (int h = 12; h <= 23; h++) {
            DashboardData.HoraPico hp = new DashboardData.HoraPico();
            hp.setHora(h);
            hp.setTotal(h >= 12 && h <= 15 ? 8 : h >= 19 && h <= 22 ? 12 : 3);
            horasPico.add(hp);
        }
        data.setHorasPico(horasPico);

        List<DashboardData.ReservaSemana> semanas = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            DashboardData.ReservaSemana rs = new DashboardData.ReservaSemana();
            rs.setSemana(i);
            rs.setTotal(20 + (i * 3) + (i % 2 == 0 ? 0 : 5));
            semanas.add(rs);
        }
        data.setReservasPorSemana(semanas);

        return data;
    }

    public static CalendarioResponse createCalendarioEventos() {
        CalendarioResponse response = new CalendarioResponse();
        response.setRestauranteId("1");
        response.setFechaInicio("2026-06-01");
        response.setFechaFin("2026-06-30");

        List<CalendarioResponse.Evento> eventos = new ArrayList<>();
        String[][] eventosData = {
            {"1", "María García - 4 pers", "2026-06-02T20:00:00", "2026-06-02T22:00:00", "#4CAF50"},
            {"2", "Pedro López - 6 pers", "2026-06-02T21:30:00", "2026-06-02T23:00:00", "#FF9800"},
            {"3", "Ana Martínez - 2 pers", "2026-06-03T19:00:00", "2026-06-03T20:30:00", "#4CAF50"},
            {"4", "Grupo Corporativo - 10 pers", "2026-06-05T20:00:00", "2026-06-05T23:00:00", "#2196F3"},
            {"5", "Cumpleaños - 8 pers", "2026-06-08T19:00:00", "2026-06-08T22:00:00", "#9C27B0"},
            {"6", "Reserva bloqueada", "2026-06-10T12:00:00", "2026-06-10T23:00:00", "#F44336"},
        };

        for (String[] ev : eventosData) {
            CalendarioResponse.Evento evento = new CalendarioResponse.Evento();
            evento.setId(Integer.parseInt(ev[0]));
            evento.setTitle(ev[1]);
            evento.setStart(ev[2]);
            evento.setEnd(ev[3]);
            evento.setBackgroundColor(ev[4]);
            evento.setBorderColor(ev[4]);
            eventos.add(evento);
        }

        response.setEventos(eventos);
        return response;
    }

    public static List<Map<String, Object>> createHorarios() {
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        List<Map<String, Object>> horarios = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            Map<String, Object> day = new HashMap<>();
            day.put("dia", dias[i]);
            day.put("abierto", i < 6);
            day.put("hora_apertura", i < 5 ? "12:00" : "14:00");
            day.put("hora_cierre", i < 5 ? "23:00" : "01:00");
            horarios.add(day);
        }
        return horarios;
    }

    public static List<Map<String, Object>> createImagenes() {
        List<Map<String, Object>> imagenes = new ArrayList<>();
        String[] urls = {
            "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4",
            "https://images.unsplash.com/photo-1555396273-367ea4eb4db5",
            "https://images.unsplash.com/photo-1559339352-11d035aa65de",
            "https://images.unsplash.com/photo-1579871494447-9811cf80d66c",
            "https://images.unsplash.com/photo-1550966871-3ed3cdb51f3a"
        };

        for (int i = 0; i < urls.length; i++) {
            Map<String, Object> img = new HashMap<>();
            img.put("id", i + 1);
            img.put("url", urls[i]);
            img.put("descripcion", "Foto " + (i + 1) + " del restaurante");
            imagenes.add(img);
        }
        return imagenes;
    }

    public static List<Map<String, Object>> createFavoritos() {
        List<Map<String, Object>> favs = new ArrayList<>();
        for (Restaurante r : createRestaurantes()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", r.getId() * 10);
            map.put("restaurante_id", r.getId());
            Map<String, Object> restMap = new HashMap<>();
            restMap.put("id", r.getId());
            restMap.put("nombre", r.getNombre());
            restMap.put("tipo_comida", r.getTipoComida());
            restMap.put("ciudad", r.getCiudad());
            restMap.put("imagen", r.getImagen());
            restMap.put("resenas_avg_rating", r.getResenasAvgRating());
            map.put("restaurante", restMap);
            favs.add(map);
        }
        return favs;
    }

    public static Map<String, Object> createReservaDetail(int id) {
        for (Reserva r : createReservas()) {
            if (r.getId() == id) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", r.getId());
                map.put("cliente_id", r.getClienteId());
                map.put("restaurante_id", r.getRestauranteId());
                map.put("mesa_id", r.getMesaId());
                map.put("fecha", r.getFecha());
                map.put("hora", r.getHora());
                map.put("cantidad_personas", r.getCantidadPersonas());
                map.put("estado", r.getEstado());
                map.put("notas", r.getNotas() != null ? r.getNotas() : "");

                Restaurante rest = r.getRestaurante();
                Map<String, Object> restMap = new HashMap<>();
                restMap.put("id", rest.getId());
                restMap.put("nombre", rest.getNombre());
                restMap.put("direccion", rest.getDireccion());
                map.put("restaurante", restMap);

                Mesa mesa = r.getMesa();
                Map<String, Object> mesaMap = new HashMap<>();
                mesaMap.put("id", mesa.getId());
                mesaMap.put("numero", mesa.getNumero());
                mesaMap.put("capacidad", mesa.getCapacidad());
                map.put("mesa", mesaMap);

                Usuario cliente = r.getCliente();
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("id", cliente.getId());
                userMap.put("name", cliente.getName());
                userMap.put("email", cliente.getEmail());
                userMap.put("telefono", cliente.getTelefono());
                map.put("cliente", userMap);

                return map;
            }
        }
        Map<String, Object> empty = new HashMap<>();
        empty.put("id", id);
        empty.put("estado", "desconocido");
        return empty;
    }
}
