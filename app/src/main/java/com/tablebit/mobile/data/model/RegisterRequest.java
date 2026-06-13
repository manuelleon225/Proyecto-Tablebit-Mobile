package com.tablebit.mobile.data.model;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String password_confirmation;

    public RegisterRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.password_confirmation = password;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
}
