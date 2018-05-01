package com.example.android.string;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CHOOSE_IMAGE = 101;
    private CircleImageView UserImage;
    private EditText UserNameField, UserAgeField, UserJobField, UserLocationField;
    private Button SaveProfileBtn;
    private Uri uri = null;

    FirebaseDatabase mDatabase;
    DatabaseReference StringDatabase;
    FirebaseAuth mAuth;
    FirebaseStorage mStorage;
    StorageReference StringStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        StringDatabase = mDatabase.getReference().child("UserProfiles");
        mStorage = FirebaseStorage.getInstance();
        StringStorage = mStorage.getReference().child("ProfileImages");

        UserImage = findViewById(R.id.UserImage);
        UserNameField = findViewById(R.id.UserNameField);
        UserAgeField = findViewById(R.id.UserAgeField);
        UserJobField = findViewById(R.id.UserJobField);
        UserLocationField = findViewById(R.id.UserLocationField);

        //progressbar = findViewById(R.id.progressbar);
        SaveProfileBtn = findViewById(R.id.SaveProfileBtn);

        UserImage.setOnClickListener(this);
        SaveProfileBtn.setOnClickListener(this);

    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            UserImage.setImageURI(uri);
        }
    }


    private void saveUserInformation() {


        final String displayUserName = UserNameField.getText().toString().trim();
        final String displayUserAge = UserAgeField.getText().toString().trim();
        final String displayUserLocation = UserLocationField.getText().toString().trim();
        final String displayUserJob = UserJobField.getText().toString().trim();


        if (displayUserName.isEmpty()) {
            UserNameField.setError("User Name is required");
            UserNameField.requestFocus();
            return;
        }
        if (displayUserAge.isEmpty()) {
            UserAgeField.setError("Age is required");
            UserAgeField.requestFocus();
            return;
        }

        if (displayUserJob.isEmpty()) {
            UserJobField.setError("Job is required");
            UserJobField.requestFocus();
            return;
        }

        if (displayUserLocation.isEmpty()) {
            UserLocationField.setError("Location is required");
            UserLocationField.requestFocus();
            return;
        }

        final String user_id = mAuth.getCurrentUser().getUid();
        // progressBar2.setVisibility(View.VISIBLE);


        if (!TextUtils.isEmpty(displayUserName) && !TextUtils.isEmpty(displayUserAge)
                && !TextUtils.isEmpty(displayUserJob) && !TextUtils.isEmpty(displayUserLocation) && uri != null) {

            StorageReference filepath = StringStorage.child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                    DatabaseReference current_user_db = StringDatabase.child(user_id);
                    current_user_db.child("UserName").setValue(displayUserName);
                    current_user_db.child("userAge").setValue(displayUserAge);
                    current_user_db.child("userJob").setValue(displayUserJob);
                    current_user_db.child("userLocation").setValue(displayUserLocation);
                    current_user_db.child("userImage").setValue(downloadUrl);

                    Toast.makeText(getApplicationContext(),"Your Profile has been saved", Toast.LENGTH_SHORT).show();

                    Intent loginIntent = new Intent(UserProfileActivity.this, LogInActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            });
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.UserImage:
                showImageChooser();
                break;

            case R.id.SaveProfileBtn:
                saveUserInformation();
                startActivity(new Intent(UserProfileActivity.this, LogInActivity.class));

        }
    }
}










