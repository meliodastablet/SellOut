package com.mcakiroglu.sellout.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.databinding.NavHeaderMainPageBinding;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

public class MainPage extends AppCompatActivity{
    NavHeaderMainPageBinding binding;

    private AppBarConfiguration mAppBarConfiguration;
    BottomNavigationView bnw;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bnw = (BottomNavigationView) findViewById(R.id.botnav);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.ananas){
                    Intent intent = new Intent(MainPage.this,MainPage.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId() == R.id.ilans){
                    Intent intent = new Intent(MainPage.this, MyStuff.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId() == R.id.ilanver){
                    Intent intent = new Intent(MainPage.this, NewStuff.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId() == R.id.messages){
                    Intent intent = new Intent(MainPage.this, Messages.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId() == R.id.profile){
                    Intent intent = new Intent(MainPage.this, Profile.class);
                    startActivity(intent);
                    return true;
                }
                else{
                    return false;
                }

            }
        });

        //binding.textView.setText("Kullanıcı adı");

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




}
