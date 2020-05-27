package com.mcakiroglu.sellout.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.models.Nearby;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShowOnMap extends AppCompatActivity implements OnMapReadyCallback {
    DatabaseReference mDatabase;
    ArrayList<Nearby> near = new ArrayList<>();
    String city;
    LocationManager locationManager;
    LocationListener locationListener;
    Location locationx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_on_map);
        mDatabase = FirebaseDatabase.getInstance().getReference();




        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapp);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final GoogleMap gMap = googleMap;
        final Button btn_MapType=(Button) findViewById(R.id.satt);
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
        gMap.setMyLocationEnabled(true);

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        //self note: add exception for current location getter code thingy
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,15,locationListener);

        locationx = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Locale locale = new Locale("tr", "TR");
        Geocoder geocoder = new Geocoder(getApplicationContext(), locale);


        if(geocoder.isPresent()){
            try {

                List<Address> list =geocoder.getFromLocation(locationx.getLatitude(),locationx.getLongitude(),10);
                Address address = list.get(0);
                city = address.getAdminArea();
            } catch (NullPointerException | IOException e) {

                Intent intent = new Intent(this,MainPage.class);
                Toast.makeText(this, R.string.mapnono,Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        }
try {
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng( locationx.getLatitude(), locationx.getLongitude()),8));
}catch (NullPointerException e){
    Toast.makeText(this,"Güncel konumunuz alınamıyor.",Toast.LENGTH_SHORT).show();
    return;
}
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot prop = dataSnapshot.child("cityProducts").child(city);
                Iterable<DataSnapshot> propdetails =prop.getChildren();
                for(DataSnapshot snap :propdetails){

                    Nearby n =snap.getValue(Nearby.class);
                    LatLng loc =new LatLng(n.getLat(),n.getLon());

                    gMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(loc).title("Name: " + n.getName() +"\n" +"Price: " +  n.getPrice()));
                    //Toast.makeText(ShowOnMap.this, n.getName() + n.getCity() + n.getPrice(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









    }




}
