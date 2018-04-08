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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{


    EditText EmailEditor;
    EditText PasswordEditor;
    TextView RegisterLink, VerifyTextView;
    Button LoginBtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar2;
    Button LearnMoreBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);




        mAuth = FirebaseAuth.getInstance();

        EmailEditor = (EditText) findViewById(R.id.EmailEditor);
        PasswordEditor = (EditText) findViewById(R.id.PasswordEditor);

        RegisterLink = (TextView) findViewById(R.id.RegisterLink);
        LoginBtn = (Button) findViewById(R.id.LoginBtn);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        //VerifyTextView = (TextView) findViewById(R.id.VerifyTextView);
        LearnMoreBtn = findViewById(R.id.LearnMoreBtn);

        RegisterLink.setOnClickListener(this);
        LoginBtn.setOnClickListener(this);
        LearnMoreBtn.setOnClickListener(this);


    }

    private void UserLogin()
    {
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

        FirebaseUser user = mAuth.getCurrentUser();

        progressBar2.setVisibility(View.VISIBLE);

       /* if(userModel.isEmailVerified()){
            VerifyTextView.setText("Email Verified");
        }
        else {
            VerifyTextView.setText("Email Not Verified");
        }*/

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //finish();

                            progressBar2.setVisibility(View.GONE);

                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            Toast.makeText(getApplicationContext(), "UserModel logged in successfully",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{

                            Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.RegisterLink:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.LoginBtn:
                UserLogin();
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.LearnMoreBtn:
                startActivity(new Intent(this, LearnMore.class));
                break;

        }


    }
}




