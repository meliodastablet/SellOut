package com.mcakiroglu.sellout.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.activities.MainPage;
import com.mcakiroglu.sellout.activities.MyLocation;
import com.mcakiroglu.sellout.databinding.ActivityNewStuffBinding;
import com.mcakiroglu.sellout.fragments.MyStuff;
import com.mcakiroglu.sellout.models.CityProducts;
import com.mcakiroglu.sellout.models.Property;
import com.mcakiroglu.sellout.models.UserProducts;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class NewStuff extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    BottomNavigationView bnw;
    int LAUNCH_SECOND_ACTIVITY = 1;
    ActivityNewStuffBinding binding;
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=30;
    private Uri filePath2,filePath3,filePath4,filePath5;
    private ArrayList<Uri> filePathr = new ArrayList<>();
    private int kk=1;
    Spinner spinner;
    String pid;
    String result;
    private FirebaseAuth mAuth;
    String spinnerres;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private DatabaseReference mDatabase;
    private DatabaseReference propid;
    boolean flag2 =false;
    int jk = 0;
    String uid,city;
    double lat=99999,lon;
    private final int PICK_IMAGE_REQUEST = 99;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityNewStuffBinding.inflate(getLayoutInflater());
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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        binding.imageView2.setOnClickListener(this);
        binding.imageView3.setOnClickListener(this);
        binding.imageView4.setOnClickListener(this);
        binding.imageView5.setOnClickListener(this);
        binding.save.setOnClickListener(this);
        binding.address.setOnClickListener(this);
        spinner = binding.spinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.array_spin, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid=user.getUid();

    }

    protected void handlers() {

    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.imageView2){
            jk =0;
            chooseImage();

        }else if(v.getId() == R.id.imageView3){
            jk = 1;
            chooseImage();
        }else if(v.getId() == R.id.imageView4){
            jk = 2;
            chooseImage();
        }else if(v.getId() == R.id.imageView5){
            jk =3;
            chooseImage();
        }else if(v.getId() == R.id.save) {
            boolean flag = true;

            if (binding.pname.getText().toString().equals("")) {
                binding.pname.setError("Bu alan zorunludur.");
                flag = false;
            }
            if (binding.pricee.getText().toString().equals("")) {
                binding.pricee.setError("Bu alan zorunludur.");
                flag = false;
            }else{
                try {
                    double d =Double.parseDouble(binding.pricee.getText().toString());
                }catch (NumberFormatException e){
                    binding.pricee.setError("Lütfen fiyatı rakam cinsinden giriniz.");
                    flag = false;
                }

            }
            if (binding.desc.getText().toString().equals("")) {
                binding.desc.setError("Bu alan zorunludur.");
                flag = false;
            }
            if(binding.address2.getText().toString().equals("")){
                binding.address2.setError("Bu alan zorunludur.");
                flag = false;
            }
            if(lat == 99999){
                binding.address2.setError("Lütfen butona tıklayarak adres seçiniz");
                flag = false;
            }
            if (spinnerres.equals("Kategori Seçiniz")) {
                Toast.makeText(getContext(), R.string.ccat, Toast.LENGTH_SHORT).show();
                flag = false;
            }
            //if resim
            if (filePath2 == null && filePath3 == null && filePath4 == null && filePath5 == null) {
                Toast.makeText(getContext(), R.string.cpppp, Toast.LENGTH_SHORT).show();
            } else {
                if (filePath2 != null)
                    filePathr.add(filePath2);
                if (filePath3 != null)
                    filePathr.add(filePath3);
                if (filePath5 != null)
                    filePathr.add(filePath5);
                if (filePath4 != null)
                    filePathr.add(filePath4);

            }

            if (flag && flag2) {

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formatted = df.format(new Date());
                propid = mDatabase.child("usersProducts").child(uid).push();
                pid =propid.getKey();
                UserProducts up = new UserProducts(binding.pname.getText().toString(),binding.desc.getText().toString(),Double.parseDouble(binding.pricee.getText().toString()),formatted,spinnerres,"0",result,lat,lon,city,uid,null,null,null,null);
                mDatabase.child("usersProducts").child(uid).child(pid).setValue(up);




                UserProducts cat = new UserProducts(binding.pname.getText().toString(),binding.desc.getText().toString(),Double.parseDouble(binding.pricee.getText().toString()),formatted,spinnerres,"0",result,lat,lon,city,uid,null,null,null,null);
                mDatabase.child("categories").child(spinnerres).child(pid).setValue(cat);


                CityProducts cp = new CityProducts(city,lat,lon,binding.pname.getText().toString(),Double.parseDouble(binding.pricee.getText().toString()),null,uid,spinnerres);
                mDatabase.child("cityProducts").child(city).child(pid).setValue(cp);










                try{
                    uploadImage();
                }catch (Exception e){

                }finally {
                    Intent i = new Intent(getContext(), MainPage.class);
                    Toast.makeText(getContext(),"İlan yükleme başarılı.",Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }






            }
        }else if(v.getId() == R.id.address){
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {





                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);



            } else {
                location();
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerres = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {


            if(jk == 0){
                filePath2 = data.getData() ;
                Glide.with(getContext()).load(filePath2.toString()).into(binding.imageView2);
            }else if(jk == 1){
                filePath3 = data.getData() ;
                Glide.with(getContext()).load(filePath3.toString()).into(binding.imageView3);
            }else if(jk == 2){
                filePath4 = data.getData() ;
                Glide.with(getContext()).load(filePath4.toString()).into(binding.imageView4);
            }else if(jk == 3){
                filePath5 = data.getData() ;
                Glide.with(getContext()).load(filePath5.toString()).into(binding.imageView5);
            }


            flag2 = true;
        }
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == RESULT_OK){
                result=data.getStringExtra("result");
                city=data.getStringExtra("city");
                binding.address2.setText(result);
                lat = data.getDoubleExtra("lat",0);
                lon = data.getDoubleExtra("lon",0);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //no result
            }
        }
    }

    private void uploadImage()
    {
        if (!filePathr.isEmpty()) {


            final ProgressDialog progressDialog
                    = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            for(int i=0; i<filePathr.size();i++) {



               final StorageReference ref = storageRef.child("/usersMedia/" + uid + "/" + pid + "/" + i);


                ref.putFile(filePathr.get(i))
                        .addOnSuccessListener(
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onSuccess(
                                            UploadTask.TaskSnapshot taskSnapshot) {

                                        // Image uploaded successfully


                                        progressDialog.dismiss();


                                    }
                                })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                // Error, Image not uploaded
                                progressDialog.dismiss();
                                Toast
                                        .makeText(getContext(),
                                                "Failed " + e.getMessage(),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .addOnProgressListener(
                                new OnProgressListener<UploadTask.TaskSnapshot>() {

                                    // Progress Listener for loading
                                    // percentage on the dialog box
                                    @Override
                                    public void onProgress(
                                            UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress
                                                = (100.0
                                                * taskSnapshot.getBytesTransferred()
                                                / taskSnapshot.getTotalByteCount());
                                        progressDialog.setMessage(
                                                "Uploaded "
                                                        + (int) progress + "%");
                                    }
                                });

                ref.putFile(filePathr.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String urli = uri.toString();
                                if(kk == 1) {
                                    mDatabase.child("usersProducts").child(uid).child(pid).child("image1").setValue(urli);
                                    mDatabase.child("categories").child(spinnerres).child(pid).child("image1").setValue(urli);
                                    mDatabase.child("cityProducts").child(city).child(pid).child("image1").setValue(urli);
                                }
                                else if(kk == 2){
                                    mDatabase.child("usersProducts").child(uid).child(pid).child("image2").setValue(urli);
                                    mDatabase.child("categories").child(spinnerres).child(pid).child("image2").setValue(urli);
                                }else if(kk == 3){
                                    mDatabase.child("usersProducts").child(uid).child(pid).child("image3").setValue(urli);
                                    mDatabase.child("categories").child(spinnerres).child(pid).child("image3").setValue(urli);
                                }else if(kk == 4){
                                    mDatabase.child("usersProducts").child(uid).child(pid).child("image4").setValue(urli);
                                    mDatabase.child("categories").child(spinnerres).child(pid).child("image4").setValue(urli);
                                }

                                kk++;
                            }
                        });

                    }
                }
                );


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

        Intent intent = new Intent(getContext(), MyLocation.class);
        startActivityForResult(intent,LAUNCH_SECOND_ACTIVITY);

    }



}
