package com.inova.ufrpe.processos.carropipa.solicitar;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inova.ufrpe.processos.carropipa.R;
import com.inova.ufrpe.processos.carropipa.cliente.dominio.Cliente;
import com.inova.ufrpe.processos.carropipa.home.M_MainActivity;
import com.inova.ufrpe.processos.carropipa.motorista.dominio.EnumQuatd;
import com.inova.ufrpe.processos.carropipa.pedido.dominio.Pedido;

public class SolicitarActivity extends AppCompatActivity {

    private Spinner quantidade;
    private DatabaseReference databaseReference;
    private Pedido pedido = new Pedido();
    private Location location = M_MainActivity.localizacao;

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


        Button solicitar = findViewById(R.id.btn_solicitar);

        solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantidadePedida = quantidade.getSelectedItem().toString();

                if(!quantidadePedida.equals( "Escolha a Quantidade de ajuda" )) {
                    //Cliente cliente = new Cliente();
                    //cliente.setId( 1 );
                    //Pessoa pessoa = new Pessoa();
                    //pessoa.setNome( "VIDAL" );
                    //pessoa.setId( (long) 1 );
                    cliente.setCpf( "100" );
                    //Usuario usuario = new Usuario();
                    //usuario.setId( 1 );
                    //pessoa.setUsuario( usuario );
                    //cliente.setPessoa( pessoa );

                    pedido.setCliente(cliente);
                    pedido.setLatitude(location.getLatitude());
                    pedido.setLongitude(location.getLongitude());
                    pedido.setQuantidade(quantidade.getSelectedItem().toString());

                    databaseReference = FirebaseDatabase.getInstance()
                            .getReference("pedido").child( cliente.getCpf());
                    databaseReference.setValue(pedido);

                    Toast.makeText(getApplicationContext(), R.string.trueEnumQtd, Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.erroEnumQtd, Toast.LENGTH_LONG).show();
                }
            }
        } );
    }
}
