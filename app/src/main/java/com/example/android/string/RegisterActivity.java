package com.example.android.string;

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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    EditText EmailEditor;
    EditText PasswordEditor;
    Button RegisterBtn;
    TextView LoginLink;

    FirebaseAuth mAuth;
    FirebaseDatabase mdatabase;
    DatabaseReference StringDatabase;
    ProgressBar progressBar2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EmailEditor = findViewById(R.id.EmailEditor);
        PasswordEditor = findViewById(R.id.PasswordEditor);

        LoginLink = findViewById(R.id.LoginLink);
        RegisterBtn = findViewById(R.id.RegisterBtn);
        progressBar2 = findViewById(R.id.progressBar2);



        RegisterBtn.setOnClickListener(this);
        LoginLink.setOnClickListener(this);



        //Firebase database access
        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        StringDatabase = mdatabase.getReference().child("UserProfiles");


    }




    //validating email and password
    private void RegisterUser() {
        final String email = EmailEditor.getText().toString().trim();
        final String password = PasswordEditor.getText().toString().trim();


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


        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final String user_id = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = StringDatabase.child(user_id);

                                current_user_db.child("UserEmail").setValue(email);

                                Intent loginIntent = new Intent(RegisterActivity.this, UserProfileActivity.class);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(loginIntent);

                            }
                        }

                    });
        }
    }













    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.RegisterBtn:
                RegisterUser();
                break;

            case R.id.LoginLink:
                startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
                break;

        }

    }
}



