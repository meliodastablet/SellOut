package com.mcakiroglu.sellout.activities;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mcakiroglu.sellout.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocation extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,OnMapReadyCallback {

    Marker marker;
   String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        final GoogleMap gMap = googleMap;
        final Button btn_MapType=(Button) findViewById(R.id.sat);
        btn_MapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL){
                    gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    btn_MapType.setText("NORMAL");
                }else{
                    gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    btn_MapType.setText("UYDU");
                }
            }
        });

        UiSettings uiSettings;
        uiSettings= googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng( 39.925533, 32.866287),10));

        Button btn = findViewById(R.id.ping);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s == null || s.length() == 0){
                    Toast.makeText(MyLocation.this, "Bir konum seçin" , Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",s);
                   setResult(Activity.RESULT_OK,returnIntent);
                   finish();
                }
            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker != null) {
                    marker.remove();
                }
                marker = gMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Konumunuz İşaretlendi!"));
                Locale locale = new Locale("tr", "TR");
                Geocoder geocoder = new Geocoder(getApplicationContext(), locale);
                if(geocoder.isPresent()){
                    try {

                        List<Address> list =geocoder.getFromLocation(latLng.latitude,latLng.longitude,10);
                        Address address = list.get(0);
                        s = address.getAddressLine(0);

                    } catch (IOException e) {
                        System.out.println("Rror");
                    }
                }


            }
        });
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MyLocation.this, marker.getPosition().toString() , Toast.LENGTH_SHORT).show();
                return false;
            }
        });



    }
}
