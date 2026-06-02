package com.tablebit.mobile.data.model;

import com.google.gson.annotations.SerializedName;

public class Reserva {
    private int id;

    @SerializedName("cliente_id")
    private int clienteId;

    @SerializedName("restaurante_id")
    private int restauranteId;

    @SerializedName("mesa_id")
    private int mesaId;

    private String fecha;
    private String hora;

    @SerializedName("cantidad_personas")
    private int cantidadPersonas;

    private String estado;
    private String notas;

    private Restaurante restaurante;
    private Mesa mesa;
    private Usuario cliente;

    public int getId() { return id; }
    public int getClienteId() { return clienteId; }
    public int getRestauranteId() { return restauranteId; }
    public int getMesaId() { return mesaId; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public int getCantidadPersonas() { return cantidadPersonas; }
    public String getEstado() { return estado; }
    public String getNotas() { return notas; }
    public Restaurante getRestaurante() { return restaurante; }
    public Mesa getMesa() { return mesa; }
    public Usuario getCliente() { return cliente; }
}
