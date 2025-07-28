package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity  {

    EditText  emailEditText, passwordEditText;
    Button LoginButton;
    FirebaseAuth mAuth;
    ProgressBar progressbar;
    TextView tv;
    FirebaseUser currentUser = null;

    @Override
    public void onStart() {
        super.onStart();
        // Initialize Firebase Auth

        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
       emailEditText = findViewById(R.id.email);
       passwordEditText = findViewById(R.id.password);
       LoginButton = findViewById(R.id.loginButton);
       progressbar = findViewById(R.id.SHOW_PROGRESS);
       tv = findViewById(R.id.TV);
        tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });
    LoginButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view){
            progressbar.setVisibility(View.VISIBLE);
            mAuth = FirebaseAuth.getInstance();

            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

if(TextUtils.isEmpty(email)){
    Toast.makeText(Login.this, "enter email", Toast.LENGTH_SHORT).show();
    return;}
if(TextUtils.isEmpty(password)){
                Toast.makeText(Login.this, "enter password", Toast.LENGTH_SHORT).show();
                return;}

           mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressbar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(getApplicationContext(),"login successful",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(Login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        }
    });

    }
}