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

    public static class Evento {
        private int id;
        private String title;
        private String start;
        private String end;
        private String backgroundColor;
        private String borderColor;

        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getStart() { return start; }
        public String getEnd() { return end; }
        public String getBackgroundColor() { return backgroundColor; }
        public String getBorderColor() { return borderColor; }

        public String getFecha() {
            if (start != null && start.contains("T")) {
                return start.split("T")[0];
            }
            return start;
        }
    }
}
