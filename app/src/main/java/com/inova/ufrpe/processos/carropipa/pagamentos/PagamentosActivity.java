package com.inova.ufrpe.processos.carropipa.pagamentos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.inova.ufrpe.processos.carropipa.R;
import com.inova.ufrpe.processos.carropipa.motorista.dominio.Motorista;
import com.inova.ufrpe.processos.carropipa.pedido.dominio.Pedido;
import com.inova.ufrpe.processos.carropipa.pessoa.dominio.Pessoa;

import java.util.ArrayList;

public class PagamentosActivity extends AppCompatActivity {

    private ArrayList<Pedido> pedidoArrayList = new ArrayList<>(  );
    private AdapterPagamentoActivity adapterPagamentoActivity;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_pagamentos );
        Pedido testePedido = new Pedido();
        Motorista testeMotorista = new Motorista();
        Pessoa testePessoa = new Pessoa();

        testePessoa.setNome( "Chico" );
        testeMotorista.setPessoa( testePessoa );

        testePedido.setMotorista( testeMotorista );
        testePedido.setQuantidade( "66666" );
        testePedido.setValor( 100 );

        pedidoArrayList.add(testePedido);


        //Pegar todos pedido do cliente
        // para alimentar a lista de pedidos

        adapterPagamentoActivity = new AdapterPagamentoActivity( pedidoArrayList,PagamentosActivity.this );

        listView = findViewById( R.id.listview_pag );
        listView.setAdapter( adapterPagamentoActivity );

    }
}
