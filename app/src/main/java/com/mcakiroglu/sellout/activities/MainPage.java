package com.mcakiroglu.sellout.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mcakiroglu.sellout.R;

import com.mcakiroglu.sellout.databinding.ActivityMainPageBinding;
import com.mcakiroglu.sellout.fragments.ArabaFragment;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



public class MainPage extends AppCompatActivity{


    ActivityMainPageBinding mainPageBinding;
    private AppBarConfiguration mAppBarConfiguration;
    BottomNavigationView bnw;
    private FirebaseAuth mAuth;
    DatabaseReference ref;
    int unread=0;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainPageBinding = ActivityMainPageBinding.inflate(getLayoutInflater());
        View view = mainPageBinding.getRoot();
        setContentView(view);

        NavigationView nw = findViewById(R.id.nav_view);
        View head = nw.getHeaderView(0);

        TextView tw = head.findViewById(R.id.mailx);
        TextView tw2 = head.findViewById(R.id.nickx);
        ImageView iw = head.findViewById(R.id.imageViewx);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("messages");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> userids = dataSnapshot.getChildren();
                for(DataSnapshot snap1 :userids) {
                    final DataSnapshot snap11 = snap1;
                    ref.child(user.getUid()).child(snap1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> messageids = dataSnapshot.getChildren();
                            for(DataSnapshot snap2:messageids){
                                ref.child(user.getUid()).child(snap11.getKey()).child(snap2.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if(!(dataSnapshot.child("fromID").getValue().toString().equals(user.getUid()))&&(dataSnapshot.child("read").getValue().toString().equals("0")))
                                            unread++;
                                        bnw.getOrCreateBadge(R.id.messages).setNumber(unread);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });




        tw2.setText(user.getDisplayName());
        tw.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).into(iw);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);




        bnw = (BottomNavigationView) findViewById(R.id.botnav);
        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.ananas){
                    navController.navigate(R.id.nav_home);
                    return true;
                }else if(item.getItemId() == R.id.ilans){

                   navController.navigate(R.id.nav_ilan);
                   return true;
                }else if(item.getItemId() == R.id.ilanver){
                    navController.navigate(R.id.nav_yeni);
                    return true;
                }else if(item.getItemId() == R.id.messages){

                    try{
                        if(bnw.getBadge(R.id.messages).hasNumber()){
                            bnw.removeBadge(R.id.messages);
                        }
                    }catch (Exception e){

                    }

                    navController.navigate(R.id.nav_mesaj);
                    return true;
                }else if(item.getItemId() == R.id.profile){
                    navController.navigate(R.id.nav_profil);
                    return true;
                }
                else{
                    return false;
                }

            }
        });
        //



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                FirebaseMessaging.getInstance().unsubscribeFromTopic(auth.getUid());
                auth.signOut();
                Toast.makeText(this, R.string.succext,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,Login.class);
                startActivity(intent);
                return true;
        }
        return false;
    }




}
