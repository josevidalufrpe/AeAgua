package com.inova.ufrpe.processos.carropipa.infraestrutura.ui;

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
import com.inova.ufrpe.processos.carropipa.motorista.dominio.EnumQuatd;
import com.inova.ufrpe.processos.carropipa.pedido.dominio.Pedido;
import com.inova.ufrpe.processos.carropipa.pessoa.dominio.EnumStados;
import com.inova.ufrpe.processos.carropipa.pessoa.dominio.Pessoa;
import com.inova.ufrpe.processos.carropipa.usuario.dominio.Usuario;

public class SolicitarActivity extends AppCompatActivity {

    private Spinner quantidade;
    private Button solicitar;
    private DatabaseReference databaseReference;
    private Pedido pedido;
    private Location location = M_MainActivity.localizacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_solicitar );

        quantidade = findViewById(R.id.spn_qtd);
        pedido = new Pedido();
        ArrayAdapter<String> enumStadosArrayAdapter;
        enumStadosArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EnumQuatd.EnumQuatdLista());
        quantidade.setAdapter(enumStadosArrayAdapter);
        enumStadosArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        String quantidadePedida = quantidade.getSelectedItem().toString();

        solicitar = findViewById( R.id.btn_solicitar );

        solicitar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cliente cliente = new Cliente();
                cliente.setId( 1 );
                Pessoa pessoa = new Pessoa();
                pessoa.setNome( "VIDAL" );
                pessoa.setId( (long) 1 );
                pessoa.setCpf("100");
                Usuario usuario = new Usuario();
                usuario.setId(1);
                pessoa.setUsuario( usuario );
                cliente.setPessoa( pessoa );

                pedido.setCliente( cliente );
                pedido.setLatitude( location.getLatitude() );
                pedido.setLongitude( location.getLongitude() );
                pedido.setQuantidade( quantidade.getSelectedItem().toString());
                // databaseReference =FirebaseDatabase.getInstance().getReference("Cu");
                //  databaseReference.setValue( "Rola" );


                databaseReference = FirebaseDatabase.getInstance()
                        .getReference("pedido").child( cliente.getPessoa().getCpf() );
                databaseReference.setValue( pedido );

                Toast.makeText( getApplicationContext(),"Pedido Enviado",Toast.LENGTH_LONG ).show();
            }
        } );

    }
}
