package com.tablebit.mobile.data.model;

import com.google.gson.annotations.SerializedName;

public class Restaurante {
    private int id;
    private String nombre;
    private String slug;
    private String direccion;
    private String ciudad;
    private String telefono;
    private String descripcion;
    private String imagen;

    @SerializedName("tipo_comida")
    private String tipoComida;

    @SerializedName("capacidad_total")
    private int capacidadTotal;

    @SerializedName("horario_apertura")
    private String horarioApertura;

    @SerializedName("horario_cierre")
    private String horarioCierre;

    @SerializedName("resenas_avg_rating")
    private Double resenasAvgRating;

    @SerializedName("resenas_count")
    private int resenasCount;

    @SerializedName("abierto_ahora")
    private Boolean abiertoAhora;

    private String logo;
    private String banner;
    private String estado;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public String getTipoComida() { return tipoComida; }
    public void setTipoComida(String tipoComida) { this.tipoComida = tipoComida; }
    public int getCapacidadTotal() { return capacidadTotal; }
    public void setCapacidadTotal(int capacidadTotal) { this.capacidadTotal = capacidadTotal; }
    public String getHorarioApertura() { return horarioApertura; }
    public void setHorarioApertura(String horarioApertura) { this.horarioApertura = horarioApertura; }
    public String getHorarioCierre() { return horarioCierre; }
    public void setHorarioCierre(String horarioCierre) { this.horarioCierre = horarioCierre; }
    public Double getResenasAvgRating() { return resenasAvgRating; }
    public void setResenasAvgRating(Double rating) { this.resenasAvgRating = rating; }
    public int getResenasCount() { return resenasCount; }
    public void setResenasCount(int resenasCount) { this.resenasCount = resenasCount; }
    public Boolean getAbiertoAhora() { return abiertoAhora; }
    public void setAbiertoAhora(Boolean abierto) { this.abiertoAhora = abierto; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    public String getBanner() { return banner; }
    public void setBanner(String banner) { this.banner = banner; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
