package com.inova.ufrpe.processos.carropipa.motorista.dominio;

import android.os.Parcel;
import android.os.Parcelable;

public class Motorista implements Parcelable {

    private Long id;
    private String cpf;
    private String logradouro;
    private String complemento;
    private String cidade;
    private String bairro;
    private String cep;
    private String uf;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeString(this.cpf);
        dest.writeString(this.logradouro);
        dest.writeString(this.complemento);
        dest.writeString(this.bairro);
        dest.writeString(this.cep);
        dest.writeString(this.cidade);
        dest.writeString(this.uf);
    }

    private void readFromParcel(Parcel parcel) {
        //le os dados na ordem que foram escritos
        this.id = parcel.readLong();
        this.cpf = parcel.readString();
        this.logradouro = parcel.readString();
        this.complemento = parcel.readString();
        this.bairro = parcel.readString();
        this.cep = parcel.readString();
        this.cidade = parcel.readString();
        this.uf = parcel.readString();
    }

    public static final Creator<Motorista> CREATOR = new Creator<Motorista>() {
        @Override
        public Motorista createFromParcel(Parcel p) {
            Motorista eq = new Motorista();
            eq.readFromParcel(p);
            return eq;
        }
        @Override
        public Motorista[] newArray(int size){
            return new Motorista[size];
        }
    };
}
