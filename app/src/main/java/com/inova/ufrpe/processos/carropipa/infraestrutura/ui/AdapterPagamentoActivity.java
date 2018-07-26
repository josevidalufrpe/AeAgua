package com.inova.ufrpe.processos.carropipa.infraestrutura.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inova.ufrpe.processos.carropipa.R;
import com.inova.ufrpe.processos.carropipa.pedido.dominio.Pedido;

import java.util.ArrayList;

public class AdapterPagamentoActivity extends BaseAdapter {

    private ArrayList<Pedido> pedidoArrayList = new ArrayList<>(  );

    private final Activity act;

    public AdapterPagamentoActivity(ArrayList<Pedido>  pedidos,Activity act) {
        this.pedidoArrayList = pedidos;
        this.act = act;
    }

    @Override
    public int getCount() {
        return pedidoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return pedidoArrayList.get( position );
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Pedido pedido = pedidoArrayList.get(position);

        View view = act.getLayoutInflater().inflate( R.layout.activity_adapter_pagamento, parent, false );

        TextView nomeMotorista = (TextView) view.findViewById( R.id.tv_pag_NomeMotorista );
        TextView quantidade = (TextView) view.findViewById( R.id.tv_pag_qtd );
        TextView valor = (TextView) view.findViewById( R.id.tv_pag_valor );

        nomeMotorista.setText( pedido.getMotorista().getPessoa().getNome() );
        quantidade.setText( pedido.getQuantidade() );
        valor.setText( String.valueOf( pedido.getValor()) );

        return view;
    }
}
