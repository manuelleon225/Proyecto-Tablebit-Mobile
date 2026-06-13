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
    public void setId(int id) { this.id = id; }
    public int getRestauranteId() { return restauranteId; }
    public void setRestauranteId(int restauranteId) { this.restauranteId = restauranteId; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
