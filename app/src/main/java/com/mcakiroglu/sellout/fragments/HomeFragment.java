package com.mcakiroglu.sellout.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.models.Property;
import com.mcakiroglu.sellout.activities.ShowOnMap;
import com.mcakiroglu.sellout.adapter.ProductAdapter;
import com.mcakiroglu.sellout.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {


    RecyclerView rw1,rw2,rw3;
    FragmentHomeBinding binding;
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=30;

    String uid;
    public ArrayList<ArrayList<Property>> pro =new ArrayList<>();
    public ArrayList<Property> pro2 =new ArrayList<>();
    public ArrayList<Property> pro3 =new ArrayList<>();
    public ArrayList<Property> pro4 =new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    static boolean active = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();




        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        handlers();
    }


    protected void init() {
        binding.geol.setOnClickListener(this);


        rw1 = binding.recyclerView6;
        rw2 = binding.recyclerView7;
        rw3 = binding.recyclerView8;
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid=user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }


    protected void handlers() {


//self note: .orderByChild().limitToFirst(3);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot prop = dataSnapshot.child("categories").child("Cep Telefonu");
                DataSnapshot prop2 = dataSnapshot.child("categories").child("Elektronik");
                DataSnapshot prop3 = dataSnapshot.child("categories").child("Araba");
                Iterable<DataSnapshot> propdetails =prop.getChildren();
                Iterable<DataSnapshot> propdetails2 =prop2.getChildren();
                Iterable<DataSnapshot> propdetails3 =prop3.getChildren();
                for(DataSnapshot snap :propdetails){

                    Property p =snap.getValue(Property.class);
                    p.setCategory("Cep Telefonu");
                    pro2.add(p);
                }
                pro.add(pro2);
                for(DataSnapshot snap :propdetails2){

                    Property p =snap.getValue(Property.class);
                    p.setCategory("Elektronik");
                    pro3.add(p);
                }
                pro.add(pro3);
                for(DataSnapshot snap :propdetails3){

                    Property p =snap.getValue(Property.class);
                    p.setCategory("Araba");
                    pro4.add(p);
                }
                pro.add(pro4);
                custom(pro);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    public void custom(ArrayList<ArrayList<Property>> a){



        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        rw1.setLayoutManager(layoutManager);
        rw2.setLayoutManager(layoutManager2);
        rw3.setLayoutManager(layoutManager3);
        if(a.isEmpty() && active){

        }
        ProductAdapter adapter = new ProductAdapter(getContext(),a.get(0));
        ProductAdapter adapter2 = new ProductAdapter(getContext(),a.get(1));
        ProductAdapter adapter3 = new ProductAdapter(getContext(),a.get(2));
        rw1.setAdapter(adapter2);
        rw2.setAdapter(adapter);
        rw3.setAdapter(adapter3);



    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }
    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.geol) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {


                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);


            } else if(!isLocationEnabled(getContext())){
                Toast.makeText(getContext(),"Lütfen telefon ayarlarından konum hizmetini açın.",Toast.LENGTH_SHORT).show();
            } else{
                location();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    location();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), R.string.allowloc,Toast.LENGTH_SHORT).show();
                }
                return;
            }


        }
    }

    public void location(){

        Intent intent = new Intent(getContext(), ShowOnMap.class);
        startActivity(intent);

    }
    public static Boolean isLocationEnabled(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
// This is Deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return  (mode != Settings.Secure.LOCATION_MODE_OFF);

        }
    }
}
