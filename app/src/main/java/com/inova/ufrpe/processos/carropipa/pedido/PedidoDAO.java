package com.inova.ufrpe.processos.carropipa.pedido;

import android.content.Context;
import android.util.Log;

import com.inova.ufrpe.processos.carropipa.cliente.dominio.Cliente;
import com.inova.ufrpe.processos.carropipa.infraestrutura.serverlayer.Conectar;
import com.inova.ufrpe.processos.carropipa.pedido.dominio.Pedido;

import java.util.ArrayList;

public class PedidoDAO implements Conectar.OnCadastroListener{
    private Boolean response;
    private String parametros;
    private Context context;
    private Conectar conectar;

    public PedidoDAO(Context context) {
        this.context = context;
        this.conectar = Conectar.getInstance();
        conectar.setOnCadastroListener(PedidoDAO.this);
    }

    public void salva(Pedido pedido) {

        if (Conectar.isConnected(context)){
            Log.d("TESTANDO: ", "conectado");
            if(pedido.getId() != 0){                                                               //cliente veio do banco
                Log.d("TESTANDO: ", "veio do banco!");
                parametros = "id="+ pedido.getId() + "&valor=" + pedido.getValor() + "&clienteid="
                        + pedido.getCliente().getId() + "&motoid=" + pedido.getMotorista().getId();  //Na moral essa história de string
                String url = "http://192.168.0.102:5000/pedido/fazer";
                conectar.doConnect(context, url, parametros);
                Log.d("resposta: ", response.toString());
            }else{                                                                                  //novo cliente
                Log.d("TESTANDO: ", "novo cliente");
                parametros = "valor=" + pedido.getValor();
                String url = "http://192.168.195.104:5000/pedido/fazer";
                conectar.doConnect(context, url, parametros);
            }
            //Log.d("resposta: ", response.toString());
        }
    }

        /**
         * metodo findAll - Retorna todas as pessoas cadastradas no banco remoto
         *
         * @return : Uma lista com todas as pessoas cadastradas no banco
         */
    public void findAll(Cliente cliente){
        parametros = "userid=" + cliente.getId();
        String url = "http://192.168.195.104:5000/pedido/ler";
        conectar.doConnect(context, url, parametros);
    }


    @Override
    public void onCadastro(String result) {
        Log.d("TESTANDO: ", "voltei com resposta");
        if (result.contains("cadastration_ok")){
            sucess();
        }else{
            fail();
        }
    }


    private void fail() {
        response = false;
    }

    private void sucess() {
        response = true;
    }
}
