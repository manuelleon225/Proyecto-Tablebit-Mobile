package com.tablebit.mobile.data.model;

import com.google.gson.annotations.SerializedName;

public class Reserva {
    private int id;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("mesa_id")
    private int mesaId;

    @SerializedName("restaurante_id")
    private int restauranteId;

    private String fecha;
    private String hora;
    private int personas;
    private String estado;

    private Restaurante restaurante;
    private Mesa mesa;

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getMesaId() { return mesaId; }
    public int getRestauranteId() { return restauranteId; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public int getPersonas() { return personas; }
    public String getEstado() { return estado; }
    public Restaurante getRestaurante() { return restaurante; }
    public Mesa getMesa() { return mesa; }
}
