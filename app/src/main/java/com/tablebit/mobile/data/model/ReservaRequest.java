package com.tablebit.mobile.data.model;

import com.google.gson.annotations.SerializedName;

public class ReservaRequest {

    @SerializedName("restaurante_id")
    private int restauranteId;

    private String fecha;
    private String hora;

    @SerializedName("cantidad_personas")
    private int cantidadPersonas;

    private String notas;

    public ReservaRequest(int restauranteId, String fecha, String hora, int cantidadPersonas) {
        this.restauranteId = restauranteId;
        this.fecha = fecha;
        this.hora = hora;
        this.cantidadPersonas = cantidadPersonas;
    }

    public int getRestauranteId() { return restauranteId; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public int getCantidadPersonas() { return cantidadPersonas; }
}
