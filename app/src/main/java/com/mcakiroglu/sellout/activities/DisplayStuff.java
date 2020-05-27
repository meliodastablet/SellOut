package com.mcakiroglu.sellout.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.databinding.ActivityDisplayStuffBinding;
import com.mcakiroglu.sellout.models.Property;

import java.util.ArrayList;

public class DisplayStuff extends AppCompatActivity implements View.OnClickListener {
    ActivityDisplayStuffBinding binding;
    Property prop;
    int k =1;
    boolean flag = false;
    private DatabaseReference mDatabase;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    ArrayList<String> a = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayStuffBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        prop = (Property)intent.getSerializableExtra("prop");

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


        if(user.getUid().equals(prop.getToID()))
            binding.msend.setVisibility(View.INVISIBLE);



        binding.nexti.setOnClickListener(this);
        binding.iptal.setOnClickListener(this);
        binding.msend.setOnClickListener(this);
        binding.markassold.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.nexti) {

            if(a.size() == 1) {
                Glide.with(this).load(a.get(0)).into(binding.imageView6);
            }else if(a.size() == 2){
                if(flag){
                    Glide.with(this).load(a.get(0)).into(binding.imageView6);
                    flag = !flag;
                }else{
                    Glide.with(this).load(a.get(1)).into(binding.imageView6);
                    flag = !flag;
                }
            }else if(a.size() == 3){
                if(k==1){
                    Glide.with(this).load(a.get(k)).into(binding.imageView6);
                    k++;
                }else if(k==2){
                    Glide.with(this).load(a.get(k)).into(binding.imageView6);
                    k=0;
                }else if(k==0){
                    Glide.with(this).load(a.get(k)).into(binding.imageView6);
                    k++;
                }
            }else{
                if(k==1){
                    Glide.with(this).load(a.get(k)).into(binding.imageView6);
                    k++;
                }else if(k==2){
                    Glide.with(this).load(a.get(k)).into(binding.imageView6);
                    k++;
                }else if(k==0){
                    Glide.with(this).load(a.get(k)).into(binding.imageView6);
                    k++;
                }else if(k==3){
                    Glide.with(this).load(a.get(k)).into(binding.imageView6);
                    k=0;
                }
            }






        }else if(v.getId() == R.id.iptal){
          // mDatabase.child("usersProducts").child(user.getUid()).child(pid).child("status").setValue("1");

        } else if(v.getId() == R.id.msend){
                Intent intent = new Intent(this,Messaging.class);
                intent.putExtra("toid",prop.getToID());
                startActivity(intent);
        }else if(v.getId() == R.id.markassold){
            //mDatabase.child("usersProducts").child(uid).child(pid).child("status").setValue("2");
        }

    }
}
