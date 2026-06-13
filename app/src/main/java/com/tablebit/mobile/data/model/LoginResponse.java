package com.tablebit.mobile.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private Usuario user;
    private String token;

    public Usuario getUser() { return user; }
    public void setUser(Usuario user) { this.user = user; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
