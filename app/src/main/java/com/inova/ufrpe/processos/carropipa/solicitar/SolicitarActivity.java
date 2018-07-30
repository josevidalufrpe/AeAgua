package com.inova.ufrpe.processos.carropipa.solicitar;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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
import com.inova.ufrpe.processos.carropipa.cliente.persistence.ClienteDAO;
import com.inova.ufrpe.processos.carropipa.home.M_MainActivity;
import com.inova.ufrpe.processos.carropipa.pedido.dominio.EnumQuatd;
import com.inova.ufrpe.processos.carropipa.pedido.dominio.Pedido;

public class SolicitarActivity extends AppCompatActivity {

    private Spinner quantidade;
    private DatabaseReference databaseReference;
    private Pedido pedido = new Pedido();
    private Location location = M_MainActivity.localizacao;
    private Cliente cliente = new Cliente();
    private String m_Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar);

        Intent autentication = getIntent();
        cliente = autentication.getExtras().getParcelable("cliente");

        quantidade = findViewById(R.id.spn_qtd);

        ArrayAdapter<String> enumQuantArrayAdapter;
        enumQuantArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EnumQuatd.EnumQuatdLista());
        quantidade.setAdapter(enumQuantArrayAdapter);
        enumQuantArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Button solicitar = findViewById(R.id.btn_solicitar);

        solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantidadePedida = quantidade.getSelectedItem().toString();

                if(!quantidadePedida.equals( "Escolha a Quantidade de Água" )) {
                    String clientecpf = cliente.getCpf();
                    if (clientecpf == null){                                                        //Se não tem cpf cadastrado
                        AlertDialog.Builder builder = new AlertDialog.Builder(SolicitarActivity.this);
                        builder.setTitle("Informe seu CPF:");

// Set up the input
                        final EditText input = new EditText(SolicitarActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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
                    }
                    if(m_Text != null){
                        cliente.setCpf(m_Text);                            //TODO: tem que arrumar isso
                        ClienteDAO clienteDao = new ClienteDAO(SolicitarActivity.this);
                        clienteDao.salva(cliente);                          //Atualiza cliente no banco
                    }
                    else{
                        finish();                                           //n pode prosseguir sem cpf
                    }

                    pedido.setCliente(cliente);
                    pedido.setLatitude(location.getLatitude());
                    pedido.setLongitude(location.getLongitude());
                    pedido.setQuantidade(quantidade.getSelectedItem().toString());

                    databaseReference = FirebaseDatabase.getInstance()
                            .getReference("pedido").child(cliente.getCpf());
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
