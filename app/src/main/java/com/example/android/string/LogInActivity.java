package com.example.android.string;

import android.app.ProgressDialog;
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
     private TextView RegisterLink, forgotPasswordLink;
    private FirebaseAuth mAuth;
    private Button LoginBtn;
    private ProgressDialog loadingBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();


        EmailEditor = findViewById(R.id.EmailEditor);
        PasswordEditor = findViewById(R.id.PasswordEditor);

        RegisterLink = findViewById(R.id.RegisterLink);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);
        LoginBtn = findViewById(R.id.LoginBtn);
        loadingBar = new ProgressDialog(this);


        RegisterLink.setOnClickListener(this);
        forgotPasswordLink.setOnClickListener(this);
        LoginBtn.setOnClickListener(this);
    }

   @Override
    protected void onStart() {

        super.onStart();

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null){

                startActivity(new Intent(LogInActivity.this, MainActivity.class));

                //SendUserToMainActivity();
            }


    }

    private void SendUserToMainActivity() {

        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }



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



        if ( email != null && password != null) {

            loadingBar.setTitle("Logging");
            loadingBar.setMessage("Please wait while your are getting logged in");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                               SendUserToMainActivity();

                                Toast.makeText(LogInActivity.this, "You are logged in successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }

                            else{
                                String message = task.getException().getMessage();
                                Toast.makeText(LogInActivity.this, "Error Occure: " + message,Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                        }
                    });
        }
    }


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




