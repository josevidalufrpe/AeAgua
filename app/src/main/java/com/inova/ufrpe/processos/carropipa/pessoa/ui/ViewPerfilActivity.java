package com.inova.ufrpe.processos.carropipa.pessoa.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inova.ufrpe.processos.carropipa.R;
import com.inova.ufrpe.processos.carropipa.cliente.dominio.Cliente;
import com.inova.ufrpe.processos.carropipa.perfil.PerfilActivity;

import java.util.Objects;

public class ViewPerfilActivity extends AppCompatActivity {

    private String user_email;
    private Cliente cliente = new Cliente();
    private Button btn_alterar;

    private TextView tv_nome;
    private TextView tv_Snome;
    private TextView tv_email;
    private TextView tv_cpf;
    private TextView tv_telefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_view_perfil );

        Intent autentication = getIntent();
        user_email = autentication.getStringExtra("email");
        cliente = Objects.requireNonNull(autentication.getExtras()).getParcelable( "cliente" );

        tv_email = findViewById( R.id.tv_EmailUsuario );
        tv_nome = findViewById( R.id.tv_NomeUsuario );
        tv_Snome = findViewById( R.id.tv_sobreNomeUsuario );
        tv_cpf = findViewById( R.id.tv_Cpfusuario );
        tv_telefone = findViewById( R.id.tv_TelefoneUsuario );

        tv_email.setText( cliente.getEmail());
        tv_nome.setText( cliente.getNome());
        tv_Snome.setText( cliente.getSobreNome());
        tv_cpf.setText( cliente.getCpf());
        tv_telefone.setText( cliente.getTelefone());

        btn_alterar = findViewById( R.id.btn_alterar );
        btn_alterar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent perfilAct = new Intent(ViewPerfilActivity.this, PerfilActivity.class);
                perfilAct.putExtra("email", user_email);
                perfilAct.putExtra("cliente",cliente);
                startActivity(perfilAct);
                finish();
            }
        } );

    }
}
