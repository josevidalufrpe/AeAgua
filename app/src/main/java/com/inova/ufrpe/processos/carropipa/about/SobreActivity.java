package com.inova.ufrpe.processos.carropipa.about;

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
        textview.setText(R.string.Str_About);
    }
}
