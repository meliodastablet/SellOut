package com.mcakiroglu.sellout.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.models.Property;
import com.mcakiroglu.sellout.adapter.ProductAdapter;
import com.mcakiroglu.sellout.databinding.FragmentArabaBinding;

import java.util.ArrayList;
import java.util.Collections;


public class ArabaFragment extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    FragmentArabaBinding binding;
    String uid;
    public ArrayList<Property> pro =new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    static boolean active = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArabaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        binding.button3.setOnClickListener(this);

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
        recyclerView = binding.recyclerView4;
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid=user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();


    }


    protected void handlers() {
        Query q = mDatabase.child("categories").child("Araba").orderByChild("price");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> propdetails =dataSnapshot.getChildren();
                for(DataSnapshot snap :propdetails){

                    Property p =snap.getValue(Property.class);
                    p.setCategory("Araba");
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
            Toast.makeText(getContext(), R.string.notfound, Toast.LENGTH_SHORT).show();
        }

        Collections.reverse(a);
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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button3){

            custom(pro);
        }
    }
}
