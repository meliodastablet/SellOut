package com.mcakiroglu.sellout.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.adapter.ProductAdapter;
import com.mcakiroglu.sellout.databinding.ActivityMyStuffBinding;
import com.mcakiroglu.sellout.models.Property;

import java.util.ArrayList;

public class MyStuff extends Fragment {
    RecyclerView recyclerView;
    ActivityMyStuffBinding binding;
    BottomNavigationView bnw;
    public ArrayList<Property> pro =new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    static boolean active = false;
    String uid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityMyStuffBinding.inflate(getLayoutInflater());
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
        recyclerView = binding.recyclerView;


        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid=user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    protected void handlers() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot prop = dataSnapshot.child("usersProducts").child(uid);
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
            Toast.makeText(getContext(), R.string.noprop,
                    Toast.LENGTH_SHORT).show();
        }
        ProductAdapter adapter = new ProductAdapter(getContext(),a);
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
}
