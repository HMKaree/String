package com.example.android.string;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText EmailEditor;
    EditText PasswordEditor;
    Button RegisterBtn;
    //TextView LoginLink;
    FirebaseAuth mAuth;
    ProgressBar progressBar2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EmailEditor = findViewById(R.id.EmailEditor);
        PasswordEditor = findViewById(R.id.PasswordEditor);

        //TextView LoginLink = (TextView) findViewById(R.id.LoginLink);
        RegisterBtn =  findViewById(R.id.RegisterBtn);
        progressBar2 = findViewById(R.id.progressBar2);

        RegisterBtn.setOnClickListener(this);
        //LoginLink.setOnClickListener(this);

        //Firebase database access
        mAuth = FirebaseAuth.getInstance();
    }
    //validating email and password
    private void RegisterUser(){
        String email= EmailEditor.getText().toString().trim();
        String password= PasswordEditor.getText().toString().trim();

        if (email.isEmpty()){
            EmailEditor.setError("Email is required");
            EmailEditor.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EmailEditor.setError("Enter valid email address");
            EmailEditor.requestFocus();
            return;
        }

        if(password.isEmpty()){
            PasswordEditor.setError("Password is required");
            PasswordEditor.requestFocus();
            return;
        }

        if(password.length()<6){
            PasswordEditor.setError("Minimum length of password is 6");
            PasswordEditor.requestFocus();
            return;
        }

        progressBar2.setVisibility(View.VISIBLE);


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            progressBar2.setVisibility(View.GONE);
                            startActivity(new Intent(RegisterActivity.this, UserProfileActivity.class));
                            /*Toast.makeText(getApplicationContext(), "UserModel registered successfully",
                                    Toast.LENGTH_SHORT).show();*/
                        }
                        else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT)
                                        .show();
                            }

                            else {
                                Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });



        }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.RegisterBtn:
                RegisterUser();
                break;
        }

    }
}



