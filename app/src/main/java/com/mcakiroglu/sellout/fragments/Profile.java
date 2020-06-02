package com.mcakiroglu.sellout.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.activities.Login;
import com.mcakiroglu.sellout.databinding.ActivityMyStuffBinding;
import com.mcakiroglu.sellout.databinding.ActivityProfileBinding;
import com.mcakiroglu.sellout.fragments.Messages;
import com.mcakiroglu.sellout.fragments.MyStuff;
import com.mcakiroglu.sellout.fragments.NewStuff;
import com.mcakiroglu.sellout.models.Property;

import java.util.Random;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment implements View.OnClickListener{
    ActivityProfileBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private DatabaseReference mDatabase;
    Uri uri;
    BottomNavigationView bnw;



    String m_Text="";
    AlertDialog.Builder alertDialog;

    private final int PICK_IMAGE_REQUEST = 71;
    int a = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
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
        binding.cadres.setOnClickListener(this);
        binding.cname.setOnClickListener(this);
        binding.cpass.setOnClickListener(this);
        binding.imageView.setOnClickListener(this);
        binding.vemail.setOnClickListener(this);
        binding.logout.setOnClickListener(this);
        binding.nick.setText(user.getDisplayName());
        binding.emaill.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).into(binding.imageView);

        alertDialog = new AlertDialog.Builder(getContext());

        alertDialog.setView(R.layout.alert);




        alertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog f = (Dialog) dialog;
                EditText input;
                input = f.findViewById(R.id.input);
                m_Text = input.getText().toString();


                dialog.dismiss();

                custom(a);
            }
        });

    }

    protected void handlers() {

    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.cadres){
            a = 1;
            alertDialog.setTitle("Eposta adresi giriniz");


            alertDialog.show();


        }else if(v.getId() == R.id.cname){
            a=2;
            alertDialog.setTitle("Yeni kullanıcı adınızı giriniz");


            alertDialog.show();





        }
        else if(v.getId() == R.id.cpass){
            a=3;
            alertDialog.setTitle("Yeni parolanızı giriniz");


            alertDialog.show();



        }
        else if(v.getId() == R.id.vemail){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), R.string.vmailsent,Toast.LENGTH_SHORT).show();
                    }
                }

            });
            }


        else if(v.getId() == R.id.imageView){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Resim Seçin"), PICK_IMAGE_REQUEST);







        }else if(v.getId() == R.id.logout){
            FirebaseMessaging.getInstance().unsubscribeFromTopic(auth.getUid());
            auth.signOut();
            Toast.makeText(getContext(), R.string.succext,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            Glide.with(this).load(data.getData().toString()).into(binding.imageView);
            uri = data.getData();
            uploadImage();
        }

    }


    private void custom(int b) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("users");
        switch (b) {
            case 1:
                if(m_Text.isEmpty()){
                    Toast.makeText(getContext(),"Uygun olmayan mail adresi, işlem iptal edildi.",Toast.LENGTH_SHORT).show();

                }else{
                    user.updateEmail(m_Text).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), R.string.cepad, Toast.LENGTH_SHORT).show();
                                mDatabase.child(user.getUid()).child("email").setValue(m_Text);

                            }
                        }
                    });
                }

                break;
            case 2:
                final String mt=user.getDisplayName();
                if(m_Text.isEmpty()){
                    Toast.makeText(getContext(),"Uygun olmayan kullanıcı adı, işlem iptal edildi.",Toast.LENGTH_SHORT).show();

                }else {
                    UserProfileChangeRequest profile2 = new UserProfileChangeRequest.Builder().setDisplayName(m_Text).build();
                    user.updateProfile(profile2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), R.string.cnn, Toast.LENGTH_SHORT).show();


                                mDatabase.child(user.getUid()).child("username").setValue(m_Text);
                                database.getReference("usernames").child(mt).removeValue();
                                database.getReference("usernames").child(m_Text).setValue(user.getUid());

                            }
                        }
                    });
                }
                break;
            case 3:
                if(m_Text.isEmpty()){
                    Toast.makeText(getContext(),"En az 8 haneli rakamlar ve harflerden oluşan bir parola seçin",Toast.LENGTH_SHORT).show();

                }else if(!isPasswordValid(m_Text)){
                    Toast.makeText(getContext(),"En az 8 haneli rakamlar ve harflerden oluşan bir parola seçin",Toast.LENGTH_SHORT).show();

                }

                else {
                    user.updatePassword(m_Text).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(getContext(), R.string.cpasss, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;

        }

    }

    private void uploadImage(){

        Random rand = new Random();
        final StorageReference ref = storageRef.child("/profilePictures/" + rand.nextInt(50000));
            ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast
                            .makeText(getContext(),
                                    "Image Uploaded!!",
                                    Toast.LENGTH_SHORT)
                            .show();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
                            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), R.string.cpp,Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }
                    });
                }

            });


    }

    private boolean isPasswordValid(String password) {
        Pattern regex = Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z]).{8,}");

        return regex.matcher(password).find();
    }


}
