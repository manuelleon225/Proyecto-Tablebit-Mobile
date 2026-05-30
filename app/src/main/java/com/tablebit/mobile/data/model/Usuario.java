package com.tablebit.mobile.data.model;

import com.google.gson.annotations.SerializedName;

public class Usuario {
    private int id;
    private String name;
    private String email;
    private String telefono;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
}
