package com.example.book1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaCodec;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    private EditText Password, Username, Email, Age;
    private Button Register;


    private ProgressBar rgProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Username = (EditText) findViewById(R.id.rgUsername);
        Password = (EditText) findViewById(R.id.rgPassword);
        Email = (EditText) findViewById(R.id.rgEmail);
        Age = (EditText) findViewById(R.id.Age);

        Register = (Button) findViewById(R.id.Registerbtn);
        Register.setOnClickListener(this);

        rgProgressBar = findViewById(R.id.progressBar2);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Registerbtn:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        String age = Age.getText().toString().trim();
        String username = Username.getText().toString().trim();


        if (email.isEmpty()) {
            Email.setError("Email is required");
            Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Please provide valid email");
            Email.requestFocus();
        }
        if (username.isEmpty()) {
            Username.setError("Username is required");
            Username.requestFocus();
            return;
        }

        if (age.isEmpty()) {
            Age.setError("Age is required");
            Age.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            Password.setError("password is required");
            Password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            Password.setError("password should be >= 6");
            Password.requestFocus();
            return;
        }

        rgProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(Email, Username, Age);
                    FirebaseDatabase.getInstance().getReference("User")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())

                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "Registered successfully", Toast.LENGTH_LONG).show();
                                rgProgressBar.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(Register.this, "Register Failed" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                rgProgressBar.setVisibility(View.GONE);

                            }
                        }
                    });

                }else {
                    Toast.makeText(Register.this, "Register Failed" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    rgProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }


}