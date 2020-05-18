package com.mcakiroglu.sellout.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.mcakiroglu.sellout.adapter.MyStuffAdapter;
import com.mcakiroglu.sellout.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {


    RecyclerView recyclerView;
    FragmentHomeBinding binding;
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=30;

    String uid;
    public ArrayList<Property> pro =new ArrayList<>();
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
        recyclerView = binding.recyclerView6;
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid=user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }


    protected void handlers() {



        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot prop = dataSnapshot.child("categories").child("Cep Telefonu");
                Iterable<DataSnapshot> propdetails =prop.getChildren();
                for(DataSnapshot snap :propdetails){

                    Property p =snap.getValue(Property.class);
                    pro.add(p);
                }

                custom(pro);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    public void custom(ArrayList<Property> a){



        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        if(a.isEmpty() && active){

        }
        MyStuffAdapter adapter = new MyStuffAdapter(getContext(),a);
        recyclerView.setAdapter(adapter);



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
        if(v.getId() == R.id.geol){
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {





                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);



            } else {
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
}
