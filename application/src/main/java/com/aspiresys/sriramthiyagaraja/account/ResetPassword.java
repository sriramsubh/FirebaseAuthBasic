package com.aspiresys.sriramthiyagaraja.account;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    private Button resetButton, back;
    private EditText resetEmailEntry;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        resetButton = (Button)findViewById(R.id.btn_reset_password);
        back = (Button)findViewById(R.id.btn_back);
        resetEmailEntry =(EditText)findViewById(R.id.email);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        auth= FirebaseAuth.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reset_email = resetEmailEntry.getText().toString().trim();
                if (TextUtils.isEmpty(reset_email)) {
                    Toast.makeText(ResetPassword.this, "enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setProgress(View.VISIBLE);
                try{
                auth.sendPasswordResetEmail(reset_email).addOnSuccessListener(new OnSuccessListener() {
                    @Override

                    public void onSuccess(Object o) {
                        Toast.makeText(getApplicationContext(), "success" + o, Toast.LENGTH_SHORT).show();

                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "sorry", Toast.LENGTH_SHORT).show();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);

                    }


                });
            }catch(Exception e){e.printStackTrace();}

        }
        });

    }
}
