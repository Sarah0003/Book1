package com.example.book1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register;
    private Button login;
    private EditText lgPassword, lgEmail;
    private FirebaseAuth mAuth;

    private ProgressBar lgProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lgPassword = (EditText) findViewById(R.id.Password);
        lgEmail = (EditText) findViewById(R.id.Email);
        register=(TextView)findViewById(R.id.Register_txt);
        register.setOnClickListener(this);
        login=(Button) findViewById(R.id.Login);
        login.setOnClickListener(this);
        lgProgressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View v){
        switch (v.getId())
        {
            case R.id.Register_txt:
                startActivity(new Intent(this,Register.class));
                break;


            case R.id.Login:
                userLogin();
                break;
        }

    }

    private void userLogin() {
        String email = lgEmail.getText().toString().trim();
        String password = lgPassword.getText().toString().trim();

        if (email.isEmpty()) {
            lgEmail.setError("Email is required");
            lgEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            lgEmail.setError("Please provide valid email");
            lgEmail.requestFocus();
        }

        if (password.isEmpty()) {
            lgPassword.setError("password is required");
            lgPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            lgPassword.setError("password should be >= 6");
            lgPassword.requestFocus();
            return;
        }

        lgProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(MainActivity.this,ProfileActivity.class));


                }else {
                    Toast.makeText(MainActivity.this, "Login Failed!Please check your credentials " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    lgProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }





}