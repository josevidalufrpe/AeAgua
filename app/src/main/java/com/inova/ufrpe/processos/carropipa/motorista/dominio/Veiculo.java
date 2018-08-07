package com.inova.ufrpe.processos.carropipa.motorista.dominio;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Veiculo implements Serializable,Parcelable {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(String capacidade) {
        this.capacidade = capacidade;
    }

    private int id;
    private String placa;
    private String cor;
    private String capacidade;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.id);
        dest.writeString(this.placa );
        dest.writeString( this.cor );
        dest.writeString( this.capacidade );

    }

    private void readFromParcel(Parcel parcel) {
        //le os dados na ordem que foram escritos
        this.id = parcel.readInt();
        this.placa = parcel.readString();
        this.cor = parcel.readString();
        this.capacidade = parcel.readString();

    }

    public static final Creator<Veiculo> CREATOR = new Creator<Veiculo>() {
        @Override
        public Veiculo createFromParcel(Parcel p) {
            Veiculo eq = new Veiculo();
            eq.readFromParcel(p);
            return eq;
        }
        @Override
        public Veiculo[] newArray(int size){
            return new Veiculo[size];
        }
    };
}
