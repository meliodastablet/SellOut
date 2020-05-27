package com.mcakiroglu.sellout.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.databinding.ActivityRegisterBinding;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private ActivityRegisterBinding binding;
    DatabaseReference ref;
boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.register.setOnClickListener(this);
        binding.textView2.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private boolean validateForm(){

        boolean valid = true;
        binding.kadi.setError(null);
        binding.email.setError(null);
        binding.pass.setError(null);
        binding.pass2.setError(null);


        String email2 = binding.email.getText().toString();
        if (TextUtils.isEmpty(email2)) {
            binding.email.setError("Bu alanı doldurmak zorunludur.");
            valid = false;
        } else if(!isEmailValid(email2)){
            binding.email.setError("Uygun bir e-mail adresi yazın.");
            valid = false;
        }
        else {
            binding.email.setError(null);
        }

        String password2 = binding.pass.getText().toString();
        if (TextUtils.isEmpty(password2)) {
            binding.pass.setError("Bu alanı doldurmak zorunludur.");
            valid = false;
        } else if (!isPasswordValid(password2)) {
            binding.pass.setError("En az 8 haneli rakamlar ve harflerden oluşan bir parola seçin");
            valid = false;
        }
        else {
            binding.pass.setError(null);
        }

        String kadi2 = binding.kadi.getText().toString();
        final String name2 = kadi2;

        ref = FirebaseDatabase.getInstance().getReference().child("usernames");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    if (data.getKey().equals(name2)) {

                        flag = true;
                        binding.kadi.setError("Bu kullanıcı adı çoktan seçilmiş.");

                    } else {


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ;
        if (TextUtils.isEmpty(kadi2)) {
            binding.kadi.setError("Bu alanı doldurmak zorunludur.");
            valid = false;}
        else {

            binding.kadi.setError(null);
        }

        String pass2 = binding.pass2.getText().toString();
        if (TextUtils.isEmpty(pass2)) {
            binding.pass2.setError("Bu alanı doldurmak zorunludur");
            valid = false;
        } else if(!isPasswordMatching(pass2)){
            binding.pass2.setError("Parolalar uyuşmuyor.");
            valid = false;
        }
        else {
            binding.pass2.setError(null);
            flag =false;
        }

        return valid;
    }

    private boolean isEmailValid(String email) {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }



    private boolean isPasswordMatching(String pass2) {
        if(binding.pass.getText().toString().equals(pass2))
            return true;
        else
            return false;
    }

    private boolean isPasswordValid(String password) {
        Pattern regex = Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z]).{8,}");

        return regex.matcher(password).find();
    }

    private void createAccount(final String email, final String password, final String kadi) {



        if (!validateForm()) {
            return;
        }



        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users");
                            DatabaseReference myRef2 = database.getReference("usernames");
                            myRef2.child(kadi).setValue(user.getUid());
                            myRef.child(user.getUid()).child("email").setValue(email);
                            myRef.child(user.getUid()).child("username").setValue(binding.kadi.getText().toString());
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(kadi)
                                    .setPhotoUri(Uri.parse("https://firebasestorage.googleapis.com/v0/b/sellout-bda2b.appspot.com/o/plus.png?alt=media&token=49d992c7-d613-4c47-a63f-30eaecd1247a"))
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Register.this, R.string.succreg,
                                                        Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(),Login.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });



                        } else

                        {

                            Toast.makeText(Register.this, task.toString(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }

                });


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.register){
            createAccount(binding.email.getText().toString(),binding.pass.getText().toString(),binding.kadi.getText().toString());


        }else if(v.getId() == R.id.textView2){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

    }


}
