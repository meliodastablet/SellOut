package com.mcakiroglu.sellout.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.mcakiroglu.sellout.ForgotPassword;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.databinding.ActivityMainBinding;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.button.setOnClickListener(this);
        binding.textView3.setOnClickListener(this);
        binding.textView.setOnClickListener(this);

    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private boolean validateForm(){

        boolean valid = true;
        binding.username.setError(null);
        binding.password.setError(null);

        String email2 = binding.username.getText().toString();
        if (TextUtils.isEmpty(email2)) {
            binding.username.setError("Bu alanı doldurmak zorunludur.");
            valid = false;
        } else if(!isEmailValid(email2)){
            binding.username.setError("Lütfen uygun bir e-mail adresi girin.");
            valid = false;
        }
        else {
            binding.username.setError(null);
        }

        String password2 = binding.password.getText().toString();
        if (TextUtils.isEmpty(password2)) {
            binding.password.setError("Bu alanı doldurmak zorunludur.");
            valid = false;
        } else if (!isPasswordValid(password2)) {
            binding.password.setError("Lütfen en az 8 haneli, harf ve rakamlardan oluşan bir parola girin.");
            valid = false;
        }
        else {
            binding.password.setError(null);
        }

        return valid;
    }

    private boolean isEmailValid(String email) {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        Pattern regex = Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z]).{8,}");

        return regex.matcher(password).find();
    }

    private void signIn(String email, String password) {

        if (!validateForm()) {
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();


                            Toast.makeText(Login.this,
                                    "Giriş başarılı.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, MainPage.class);
                            startActivity(intent);







                        } else {


                            Toast.makeText(Login.this, "Giriş başarısız oldu, yanlış e-posta ya da parola.",
                                    Toast.LENGTH_SHORT).show();

                        }





                    }
                });

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button){
            signIn(binding.username.getText().toString(), binding.password.getText().toString());

        }else if(v.getId() == R.id.textView3){
            Intent intent = new Intent(this, ForgotPassword.class);
            startActivity(intent);

        }else if (v.getId()==R.id.textView){
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        }

    }
}
