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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class sign_up extends AppCompatActivity {

    EditText signup_email,signup_password;
    Button signupButton;
    TextView signinTextview;
    private FirebaseAuth mAuth;
    ProgressBar signup_progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Sign Up");


        signup_email = (EditText) findViewById(R.id.signupemailboxID);
        signup_password = (EditText) findViewById(R.id.signuppassboxID);
        signupButton = (Button) findViewById(R.id.signupbuttonID);
        signinTextview = (TextView) findViewById(R.id.signintextviewID);
        signup_progressBar = (ProgressBar) findViewById(R.id.signupprogressID);
        mAuth = FirebaseAuth.getInstance();


        signinTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sign_up.this, signin.class);
                startActivity(intent);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegister();
            }
        });
    }

    private void userRegister() {

        String sign_up_email = signup_email.getText().toString();
        String sign_up_pass = signup_password.getText().toString();

        if(sign_up_email.isEmpty()){
            signup_email.setError("Enter an Email address");
            signup_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(sign_up_email).matches()){
            signup_email.setError("Enter a valid Email Address");
            signup_email.requestFocus();
            return;
        }

        if(sign_up_pass.isEmpty()){
            signup_password.setError("Enter a valid Password");
            signup_password.requestFocus();
            return;
        }
        if(sign_up_pass.length()<6){
            signup_password.setError("Enter More than 6 characters.");
            signup_password.requestFocus();
            return;
        }


        signup_progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(sign_up_email,sign_up_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                signup_progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(sign_up.this,"Registration is Successful!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),signin.class);
                    startActivity(intent);
                }
                else {

                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(sign_up.this,"This email is already used.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(sign_up.this,"Something went wrong.",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });






    }
}
