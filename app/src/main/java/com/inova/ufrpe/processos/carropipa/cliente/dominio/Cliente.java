package com.inova.ufrpe.processos.carropipa.cliente.dominio;

import com.inova.ufrpe.processos.carropipa.pessoa.dominio.Pessoa;

public class Cliente {
    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    private Pessoa pessoa;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
}