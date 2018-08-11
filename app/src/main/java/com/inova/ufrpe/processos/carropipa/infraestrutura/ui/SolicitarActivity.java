package com.inova.ufrpe.processos.carropipa.infraestrutura.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inova.ufrpe.processos.carropipa.R;
import com.inova.ufrpe.processos.carropipa.cliente.dominio.Cliente;
import com.inova.ufrpe.processos.carropipa.infraestrutura.serverlayer.Conexao;
import com.inova.ufrpe.processos.carropipa.motorista.dominio.EnumQuatd;
import com.inova.ufrpe.processos.carropipa.pedido.dominio.Pedido;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class SolicitarActivity extends AppCompatActivity {

    private Spinner quantidade;
    private Button solicitar;
    private DatabaseReference databaseReference;
    private Pedido pedido = new Pedido();
    private Location location = M_MainActivity.localizacao;
    private String parametros;

    private Cliente cliente = new Cliente();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_solicitar );

        Intent autentication = getIntent();
        cliente = (Cliente) autentication.getExtras().getSerializable( "cliente" );

        quantidade = findViewById(R.id.spn_qtd);

        ArrayAdapter<String> enumStadosArrayAdapter;
        enumStadosArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EnumQuatd.EnumQuatdLista());
        quantidade.setAdapter(enumStadosArrayAdapter);
        enumStadosArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        solicitar = findViewById( R.id.btn_solicitar );

        solicitar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantidadePedida = quantidade.getSelectedItem().toString();

                if(!quantidadePedida.equals( "Escolha a Quantidade de Água" )) {
                    if (cliente.getPessoa().getCpf().equals("0")) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(SolicitarActivity.this);
                        final EditText input = new EditText(SolicitarActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        builder.setView(input);
                        builder.setTitle(R.string.informe_cpf)
                                .setPositiveButton(R.string.btn_confirmar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        cliente.getPessoa().setCpf(input.getText().toString());
                                        setarPedido();
                                    }
                                })
                                .setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                        finish();
                                    }
                                });
                        // Create the AlertDialog object and return it
                        builder.create();
                        builder.show();
                    }
                }else{
                    Toast.makeText( getApplicationContext(), R.string.erroEnumQtd, Toast.LENGTH_LONG ).show();
                }
            }
        } );
    }

    private void setarPedido() {
        pedido.setCliente(cliente);
        pedido.setLatitude(location.getLatitude());
        pedido.setLongitude(location.getLongitude());
        pedido.setQuantidade(quantidade.getSelectedItem().toString());
        pedido.setValor(new Random().nextInt((500-150)+1));

        databaseReference = FirebaseDatabase.getInstance()
                .getReference("pedido").child(cliente.getPessoa().getCpf());
        databaseReference.setValue(pedido);

        savePedido();
    }

    public void savePedido(){
        String url = "http://10.246.1.121:5000/cadastro/cadastrarpedido";
        //String url = "http://192.168.1.101:5000/login/getperfil";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YY - HH:mm");
        Date datahora = Calendar.getInstance().getTime();
        String dataini = simpleDateFormat.format(datahora);

        parametros = "horaini="+ dataini + "&valor=" + pedido.getValor() + "&clienteid=" + cliente.getId();
        Log.e("ENVIOU", parametros);
        new SolicitaDados().execute(url);
    }
    private class SolicitaDados extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            Log.e("Chamou", url[0]);
            return Conexao.postDados(url[0], parametros);
        }

        //exibe os resultados
        @Override
        protected void onPostExecute(String results) {

            //Criado para tratar a nova String vinda do Servidor;

            String[] resultado = results.split( "," );
            //Log.d("OLHO NO LANCE!",resultado[1]);
            //TODO falta verificar se é juridica ou fisica
            // está so pegando a resposta de fisica
            Log.e("ERROU", results);
            if (resultado[0].contains( "Cadastration_ok" )) {
                Toast.makeText(getApplicationContext(), R.string.trueEnumQtd, Toast.LENGTH_LONG).show();
                finish();
            }else {
                Toast.makeText(getApplicationContext(), R.string.cadastration_failed, Toast.LENGTH_LONG).show();
            }
        }
    }
}
