package com.inova.ufrpe.processos.carropipa.solicitar;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inova.ufrpe.processos.carropipa.R;
import com.inova.ufrpe.processos.carropipa.cliente.dominio.Cliente;
import com.inova.ufrpe.processos.carropipa.home.M_MainActivity;
import com.inova.ufrpe.processos.carropipa.pedido.PedidoDAO;
import com.inova.ufrpe.processos.carropipa.pedido.dominio.EnumQuatd;
import com.inova.ufrpe.processos.carropipa.pedido.dominio.Pedido;

import java.util.Objects;

public class SolicitarActivity extends AppCompatActivity {

    private Spinner quantidade;
    private Button solicitar;
    private DatabaseReference databaseReference;
    private Pedido pedido = new Pedido();
    private Location location = M_MainActivity.localizacao;
    private String m_Text;

    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_solicitar );

        //Intent solicitarAct = getIntent();
        Bundle args =getIntent().getExtras();   //recupera os arguementos passados
        cliente = new Cliente();
        cliente = args.getParcelable("cliente");
        Log.d("SOLICITAR: ", Integer.toString(cliente.getId()));

        quantidade = findViewById(R.id.spn_qtd);

        ArrayAdapter<String> QuantidadeArrayAdapter;
        QuantidadeArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EnumQuatd.EnumQuatdLista());
        quantidade.setAdapter(QuantidadeArrayAdapter);
        QuantidadeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        solicitar = findViewById( R.id.btn_solicitar );

        solicitar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantidadePedida = quantidade.getSelectedItem().toString();
                String cpf;
                try{
                    cpf = cliente.getCpf();
                }catch (Exception e){
                    cpf = "0";
                }
                if(!quantidadePedida.equals( "Escolha a Quantidade de Água" )) {
                    if(cpf.equals("0")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(SolicitarActivity.this);
                        builder.setTitle("Informe seu CPF:");                                       // Set up the input
                        final EditText input = new EditText(SolicitarActivity.this);
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL | InputType.TYPE_CLASS_NUMBER);
                        builder.setView(input);

                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m_Text = input.getText().toString();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                        cliente.setCpf(m_Text);
                    }

                    //Usuario usuario = new Usuario();
                    //usuario.setId( 1 );
                    //pessoa.setUsuario( usuario );
                    //cliente.setPessoa( pessoa );

                    pedido.setCliente(cliente);
                    pedido.setLatitude( location.getLatitude() );
                    pedido.setLongitude( location.getLongitude() );
                    pedido.setQuantidade( quantidade.getSelectedItem().toString() );
                    pedido.setValor(350.0);


                    databaseReference = FirebaseDatabase.getInstance()
                            .getReference( "pedido" ).child(cliente.getNome());
                    databaseReference.setValue( pedido );


                    PedidoDAO pedidoDAO = new PedidoDAO(SolicitarActivity.this);
                    pedidoDAO.salva(pedido);
                    Toast.makeText( getApplicationContext(), R.string.trueEnumQtd, Toast.LENGTH_LONG ).show();
                    finish();
                }
                else{
                    Toast.makeText( getApplicationContext(), R.string.erroEnumQtd, Toast.LENGTH_LONG ).show();
                }
            }
        } );
    }
}