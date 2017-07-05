package com.aspiresys.sriramthiyagaraja.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    private Button changeButton, back;
    private EditText resetEmailEntry,oldPassword,newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private final String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        changeButton = (Button)findViewById(R.id.btn_change_password);
        back = (Button)findViewById(R.id.btn_back);
        resetEmailEntry =(EditText)findViewById(R.id.email);
        oldPassword = (EditText)findViewById(R.id.old_password);
        newPassword = (EditText)findViewById(R.id.new_password);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        auth= FirebaseAuth.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reset_email = resetEmailEntry.getText().toString().trim();
                String old_password = oldPassword.getText().toString().trim();
                final String new_password = newPassword.getText().toString().trim();
                if (TextUtils.isEmpty(reset_email)) {
                    Toast.makeText(ChangePasswordActivity.this, "enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(old_password)) {
                    Toast.makeText(ChangePasswordActivity.this, "enter your old_password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(new_password)) {
                    Toast.makeText(ChangePasswordActivity.this, "enter your new_password", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setProgress(View.VISIBLE);
                try{
                   final  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(reset_email,old_password);

// Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(new_password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "Password updated");
                                                    Toast.makeText(ChangePasswordActivity.this,"your password has been updated",Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(ChangePasswordActivity.this,MainActivity.class));
                                                } else {
                                                    Log.d(TAG, "Error password not updated");
                                                Toast.makeText(ChangePasswordActivity.this,"Change password failed",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Log.d(TAG, "Error auth failed");
                                    }
                                }
                            });
                }catch(Exception e){e.printStackTrace();}

            }
        });

    }
}
