package com.inova.ufrpe.processos.carropipa.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.inova.ufrpe.processos.carropipa.R;
import com.inova.ufrpe.processos.carropipa.criarconta.CriarContaUsuarioFinalActivity;
import com.inova.ufrpe.processos.carropipa.login.LoginActivity;

public class ApresentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_apresent );
        Button btn_Criar = findViewById(R.id.button2);
        Button btn_Entrar = findViewById(R.id.btn_entrar);

        btn_Entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ApresentActivity.this,LoginActivity.class));
            }
        } );
        btn_Criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ApresentActivity.this,CriarContaUsuarioFinalActivity.class));
            }
        } );

    }
}
