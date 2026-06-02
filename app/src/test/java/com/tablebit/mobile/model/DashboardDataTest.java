package com.tablebit.mobile.model;

import com.tablebit.mobile.data.model.DashboardData;

import org.junit.Test;

import static org.junit.Assert.*;

public class DashboardDataTest {

    @Test
    public void getEstadoDescripcion_sinReservas_tranquilo() {
        DashboardData data = new DashboardData();
        String desc = data.getEstadoDescripcion();
        assertTrue(desc.contains("Tranquilo") || desc.contains("Sin reservas"));
    }

    @Test
    public void getCapacidadFisica_formatoCorrecto() {
        DashboardData data = new DashboardData();
        String cap = data.getCapacidadFisica();
        assertTrue(cap.contains("/"));
    }

    @Test
    public void getOcupacionDetalle_contienePorcentaje() {
        DashboardData data = new DashboardData();
        String detalle = data.getOcupacionDetalle();
        assertTrue(detalle.contains("%"));
    }

    @Test
    public void reservasHoy_defaultZero() {
        DashboardData data = new DashboardData();
        assertEquals(0, data.getReservasHoy());
    }

    @Test
    public void mesasTotales_defaultZero() {
        DashboardData data = new DashboardData();
        assertEquals(0, data.getMesasTotales());
    }

    @Test
    public void ocupacionHoy_defaultZero() {
        DashboardData data = new DashboardData();
        assertEquals(0.0, data.getOcupacionHoy(), 0.01);
    }

    @Test
    public void confirmadas_defaultZero() {
        DashboardData data = new DashboardData();
        assertEquals(0, data.getConfirmadas());
    }

    @Test
    public void canceladas_defaultZero() {
        DashboardData data = new DashboardData();
        assertEquals(0, data.getCanceladas());
    }
}
