package com.mcakiroglu.sellout.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.databinding.ActivityDisplayStuffBinding;

import java.util.ArrayList;

public class DisplayStuff extends AppCompatActivity implements View.OnClickListener {
    ActivityDisplayStuffBinding binding;
    int k = 0;
    private DatabaseReference mDatabase;
    ArrayList<String> a = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayStuffBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        Property prop = (Property)intent.getSerializableExtra("prop");

        binding.urunadi.setText("Ürün İsmi: " + prop.getName());
        binding.ilantarihi.setText("İlan Tarihi: " +prop.getDate());
        binding.urunsahibi.setText("Ürün Sahibi: Selam");
        binding.aciklama.setText("Ürün Detayı: " + prop.getComment());
        Glide.with(this).load(prop.getImage1()).into(binding.imageView6);

        try {
            if(!prop.getImage1().equals(""))
                a.add(prop.getImage1());
        }catch (Exception e){

        }
        try {
            if(!prop.getImage2().equals(""))
                a.add(prop.getImage2());
        }catch (Exception e){

        }
        try {
            if(!prop.getImage3().equals(""))
                a.add(prop.getImage3());
        }catch (Exception e){

        } try {
            if(!prop.getImage4().equals(""))
                a.add(prop.getImage4());
        }catch (Exception e){

        }





        binding.nexti.setOnClickListener(this);
        binding.iptal.setOnClickListener(this);
        binding.msend.setOnClickListener(this);
        binding.markassold.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.nexti){
            k++;
            if(k == 1) {
                System.out.println("11111" + k);
                Glide.with(this).load(a.get(k)).into(binding.imageView6);

            }else if(k==2) {
                try{
                    System.out.println("222222" + k);
                    Glide.with(this).load(a.get(k)).into(binding.imageView6);
                }catch (Exception e){
                    k = 0;
                }


            }else if(k==3) {
                try{
                    System.out.println("333333" + k);
                    Glide.with(this).load(a.get(k)).into(binding.imageView6);
                }catch (Exception e){
                    k = 0;
                }

            }else{
                k=0;
                System.out.println("4444444");
                Glide.with(this).load(a.get(1)).into(binding.imageView6);
            }



        }else if(v.getId() == R.id.iptal){
           // mDatabase.child("usersProducts").child(uid).child(pid).child("status").setValue(binding.desc.getText().toString());

        } else if(v.getId() == R.id.msend){
                Intent intent = new Intent(this,Messages.class);
                startActivity(intent);
        }else if(v.getId() == R.id.markassold){
            //mDatabase.child("usersProducts").child(uid).child(pid).child("comment").setValue(binding.desc.getText().toString());
        }

    }
}
