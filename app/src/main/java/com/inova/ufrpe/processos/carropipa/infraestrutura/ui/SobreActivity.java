package com.inova.ufrpe.processos.carropipa.infraestrutura.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.inova.ufrpe.processos.carropipa.R;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sobre );

        TextView textview = findViewById( R.id.textView10 );
        textview.setText("Esse Aplicativo foi feito por 2 Discentes de Barchalerado em Sistemas de Informação para ajuda na solucionar a falta de agua em certas:  ==> Felipe Caetano <==  ==>    José Vidal      <==  para a disciplina de Processo 2018.1");
    }
}
