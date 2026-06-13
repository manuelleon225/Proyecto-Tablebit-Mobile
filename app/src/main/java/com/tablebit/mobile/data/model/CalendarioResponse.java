package com.tablebit.mobile.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CalendarioResponse {

    @SerializedName("restaurante_id")
    private String restauranteId;

    @SerializedName("fecha_inicio")
    private String fechaInicio;

    @SerializedName("fecha_fin")
    private String fechaFin;

    private List<Evento> eventos;

    public List<Evento> getEventos() { return eventos; }
    public void setEventos(List<Evento> eventos) { this.eventos = eventos; }
    public String getRestauranteId() { return restauranteId; }
    public void setRestauranteId(String restauranteId) { this.restauranteId = restauranteId; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public static class Evento {
        private int id;
        private String title;
        private String start;
        private String end;
        private String backgroundColor;
        private String borderColor;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getStart() { return start; }
        public void setStart(String start) { this.start = start; }
        public String getEnd() { return end; }
        public void setEnd(String end) { this.end = end; }
        public String getBackgroundColor() { return backgroundColor; }
        public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }
        public String getBorderColor() { return borderColor; }
        public void setBorderColor(String borderColor) { this.borderColor = borderColor; }

        public String getFecha() {
            if (start != null && start.contains("T")) {
                return start.split("T")[0];
            }
            return start;
        }
    }
}
