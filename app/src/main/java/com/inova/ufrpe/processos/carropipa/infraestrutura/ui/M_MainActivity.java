package com.inova.ufrpe.processos.carropipa.infraestrutura.ui;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.text.StaticLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.inova.ufrpe.processos.carropipa.R;
import com.inova.ufrpe.processos.carropipa.cliente.dominio.Cliente;
import com.inova.ufrpe.processos.carropipa.infraestrutura.serverlayer.Conexao;
import com.inova.ufrpe.processos.carropipa.pessoa.dominio.Pessoa;
import com.inova.ufrpe.processos.carropipa.pessoa.ui.ViewPerfilActivity;
import com.inova.ufrpe.processos.carropipa.usuario.dominio.Usuario;

import java.io.Serializable;

public class M_MainActivity extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private String user_email;
    private String user_name;
    private String user_sname;
    private String user_rank;
    private LocationManager locationManager;
    public  static Location localizacao;
    private static final int REQUEST_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private Cliente cliente = new Cliente();
    private String parametros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_m__main );
        //Pega os dados vindos após o login
        checkPermission();
        setarInformaçoes();
        //fim de Pega os dados vindos após o login
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();
        parametros = "email=" + cliente.getPessoa().getUsuario().getEmail() +"&senha=" + cliente.getPessoa().getUsuario().getSenha();
        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        setNavUserName(navigationView, cliente.getPessoa().getNome(), cliente.getPessoa().getSnome(), cliente.getRank()); //seta nome e rank
        setUserEmail(navigationView, cliente.getPessoa().getUsuario().getEmail()); //seta email
        //setUserProfileImage(navigationView, imageUser); //quando implementar foto
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //TODO tenho que atualizar ao abrir o menu lateral
        getPerfil();
        //
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_perfil:
                if(cliente.getPessoa().getCpf().equals( "0")){
                    Intent perfilAct = new Intent(M_MainActivity.this, PerfilActivity.class);
                    perfilAct.putExtra("email", user_email);
                    perfilAct.putExtra("cliente",cliente);
                    startActivity(perfilAct);
                }
                else{
                    Intent perfilAct = new Intent(M_MainActivity.this, ViewPerfilActivity.class);
                    perfilAct.putExtra("email", user_email);
                    perfilAct.putExtra("cliente",cliente);
                    startActivity(perfilAct);
                }
                break;
            case R.id.nav_pedir:
                Intent solicitarAct = new Intent(M_MainActivity.this, SolicitarActivity.class);
                solicitarAct.putExtra( "cliente",cliente);
                startActivity(solicitarAct);
                break;
            case R.id.nav_pagamento:
                Intent viewPagAct = new Intent(M_MainActivity.this, PagamentosActivity.class);
                viewPagAct.putExtra( "cliente",cliente);
                startActivity(viewPagAct);
                break;
            case R.id.nav_ajuda:
                Intent ajudaAct = new Intent(M_MainActivity.this, AjudaActivity.class);
                ajudaAct.putExtra( "cliente",cliente);
                startActivity(ajudaAct);
                break;
            case R.id.nav_sobre:
                Intent sobreAct = new Intent(M_MainActivity.this, SobreActivity.class);
                sobreAct.putExtra( "cliente",cliente);
                startActivity(sobreAct);
                break;
            case R.id.nav_sair:
                finish();

                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }
//Esses 2 metodos setam os campos na navgationView
    private void setUserEmail(NavigationView navView, String email) {

        View headerView = navView.getHeaderView(0);
        TextView userEmail = headerView.findViewById(R.id.tv_emaiiuser);
        userEmail.setText(email);
        userEmail.setTextColor(getResources().getColor(R.color.primaryTextColor));
    }

    private void setNavUserName(NavigationView navView, String nome, String segundoNome, String rank) {

        View headerView = navView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.tv_nomeuser);
        userName.setText(String.format("%s %s - Rank: %s", nome, segundoNome, rank));
        userName.setTextColor(getResources().getColor(R.color.primaryTextColor));
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        updateLocation();

    }

    private void checkPermission() {
        boolean permissionFineLocation = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean permissionCoarseLocation = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;

        if (permissionFineLocation && permissionCoarseLocation) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                updateLocation();
            }
        }
    }

    public void updateLocation() {
        checkPermission();
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED /*&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED*/) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            goToCurrentLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    localizacao = location;
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
            localizacao = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mMap.setMyLocationEnabled(true);
            goToCurrentLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    localizacao = location;
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
        localizacao = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    public void goToCurrentLocation(Location location){
        if(location!= null){
            localizacao = location;
            Toast.makeText(getApplicationContext(),"Pegamos sua localização",Toast.LENGTH_LONG ).show();

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));}
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
    public void setarInformaçoes(){
        Intent autentication = getIntent();
        user_email = autentication.getStringExtra("email");
        user_name = autentication.getStringExtra("nome");
        user_sname = autentication.getStringExtra("snome");
        user_rank = autentication.getStringExtra("rank");
        //passando objeto teste
        cliente = (Cliente) autentication.getExtras().getSerializable( "cliente" );
    }
    public void getPerfil(){
        String url = "http://10.246.217.119:5000/login/getperfil";
        //String url = "http://192.168.1.101:5000/login/getperfil";
        new SolicitaDados().execute(url);
    }
    private class SolicitaDados extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            return Conexao.postDados(url[0], parametros);
        }

        //exibe os resultados
        @Override
        protected void onPostExecute(String results) {

            //Criado para tratar a nova String vinda do Servidor;

            String[] resultado = results.split( "," );
            //Log.d("OLHO NO LANCE!",resultado[1]);
            //TODO falta verificar se é juridica ou fisica
            // está so pegando a resposta de fisica
            if (resultado[0].contains( "login_ok" )) {
                Usuario usuario = new Usuario();
                Pessoa pessoa = new Pessoa();
                Cliente clienteat = new Cliente();
                //exibir toast apenas para verificar os dados q chegam do servidor
                //Criar objeto usuario
                usuario.setId( Integer.parseInt( resultado[1] ) );
                usuario.setEmail( resultado[2] );
                usuario.setSenha( resultado[3] );
                //Criar objeto pessoa
                pessoa.setId( Long.valueOf( resultado[4] ) );
                pessoa.setNome( resultado[5] );
                pessoa.setSnome( resultado[6] );
                if (resultado.length == 11) {
                    pessoa.setCpf( resultado[7] );
                    pessoa.setTelefone( resultado[8] );
                    clienteat.setId( Integer.parseInt( resultado[9] ) );
                    clienteat.setRank( resultado[10] );
                } else {
                    pessoa.setCpf( "0" );
                    clienteat.setId( Integer.parseInt( resultado[7] ) );
                    clienteat.setRank( resultado[8] );

                }

                //Unir todos objeto em 1 só
                pessoa.setUsuario( usuario );
                clienteat.setPessoa( pessoa );
                cliente = clienteat;
            }
        }
    }
}
