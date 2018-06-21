package com.inova.ufrpe.processos.carropipa.infraestrutura.ui;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.inova.ufrpe.processos.carropipa.R;

public class M_MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String user_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_m__main );
        Intent autentication = getIntent();
        user_email = autentication.getStringExtra("email");

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        // BOTÃO DA POSIÇÃO NO MAPA
        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // chama o mapa
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace( R.id.context_frame, new HomeMapsActivity() ).commit();
               // Snackbar.make( view, "Replace with your own action", Snackbar.LENGTH_LONG )
               //         .setAction( "Action", null ).show();

            }
        } );

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.m__main, menu );
        return true;
    }
    //OS TRES PONTINHO
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent perfilAct= new Intent(M_MainActivity.this,PerfilActivity.class);
            startActivity(perfilAct);


            return true;
        }

        return super.onOptionsItemSelected( item );
    }
    //MENU
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            Intent perfilAct= new Intent(M_MainActivity.this,PerfilActivity.class);
            perfilAct.putExtra("email",user_email);
            startActivity(perfilAct);
        } else if (id == R.id.nav_pedir) {
            Intent solicitarAct= new Intent(M_MainActivity.this,SolicitarActivity.class);
            startActivity(solicitarAct);
        } else if (id == R.id.nav_pagamento) {

        } else if (id == R.id.nav_ajuda) {

        } else if (id == R.id.nav_sobre) {

        } else if (id == R.id.nav_sair) {
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }
}
