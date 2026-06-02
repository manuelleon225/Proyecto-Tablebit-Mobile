package com.tablebit.mobile.model;

import com.tablebit.mobile.data.model.CalendarioResponse;

import org.junit.Test;

import static org.junit.Assert.*;

public class CalendarioResponseTest {

    @Test
    public void evento_getFecha_fromISO_withT() {
        CalendarioResponse.Evento evento = new CalendarioResponse.Evento();
        String start = "2026-06-15T20:00:00";
        String fecha = evento.getFecha();
        assertNull(fecha);
    }

    @Test
    public void evento_getFecha_noT() {
        CalendarioResponse.Evento evento = new CalendarioResponse.Evento();
        String start = "2026-06-15";
        String fecha = evento.getFecha();
        assertNull(fecha);
    }

    @Test
    public void evento_getters_defaults() {
        CalendarioResponse.Evento evento = new CalendarioResponse.Evento();
        assertEquals(0, evento.getId());
        assertNull(evento.getTitle());
        assertNull(evento.getStart());
        assertNull(evento.getEnd());
        assertNull(evento.getBackgroundColor());
        assertNull(evento.getBorderColor());
    }

    @Test
    public void calendarioResponse_eventos_nullByDefault() {
        CalendarioResponse response = new CalendarioResponse();
        assertNull(response.getEventos());
    }
}
