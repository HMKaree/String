package com.example.android.string;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{


     private EditText EmailEditor;
     private EditText PasswordEditor;
     private TextView RegisterLink, forgotPasswordLink; //VerifyTextView;
    private FirebaseAuth mAuth;
    private Button LoginBtn;
     //private FirebaseUser user_id;
     //private FirebaseDatabase mdatabase;
    // private DatabaseReference StringDatabase;
     //FirebaseAuth.AuthStateListener mAuthListener;
     //FirebaseUser user = mAuth.getInstance().getCurrentUser();
     //ProgressBar progressBar2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        EmailEditor = findViewById(R.id.EmailEditor);
        PasswordEditor = findViewById(R.id.PasswordEditor);

        RegisterLink = findViewById(R.id.RegisterLink);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);
        LoginBtn = findViewById(R.id.LoginBtn);
        //progressBar2 =  findViewById(R.id.progressBar2);
        //VerifyTextView =  findViewById(R.id.VerifyTextView);


        RegisterLink.setOnClickListener(this);
        forgotPasswordLink.setOnClickListener(this);
        LoginBtn.setOnClickListener(this);
    }


        //mdatabase = FirebaseDatabase.getInstance();
        //StringDatabase = mdatabase.getReference().child("UserProfiles");


       /*mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
              if (firebaseAuth.getCurrentUser() == null){
                  Intent loginIntent = new Intent(LogInActivity.this, RegisterActivity.class);
                  loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                  startActivity(loginIntent);
                  finish();
              }

                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(LogInActivity.this, MainActivity.class));
                    finish();
                }
            }
        };

    }*/

  /* @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }*/

    private void UserLogin() {
        String email = EmailEditor.getText().toString().trim();
        String password = PasswordEditor.getText().toString().trim();

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


     /*   //progressBar2.setVisibility(View.VISIBLE);
        if ( email != null && password != null) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                        }
                    });
        }*/
    }

    /*public void checkUserExists(){

        final FirebaseUser user_id = mAuth.getCurrentUser();
        StringDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)) {


                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    //finish();

                } else {

                    Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/









    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.RegisterLink:
                sendUserToRegister();
                break;

            case R.id.forgotPasswordLink:
                startActivity(new Intent(this, ForgotPassword.class));
                break;

            case R.id.LoginBtn:
                UserLogin();
                //startActivity(new Intent(this, MainActivity.class));
                break;



        }


    }

    private void sendUserToRegister() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);

    }
}




