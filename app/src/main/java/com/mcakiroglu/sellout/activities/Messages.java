package com.mcakiroglu.sellout.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mcakiroglu.sellout.R;

import java.sql.SQLOutput;
import java.util.ArrayList;

import static com.mcakiroglu.sellout.R.layout.activity_messages;

public class Messages extends AppCompatActivity {
    BottomNavigationView bnw;
    DatabaseReference ref,ref2;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    ArrayList<String> ids = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();


    ListView uList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_messages);


        uList = findViewById(R.id.listw);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("messages");
        ref2 = database.getReference("users");
        bnw = (BottomNavigationView) findViewById(R.id.botnav4);
        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.ananas){
                    Intent intent = new Intent(Messages.this,MainPage.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId() == R.id.ilans){
                    Intent intent = new Intent(Messages.this, MyStuff.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId() == R.id.ilanver){
                    Intent intent = new Intent(Messages.this, NewStuff.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId() == R.id.messages){
                    Intent intent = new Intent(Messages.this, Messages.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId() == R.id.profile){
                    Intent intent = new Intent(Messages.this, Profile.class);
                    startActivity(intent);
                    return true;
                }
                else{
                    return false;
                }

            }
        });

ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        System.out.println(dataSnapshot.toString());
        Iterable<DataSnapshot> iterable =dataSnapshot.getChildren();
        for(DataSnapshot snap :iterable) {



            ids.add(snap.getKey());
            ref2.child(snap.getKey()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    names.add(dataSnapshot.child("username").getValue().toString());
                    uList.invalidateViews();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }


        System.out.println("FC" + ids.toString());

        ArrayAdapter adapter;
        adapter= new ArrayAdapter<String>(Messages.this,R.layout.simple_list_item_1,names);

        uList.setAdapter(adapter);
        uList.setVisibility(View.VISIBLE);


    }
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }








});

uList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(Messages.this,Messaging.class);
        intent.putExtra("toid",ids.get(position));
        startActivity(intent);
    }
});



    }

    public void onBackPressed(){
        startActivity(new Intent(Messages.this,MainPage.class));
    }

}
