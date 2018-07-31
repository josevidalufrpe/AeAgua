package com.inova.ufrpe.processos.carropipa.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.inova.ufrpe.processos.carropipa.pagamentos.PagamentosActivity;
import com.inova.ufrpe.processos.carropipa.perfil.PerfilActivity;
import com.inova.ufrpe.processos.carropipa.solicitar.SolicitarActivity;

import java.util.Objects;

public class M_MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener {

    public  static Location localizacao;
    private LocationManager locationManager;
    private GoogleMap mMap;
    private Cliente cliente;

    private static final int REQUEST_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m__main);
        cliente = new Cliente();

        //Pega os dados vindos após o login
        checkPermission();
        Intent autentication = getIntent();
        cliente = Objects.requireNonNull(autentication.getExtras()).getParcelable("cliente");
        Log.d("ID CLIENTE:", Integer.toString(cliente.getId()));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map );
        mapFragment.getMapAsync(this);
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        setNavUserName(navigationView, cliente.getNome(), cliente.getRank()); //seta nome e rank
        setUserEmail(navigationView, cliente.getEmail()); //seta email
        //setUserProfileImage(navigationView, imageUser); //quando implementar foto
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
            perfilAct.putExtra("cliente", cliente);
            startActivity(perfilAct);
            return true;
        }
        return super.onOptionsItemSelected( item );
    }
    //MENU
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_perfil:
                Intent perfilAct = new Intent(M_MainActivity.this, PerfilActivity.class);
                perfilAct.putExtra("cliente", cliente);
                startActivity(perfilAct);
                break;
            case R.id.nav_pedir:
                Intent solicitarAct = new Intent(M_MainActivity.this, SolicitarActivity.class);
                solicitarAct.putExtra( "cliente", cliente);
                startActivity(solicitarAct);
                break;
            case R.id.nav_pagamento:
                Intent pagarAct = new Intent(M_MainActivity.this, PagamentosActivity.class);
                pagarAct.putExtra("cliente", cliente);
                startActivity(pagarAct);
                break;
            case R.id.nav_ajuda:
                break;
            case R.id.nav_sobre:
                break;
            case R.id.nav_sair:
                finish();
                break;
        }
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//Esses 2 metodos setam os campos na navgationView
    private void setUserEmail(NavigationView navView, String email) {

        View headerView = navView.getHeaderView(0);
        TextView userEmail = headerView.findViewById(R.id.tv_emaiiuser);
        userEmail.setText(email);
        userEmail.setTextColor(getResources().getColor(R.color.primaryTextColor));
    }

    private void setNavUserName(NavigationView navView, String nome, String rank) {

        View headerView = navView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.tv_nomeuser);
        userName.setText(String.format("%s - Rank: %s", nome, rank));
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
        checkPermission();  //de novo???
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;     //RETORNA O Q??? num é void??
            }
            mMap.setMyLocationEnabled(true);
            goToCurrentLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    localizacao = location;
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(String provider) { }

                @Override
                public void onProviderDisabled(String provider) { }
            });
            localizacao = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mMap.setMyLocationEnabled(true);
            goToCurrentLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    localizacao = location;
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) { }

                @Override
                public void onProviderEnabled(String provider) { }

                @Override
                public void onProviderDisabled(String provider) { }
            });
        }
        localizacao = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    public void goToCurrentLocation(Location location){
        if(location!= null){
            localizacao = location;
            Toast.makeText( getApplicationContext(),"Pegamos sua localização",Toast.LENGTH_LONG ).show();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) { }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
