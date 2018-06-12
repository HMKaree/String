package com.example.android.string;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {


    private EditText EmailEditor;
    private EditText PasswordEditor, ConfirmPasswordEditor;
    private Button RegisterBtn;
    private TextView LoginLink;
    private Button LearnMoreBtn, UserGuide;
    private ProgressDialog loadingBar;




    private FirebaseAuth mAuth;
    FirebaseDatabase mdatabase;
    DatabaseReference StringDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Firebase database access
        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        StringDatabase = mdatabase.getReference().child("Users");

        LearnMoreBtn = findViewById(R.id.LearnMoreBtn);
        UserGuide = findViewById(R.id.UserGuide);

        EmailEditor = findViewById(R.id.EmailEditor);
        PasswordEditor = findViewById(R.id.PasswordEditor);
        ConfirmPasswordEditor = findViewById(R.id.ConfirmPasswordEditor);

        LoginLink = findViewById(R.id.LoginLink);
        RegisterBtn = findViewById(R.id.RegisterBtn);
        loadingBar = new ProgressDialog(this);


        LearnMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LearnMore.class));
            }
        });
        UserGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, UserGuide.class));
            }
        });
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });
        LoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
            }
        });






    }

   /* @Override
    protected void onStart() {

        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){

            SendUserToMainActivity();
        }


    }*/

    private void SendUserToMainActivity() {

        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }




    //validating email and password
    private void RegisterUser() {
        final String email = EmailEditor.getText().toString().trim();
        final String password = PasswordEditor.getText().toString().trim();
        final String confirmPassword = ConfirmPasswordEditor.getText().toString().trim();

        if (email.isEmpty()) {
            EmailEditor.setError("Email is required");
            EmailEditor.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EmailEditor.setError("Enter valid email address");
            EmailEditor.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            PasswordEditor.setError("Password is required");
            PasswordEditor.requestFocus();
            return;
        }

        if (password.length() < 6) {
            PasswordEditor.setError("Minimum length of password is 6");
            PasswordEditor.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)){
            ConfirmPasswordEditor.setError("Passwords do not match");
            ConfirmPasswordEditor.requestFocus();
            return;
        }


        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait while your account is being created");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                SendUserToSetup();

                                Toast.makeText(RegisterActivity.this, "You have been authenticated", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();



                            }
                            else{
                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                        }

                    });
        }
    }

    private void SendUserToSetup(){
        Intent loginIntent = new Intent(RegisterActivity.this, UserProfileActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }






}



