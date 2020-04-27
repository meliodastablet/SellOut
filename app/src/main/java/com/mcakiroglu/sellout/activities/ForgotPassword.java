package com.mcakiroglu.sellout.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.databinding.DialogEmailBinding;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    DialogEmailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogEmailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.button2.setOnClickListener(this);


    }

    private void resetPassword(String email){

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(!isEmailValid(binding.forpass.getText().toString())) {
            binding.forpass.setError("Lütfen uygun bir mail adresi giriniz.");
        }
        else {

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(ForgotPassword.this, "Parola sıfırlama email'i gönderildi",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgotPassword.this, "Bilinmeyen hata",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button2)
            resetPassword(binding.forpass.getText().toString());

    }
    private boolean isEmailValid(String email) {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
