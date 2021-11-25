package com.example.alertschedule;

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

public class signin extends AppCompatActivity {

    EditText signin_email,signin_password;
    Button signinButton;
    TextView signupTextview;
    ProgressBar signin_progressBar;
    private FirebaseAuth mAuth;
    String sign_in_email;
    String sign_in_pass ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        this.setTitle("Sign In");



        signin_email = (EditText) findViewById(R.id.signinemailboxID);
        signin_password = (EditText) findViewById(R.id.signinpassboxID);
        signinButton = (Button) findViewById(R.id.signinbuttonID);
        signupTextview = (TextView) findViewById(R.id.signuptextviewID);
        signin_progressBar = (ProgressBar) findViewById(R.id.signinprogressID);
        mAuth = FirebaseAuth.getInstance();

        signupTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signin.this, sign_up.class);
                startActivity(intent);

            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();

            }
        });

    }

    private void userLogin() {

        sign_in_email = signin_email.getText().toString();
        sign_in_pass = signin_password.getText().toString();

        if(sign_in_email.isEmpty()){
            signin_email.setError("Enter an Email address");
            signin_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(sign_in_email).matches()){
            signin_email.setError("Enter a valid Email Address");
            signin_email.requestFocus();
            return;
        }

        if(sign_in_pass.isEmpty()){
            signin_password.setError("Enter a valid Password");
            signin_password.requestFocus();
            return;
        }
        if(sign_in_pass.length()<6){
            signin_password.setError("Enter More than 6 characters.");
            signin_password.requestFocus();
            return;
        }

        signin_progressBar.setVisibility(View.VISIBLE);


        mAuth.signInWithEmailAndPassword(sign_in_email,sign_in_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                signin_progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Sign In Successful!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(signin.this,store_and_retrieve.class);
                    intent.putExtra("email", sign_in_email);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Something went wrong.",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
