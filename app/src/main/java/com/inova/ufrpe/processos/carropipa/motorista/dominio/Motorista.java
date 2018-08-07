package com.inova.ufrpe.processos.carropipa.motorista.dominio;

import android.os.Parcel;
import android.os.Parcelable;

import com.inova.ufrpe.processos.carropipa.pessoa.dominio.Pessoa;

import java.io.Serializable;

public class Motorista implements Parcelable, Serializable {

    private Long id;
    private String cnh;
    private String rank;

    private Pessoa pessoa;

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    private Veiculo veiculo;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeString(this.cnh );
        dest.writeString( this.rank );

    }

    private void readFromParcel(Parcel parcel) {
        //le os dados na ordem que foram escritos
        this.id = parcel.readLong();
        this.cnh = parcel.readString();
        this.rank = parcel.readString();

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

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
}
