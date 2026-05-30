package com.tablebit.mobile.data.model;

import com.google.gson.annotations.SerializedName;

public class Mesa {
    private int id;

    @SerializedName("restaurante_id")
    private int restauranteId;

    @SerializedName("numero_mesa")
    private int numeroMesa;

    private int capacidad;
    private String ubicacion;
    private String estado;

    public int getId() { return id; }
    public int getRestauranteId() { return restauranteId; }
    public int getNumeroMesa() { return numeroMesa; }
    public int getCapacidad() { return capacidad; }
    public String getUbicacion() { return ubicacion; }
    public String getEstado() { return estado; }
}
