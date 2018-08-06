package com.inova.ufrpe.processos.carropipa.usuario.dominio;

import android.os.Parcel;
import android.os.Parcelable;

import com.inova.ufrpe.processos.carropipa.pessoa.dominio.Pessoa;

import java.io.Serializable;

public class Usuario implements Parcelable, Serializable {
    private int id;
    private String email;

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

    private String senha;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(this.email);
        dest.writeString(this.senha);
    }
    private void readFromParcel(Parcel parcel) {
        //le os dados na ordem que foram escritos
        this.id = parcel.readInt();
        this.email= parcel.readString();
        this.senha= parcel.readString();
    }

    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel p) {
            Usuario eq = new Usuario();
            eq.readFromParcel(p);
            return eq;
        }
        @Override
        public Usuario[] newArray(int size){
            return new Usuario[size];
        }
    };
}
