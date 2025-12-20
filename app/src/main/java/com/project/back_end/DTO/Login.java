package com.project.back_end.DTO;

public class Login {

    private String email;
    private String password;

    // Constructor vacío (Java lo crea igual, pero es buena práctica)
    public Login() {
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

