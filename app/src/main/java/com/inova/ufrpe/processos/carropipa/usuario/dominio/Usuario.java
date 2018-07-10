package com.inova.ufrpe.processos.carropipa.usuario.dominio;

import java.io.Serializable;

public class Usuario implements Serializable {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    private int id;
    private String email;
    private String senha;

}
