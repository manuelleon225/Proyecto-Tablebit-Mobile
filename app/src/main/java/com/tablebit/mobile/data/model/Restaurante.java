package com.tablebit.mobile.data.model;

import com.google.gson.annotations.SerializedName;

public class Restaurante {
    private int id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String descripcion;
    private String imagen;

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getDescripcion() { return descripcion; }
    public String getImagen() { return imagen; }
}
