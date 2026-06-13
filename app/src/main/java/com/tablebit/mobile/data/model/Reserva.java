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
    public void setId(int id) { this.id = id; }
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public int getRestauranteId() { return restauranteId; }
    public void setRestauranteId(int restauranteId) { this.restauranteId = restauranteId; }
    public int getMesaId() { return mesaId; }
    public void setMesaId(int mesaId) { this.mesaId = mesaId; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    public int getCantidadPersonas() { return cantidadPersonas; }
    public void setCantidadPersonas(int cantidadPersonas) { this.cantidadPersonas = cantidadPersonas; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public Restaurante getRestaurante() { return restaurante; }
    public void setRestaurante(Restaurante restaurante) { this.restaurante = restaurante; }
    public Mesa getMesa() { return mesa; }
    public void setMesa(Mesa mesa) { this.mesa = mesa; }
    public Usuario getCliente() { return cliente; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }
}
