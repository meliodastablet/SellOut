package com.mcakiroglu.sellout.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.databinding.ActivityNewStuffBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.Date;

public class NewStuff extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    BottomNavigationView bnw;
    ActivityNewStuffBinding binding;
    private Uri filePath2,filePath3,filePath4,filePath5;
    private ArrayList<Uri> filePathr = new ArrayList<>();
    private int kk=1;
    Spinner spinner;
    String pid;
    private FirebaseAuth mAuth;
    String spinnerres;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private DatabaseReference mDatabase;
    private DatabaseReference propid;
    boolean flag2 =false;
    int jk = 0;
    String uid;
    private final int PICK_IMAGE_REQUEST = 71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewStuffBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        binding.imageView2.setOnClickListener(this);
        binding.imageView3.setOnClickListener(this);
        binding.imageView4.setOnClickListener(this);
        binding.imageView5.setOnClickListener(this);
        binding.save.setOnClickListener(this);
        binding.address.setOnClickListener(this);

        bnw = (BottomNavigationView) findViewById(R.id.botnav2);
        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.ananas){
                    Intent intent = new Intent(NewStuff.this,MainPage.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId() == R.id.ilans){
                    Intent intent = new Intent(NewStuff.this, MyStuff.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId() == R.id.ilanver){
                    Intent intent = new Intent(NewStuff.this, NewStuff.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId() == R.id.messages){
                    Intent intent = new Intent(NewStuff.this, Messages.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId() == R.id.profile){
                    Intent intent = new Intent(NewStuff.this, Profile.class);
                    startActivity(intent);
                    return true;
                }
                else{
                    return false;
                }

            }
        });

        spinner = binding.spinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_spin, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid=user.getUid();

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
                binding.pname.setError("Bu alan zorunludur.");
                flag = false;
            }
            if (binding.desc.getText().toString().equals("")) {
                binding.desc.setError("Bu alan zorunludur.");
                flag = false;
            }
            if (spinnerres.equals("Kategori Seçiniz")) {
                Toast.makeText(this, "Kategori seçiniz", Toast.LENGTH_SHORT).show();
                flag = false;
            }
            //if resim
            if (filePath2 == null && filePath3 == null && filePath4 == null && filePath5 == null) {
                Toast.makeText(this, "Resim seçiniz", Toast.LENGTH_SHORT).show();
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
                propid = mDatabase.child("usersProducts").child(uid).push();
                pid =propid.getKey();
                mDatabase.child("usersProducts").child(uid).child(pid).child("name").setValue(binding.pname.getText().toString());
                mDatabase.child("usersProducts").child(uid).child(pid).child("comment").setValue(binding.desc.getText().toString());
                mDatabase.child("usersProducts").child(uid).child(pid).child("price").setValue(Double.parseDouble(binding.pricee.getText().toString()));
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formatted = df.format(new Date());
                mDatabase.child("usersProducts").child(uid).child(pid).child("date").setValue(formatted);

                mDatabase.child("usersProducts").child(uid).child(pid).child("category").setValue(spinnerres);
                mDatabase.child("usersProducts").child(uid).child(pid).child("status").setValue("0");
                mDatabase.child("usersProducts").child(uid).child(pid).child("adress").setValue("buraya bakarlar");




                mDatabase.child("categories").child(spinnerres).child(pid).child("adress").setValue("buraya bakarlar");
                mDatabase.child("categories").child(spinnerres).child(pid).child("comment").setValue(binding.desc.getText().toString());
                mDatabase.child("categories").child(spinnerres).child(pid).child("date").setValue(formatted);
                mDatabase.child("categories").child(spinnerres).child(pid).child("name").setValue(binding.pname.getText().toString());
                mDatabase.child("categories").child(spinnerres).child(pid).child("price").setValue(Double.parseDouble(binding.pricee.getText().toString()));
                mDatabase.child("categories").child(spinnerres).child(pid).child("status").setValue("0");




                uploadImage();





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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {


            if(jk == 0){
                filePath2 = data.getData() ;
                Glide.with(getApplicationContext()).load(filePath2.toString()).into(binding.imageView2);
            }else if(jk == 1){
                filePath3 = data.getData() ;
                Glide.with(getApplicationContext()).load(filePath3.toString()).into(binding.imageView3);
            }else if(jk == 2){
                filePath4 = data.getData() ;
                Glide.with(getApplicationContext()).load(filePath4.toString()).into(binding.imageView4);
            }else if(jk == 3){
                filePath5 = data.getData() ;
                Glide.with(getApplicationContext()).load(filePath5.toString()).into(binding.imageView5);
            }


            flag2 = true;
        }
    }

    private void uploadImage()
    {
        if (!filePathr.isEmpty()) {


            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            for(int i=0; i<filePathr.size();i++) {



               final StorageReference ref = storageRef.child("/usersMedia/" + uid + "/" + pid + "/" + i);

                System.out.println("FILEPATH" + filePathr.get(i));
                ref.putFile(filePathr.get(i))
                        .addOnSuccessListener(
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onSuccess(
                                            UploadTask.TaskSnapshot taskSnapshot) {

                                        // Image uploaded successfully


                                        progressDialog.dismiss();
                                        Toast
                                                .makeText(NewStuff.this,
                                                        "Image Uploaded!!",
                                                        Toast.LENGTH_SHORT)
                                                .show();

                                    }
                                })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                // Error, Image not uploaded
                                progressDialog.dismiss();
                                Toast
                                        .makeText(NewStuff.this,
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
                                System.out.println("kkkk" +kk);
                                kk++;
                            }
                        });

                    }
                }
                );


            }
        }

    }


}
