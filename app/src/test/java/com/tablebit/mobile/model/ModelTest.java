package com.tablebit.mobile.model;

import com.tablebit.mobile.data.model.LoginRequest;
import com.tablebit.mobile.data.model.RegisterRequest;
import com.tablebit.mobile.data.model.MesaEstadoRequest;
import com.tablebit.mobile.data.model.ReservaRequest;
import com.tablebit.mobile.data.model.ApiResponse;
import com.tablebit.mobile.data.model.LoginResponse;
import com.tablebit.mobile.data.model.Usuario;
import com.tablebit.mobile.data.model.Restaurante;
import com.tablebit.mobile.data.model.Mesa;
import com.tablebit.mobile.data.model.Reserva;
import com.tablebit.mobile.data.model.DashboardData;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ModelTest {

    @Test
    public void loginRequest_createsWithEmailAndPassword() {
        LoginRequest request = new LoginRequest("test@test.com", "password123");
        assertEquals("test@test.com", request.getEmail());
        assertEquals("password123", request.getPassword());
    }

    @Test
    public void registerRequest_createsWithNameEmailPassword() {
        RegisterRequest request = new RegisterRequest("Juan", "juan@test.com", "pass123");
        assertEquals("Juan", request.name);
        assertEquals("juan@test.com", request.email);
        assertEquals("pass123", request.password);
        assertEquals("pass123", request.password_confirmation);
    }

    @Test
    public void mesaEstadoRequest_createsWithEstado() {
        MesaEstadoRequest request = new MesaEstadoRequest("ocupada");
        assertEquals("ocupada", request.estado);
    }

    @Test
    public void reservaRequest_createsWithAllFields() {
        ReservaRequest request = new ReservaRequest(1, "2026-06-15", "20:00", 4);
        assertEquals(1, request.restauranteId);
        assertEquals("2026-06-15", request.fecha);
        assertEquals("20:00", request.hora);
        assertEquals(4, request.cantidadPersonas);
    }

    @Test
    public void apiResponse_gettersWork() {
        ApiResponse<String> response = new ApiResponse<>();
        assertFalse(response.isSuccess());
        assertNull(response.getMessage());
        assertNull(response.getData());
    }

    @Test
    public void loginResponse_hasUserAndToken() {
        Usuario user = new Usuario();
        LoginResponse response = new LoginResponse();
        assertNull(response.getUser());
        assertNull(response.getToken());
    }

    @Test
    public void usuario_gettersReturnDefaults() {
        Usuario user = new Usuario();
        assertEquals(0, user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getAvatar());
        assertNull(user.getRole());
        assertNull(user.getEstado());
    }

    @Test
    public void restaurante_gettersWork() {
        Restaurante r = new Restaurante();
        r.setId(1);
        r.setNombre("Test Restaurante");
        r.setCiudad("Bogota");
        r.setTipoComida("Italiana");
        r.setImagen("imagen.jpg");
        r.setResenasAvgRating(4.5);
        r.setAbiertoAhora(true);

        assertEquals(1, r.getId());
        assertEquals("Test Restaurante", r.getNombre());
        assertEquals("Bogota", r.getCiudad());
        assertEquals("Italiana", r.getTipoComida());
        assertEquals("imagen.jpg", r.getImagen());
        assertEquals(4.5, r.getResenasAvgRating(), 0.01);
        assertTrue(r.getAbiertoAhora());
    }

    @Test
    public void mesa_gettersWork() {
        Mesa mesa = new Mesa();
        assertEquals(0, mesa.getId());
        assertEquals(0, mesa.getRestauranteId());
        assertEquals(0, mesa.getNumero());
        assertEquals(0, mesa.getCapacidad());
        assertNull(mesa.getEstado());
    }

    @Test
    public void reserva_gettersWork() {
        Reserva reserva = new Reserva();
        assertEquals(0, reserva.getId());
        assertEquals(0, reserva.getClienteId());
        assertEquals(0, reserva.getRestauranteId());
        assertEquals(0, reserva.getMesaId());
        assertEquals(0, reserva.getCantidadPersonas());
        assertNull(reserva.getFecha());
        assertNull(reserva.getHora());
        assertNull(reserva.getEstado());
        assertNull(reserva.getNotas());
        assertNull(reserva.getRestaurante());
        assertNull(reserva.getMesa());
        assertNull(reserva.getCliente());
    }

    @Test
    public void dashboardData_estadoDescripcion_tranquilo() {
        DashboardData data = new DashboardData();
        assertEquals("Tranquilo — Sin reservas hoy", data.getEstadoDescripcion());
    }

    @Test
    public void dashboardData_estadoDescripcion_normal() {
        DashboardData data = new DashboardData();
        int reservasHoy = 2;
        assertEquals("Normal — 2 reservas hoy", data.getEstadoDescripcion());
    }
}
