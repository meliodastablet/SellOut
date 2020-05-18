package com.mcakiroglu.sellout.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.adapter.MyStuffAdapter;
import com.mcakiroglu.sellout.databinding.ActivityMyStuffBinding;
import com.mcakiroglu.sellout.models.Property;

import java.util.ArrayList;

public class MyStuff extends AppCompatActivity {
    RecyclerView recyclerView;
    ActivityMyStuffBinding binding;
    BottomNavigationView bnw;
    public ArrayList<Property> pro =new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    static boolean active = false;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyStuffBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        recyclerView = binding.recyclerView;

        bnw = (BottomNavigationView) findViewById(R.id.botnav3);
        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.ananas){
                    Intent intent = new Intent(MyStuff.this,MainPage.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId() == R.id.ilans){
                    Intent intent = new Intent(MyStuff.this, MyStuff.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId() == R.id.ilanver){
                    Intent intent = new Intent(MyStuff.this, NewStuff.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId() == R.id.messages){
                    Intent intent = new Intent(MyStuff.this, Messages.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId() == R.id.profile){
                    Intent intent = new Intent(MyStuff.this, Profile.class);
                    startActivity(intent);
                    return true;
                }
                else{
                    return false;
                }

            }
        });

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid=user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

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



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(a.isEmpty() && active){
            Toast.makeText(MyStuff.this, R.string.noprop,
                    Toast.LENGTH_SHORT).show();
        }
        MyStuffAdapter adapter = new MyStuffAdapter(this,a);
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
