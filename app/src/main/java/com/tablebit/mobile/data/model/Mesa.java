package com.tablebit.mobile.data.model;

import com.google.gson.annotations.SerializedName;

public class Mesa {
    private int id;

    @SerializedName("restaurante_id")
    private int restauranteId;

    private int numero;
    private int capacidad;
    private String estado;

    public int getId() { return id; }
    public int getRestauranteId() { return restauranteId; }
    public int getNumero() { return numero; }
    public int getCapacidad() { return capacidad; }
    public String getEstado() { return estado; }
}
