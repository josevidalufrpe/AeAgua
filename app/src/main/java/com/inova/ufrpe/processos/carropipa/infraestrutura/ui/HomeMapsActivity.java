package com.inova.ufrpe.processos.carropipa.infraestrutura.ui;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inova.ufrpe.processos.carropipa.R;

public class HomeMapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate( R.layout.activity_home_maps,container,false );
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated( view,savedInstanceState );
        MapFragment fragment;
        fragment =(MapFragment)getChildFragmentManager().findFragmentById( R.id.map_home );
        fragment.getMapAsync( this );
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng( -8.0176527, -34.9465626 );
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( sydney,14.0f ) );
        mMap.addMarker( new MarkerOptions().position( sydney ).title( "Local Atual " ));
    }
}
