package com.tablebit.mobile.data.model;

import com.google.gson.annotations.SerializedName;

public class ReservaRequest {

    @SerializedName("restaurante_id")
    private int restauranteId;

    @SerializedName("mesa_id")
    private int mesaId;

    private String fecha;
    private String hora;
    private int personas;

    public ReservaRequest(int restauranteId, int mesaId, String fecha, String hora, int personas) {
        this.restauranteId = restauranteId;
        this.mesaId = mesaId;
        this.fecha = fecha;
        this.hora = hora;
        this.personas = personas;
    }
}
