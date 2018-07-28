package com.inova.ufrpe.processos.carropipa.cliente.dominio;

import android.os.Parcel;
import android.os.Parcelable;

import com.inova.ufrpe.processos.carropipa.pessoa.dominio.Pessoa;

/**
 * Classe de base cliente implementa parcelable
 * parcelable é recomendado pelo android developer
 * describeContents e writetoParcel são exigidos
 * Serializable no android gera problemas de perfomance - usemos apenas pacerlable
 */

public class Cliente implements Parcelable {

    private int id;
    private String email;
    private String nome;
    private String sobreNome;
    private String senha;
    private String rank;
    private String telefone;
    private String cpf;
    private String logradouro;
    private String complemento;
    private String cidade;
    private String bairro;
    private String cep;
    private String uf;


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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public static final Parcelable.Creator<Cliente> CREATOR = new Creator<Cliente>() {
        @Override
        public Cliente createFromParcel(Parcel in) {
            Cliente cliente = new Cliente();
            cliente.readFromParcel(in);
            return cliente;
        }

        @Override
        public Cliente[] newArray(int size){
            return new Cliente[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(email);
        dest.writeString(nome);
        dest.writeString(senha);
        dest.writeString(rank);
        dest.writeString(telefone);
        dest.writeString(cep);
        dest.writeString(logradouro);
        dest.writeString(complemento);
        dest.writeString(cidade);
        dest.writeString(bairro);
        dest.writeString(cep);
        dest.writeString(uf);
    }

    private void readFromParcel(Parcel parcel){
        this.id = parcel.readInt();
        this.email = parcel.readString();
        this.nome = parcel.readString();
        this.senha = parcel.readString();
        this.rank = parcel.readString();
        this.telefone = parcel.readString();
        this.cep = parcel.readString();
        this.logradouro = parcel.readString();
        this.complemento = parcel.readString();
        this.cidade = parcel.readString();
        this.bairro = parcel.readString();
        this.cep = parcel.readString();
        this.uf = parcel.readString();
    }
}