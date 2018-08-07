package com.inova.ufrpe.processos.carropipa.infraestrutura.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inova.ufrpe.processos.carropipa.R;
import com.inova.ufrpe.processos.carropipa.cliente.dominio.Cliente;
import com.inova.ufrpe.processos.carropipa.infraestrutura.ui.M_MainActivity;
import com.inova.ufrpe.processos.carropipa.infraestrutura.ui.PerfilActivity;
import com.inova.ufrpe.processos.carropipa.motorista.dominio.Motorista;

import static com.inova.ufrpe.processos.carropipa.infraestrutura.ui.M_MainActivity.motorista;

public class ViewPerfilActivity extends AppCompatActivity {

    private String user_email;
    private Motorista motorista = M_MainActivity.motorista;
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

        //Intent autentication = getIntent();
        //motorista = (Motorista) autentication.getExtras().getSerializable( "motorista" );
        user_email = motorista.getPessoa().getUsuario().getEmail();

        tv_email = findViewById( R.id.tv_EmailUsuario );
        tv_nome = findViewById( R.id.tv_NomeUsuario );
        tv_Snome = findViewById( R.id.tv_sobreNomeUsuario );
        tv_cpf = findViewById( R.id.tv_Cpfusuario );
        tv_telefone = findViewById( R.id.tv_TelefoneUsuario );

        tv_email.setText( motorista.getPessoa().getUsuario().getEmail());
        tv_nome.setText( motorista.getPessoa().getNome() );
        tv_Snome.setText( motorista.getPessoa().getSnome() );
        tv_cpf.setText( motorista.getPessoa().getCpf() );
        tv_telefone.setText( motorista.getPessoa().getTelefone() );


        btn_alterar = findViewById( R.id.btn_alterar );
        btn_alterar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent perfilAct = new Intent(ViewPerfilActivity.this, PerfilActivity.class);
                perfilAct.putExtra("email", user_email);
                Bundle bundle = new Bundle(  );
                bundle.putSerializable("motorista",motorista);
                perfilAct.putExtras( bundle );
                startActivity(perfilAct);
                finish();
            }
        } );

    }
}