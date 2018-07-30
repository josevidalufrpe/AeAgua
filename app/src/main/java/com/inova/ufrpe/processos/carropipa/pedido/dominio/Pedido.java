package com.inova.ufrpe.processos.carropipa.pedido.dominio;

import android.os.Parcel;
import android.os.Parcelable;

import com.inova.ufrpe.processos.carropipa.cliente.dominio.Cliente;
import com.inova.ufrpe.processos.carropipa.motorista.dominio.Motorista;

import java.io.Serializable;

public class Pedido implements Parcelable {
    private int id;
    private Cliente motorista;
    private Cliente cliente;
    private String quantidade;
    private Double valor;
    private Double latitude;
    private Double longitude;


    public Pedido(Parcel in) {

        id = in.readInt();
        motorista = in.readParcelable(Motorista.class.getClassLoader());
        cliente = in.readParcelable(Cliente.class.getClassLoader());
        quantidade = in.readString();
        if (in.readByte() == 0) {
            valor = null;
        } else {
            valor = in.readDouble();
        }
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
    }

    public static final Creator<Pedido> CREATOR = new Creator<Pedido>() {
        @Override
        public Pedido createFromParcel(Parcel in) {
            return new Pedido(in);
        }

        @Override
        public Pedido[] newArray(int size) {
            return new Pedido[size];
        }
    };

    public Pedido() {

    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cliente getMotorista() {
        return motorista;
    }

    public void setMotorista(Cliente motorista) {
        this.motorista = motorista;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(quantidade);
        dest.writeDouble(valor);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeParcelable(cliente,flags);
        dest.writeParcelable(motorista, flags);
    }
}
