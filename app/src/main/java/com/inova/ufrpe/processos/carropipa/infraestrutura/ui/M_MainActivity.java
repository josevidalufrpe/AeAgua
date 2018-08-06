package com.inova.ufrpe.processos.carropipa.infraestrutura.ui;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inova.ufrpe.processos.carropipa.R;
import com.inova.ufrpe.processos.carropipa.ajuda.AjudaActivity;
import com.inova.ufrpe.processos.carropipa.motorista.dominio.Motorista;
import com.inova.ufrpe.processos.carropipa.pagamentos.PagamentosActivity;
import com.inova.ufrpe.processos.carropipa.pedido.dominio.Pedido;
import com.inova.ufrpe.processos.carropipa.pessoa.dominio.Pessoa;
import com.inova.ufrpe.processos.carropipa.usuario.dominio.Usuario;

import java.util.HashMap;

public class M_MainActivity extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener {

    private String user_email;
    private FirebaseDatabase database;
    private LocationManager locationManager;
    private Location localizacao;
    private static final int REQUEST_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private Pedido pedido;
    private String destinolongitude;
    private String destinolatitude;
    private Motorista motorista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_m__main );
        checkPermission();
        Intent autentication = getIntent();
        motorista = (Motorista) autentication.getExtras().getSerializable("motorista");
        Usuario recUsuario = (Usuario) autentication.getExtras().getParcelable("usuario");
        motorista.getPessoa().setUsuario(  recUsuario);
        database = FirebaseDatabase.getInstance();
        pedido = new Pedido();
        DatabaseReference myRef = database.getReference("pedido").child("100");//bota cpf do cara
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                pedido.setQuantidade( dataSnapshot.child( "quantidade" ).getValue().toString() );
                Pessoa pessoa = new Pessoa();
                pessoa.setNome( dataSnapshot.child( "cliente" ).child("nome").getValue().toString() );
                destinolatitude = dataSnapshot.child("latitude" ).getValue().toString();
                destinolongitude = dataSnapshot.child("longitude" ).getValue().toString();

                // tela de aceitação de chamado
                AlertDialog.Builder chamado = new AlertDialog.Builder(M_MainActivity.this);
                View ViewChamado = getLayoutInflater().inflate(R.layout.activity_chamado, null);
                TextView textviewNomeCliente = ViewChamado.findViewById( R.id.tv_nomeC );
                TextView textviewQuantidade = ViewChamado.findViewById( R.id.tv_quantidade );
                textviewNomeCliente.setText( pessoa.getNome() );
                textviewQuantidade.setText( pedido.getQuantidade() );
                Button botaoAceitar = ViewChamado.findViewById(R.id.btn_aceitar );
                Button botaoNegar = ViewChamado.findViewById( R.id.btn_negar );



                chamado.setView(ViewChamado);
                final AlertDialog dialog2 = chamado.create();
                dialog2.show();
                botaoAceitar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText( getApplicationContext(),"Chamado Aceito ",Toast.LENGTH_LONG ).show();
                        //Chama o mapa
                        dialog2.dismiss();
                        try {
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?saddr=&daddr=" + destinolatitude + "," + destinolongitude));

                            intent.setComponent(new ComponentName(getString(R.string.comandoAppMaps), getString(R.string.comandoMapsActivity)));

                            startActivity(intent);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(M_MainActivity.this, R.string.erroNaoTemGoogleMaps, Toast.LENGTH_SHORT).show();
                        }
                    }

                });


                botaoNegar.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText( getApplicationContext(),"Chamado Negado ",Toast.LENGTH_LONG ).show();
                        dialog2.dismiss();
                    }
                } );


                //Log.d(TAG, "Value is: " + value);
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText( getApplicationContext(),"Errou ao Baixar",Toast.LENGTH_LONG ).show();
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //Pega os dados vindos após o login

        user_email = motorista.getPessoa().getUsuario().getEmail();

        String user_name = motorista.getPessoa().getNome();
        String user_sname = motorista.getPessoa().getSnome();
        String user_rank = motorista.getRank();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );
        //fim de Pega os dados vindos após o login
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        setNavUserName(navigationView, user_name, user_sname, user_rank); //seta nome e rank
        setUserEmail(navigationView, user_email); //seta email
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_perfil:
                Intent perfilAct = new Intent(M_MainActivity.this, PerfilActivity.class);
                Bundle bundle = new Bundle(  );
                bundle.putSerializable("motorista",motorista);
                perfilAct.putExtras( bundle );
                startActivity(perfilAct);
                break;
            case R.id.nav_pagamento:

                Intent viewPagAct = new Intent(M_MainActivity.this, PagamentosActivity.class);
                Bundle bundle2 = new Bundle(  );
                bundle2.putSerializable("motorista",motorista);
                viewPagAct.putExtras( bundle2 );
                startActivity(viewPagAct);

                break;
            case R.id.nav_ajuda:

                Intent ajudaAct = new Intent(M_MainActivity.this, AjudaActivity.class );
                startActivity( ajudaAct );
                break;
            case R.id.nav_sobre:

                Intent sobreAct = new Intent(M_MainActivity.this, SobreActivity.class);
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

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mMap.setMyLocationEnabled(true);
            goToCurrentLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
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
    }

    public void goToCurrentLocation(Location location){
        if(location!= null){
            localizacao = location;
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));}
    }
}
