package com.example.android.string;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CHOOSE_IMAGE = 101;

    ImageView UserImage;
    EditText UserNameField, UserAgeField, UserJobField;
    Button SaveProfileBtn;
    Uri UriProfileImage;
    //ProgressBar progressbar;
    String ProfileImageUrl;

    FirebaseAuth mAuth;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();

        UserImage = findViewById(R.id.UserImage);
        UserNameField = findViewById(R.id.UserNameField);
        UserAgeField = findViewById(R.id.UserAgeField);
        UserJobField = findViewById(R.id.UserJobField);

        //progressbar = findViewById(R.id.progressbar);
        SaveProfileBtn = findViewById(R.id.SaveProfileBtn);

        UserImage.setOnClickListener(this);
        SaveProfileBtn.setOnClickListener(this);

    }

    private void saveUserInformation() {
        String displayName = UserNameField.getText().toString();
        String displayAge = UserAgeField.getText().toString();
        String displayJob = UserJobField.getText().toString();

        if (displayName.isEmpty()) {
            UserNameField.setError("UserModel Name is required");
            UserNameField.requestFocus();
            return;
        }
        if (displayAge.isEmpty()) {
            UserAgeField.setError("Age is required");
            UserAgeField.requestFocus();
            return;
        }

        if (displayJob.isEmpty()) {
            UserJobField.setError("Job is required");
            UserJobField.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null && ProfileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(ProfileImageUrl))
                    .build();
            //add displayAge nd displayJob

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserProfileActivity.this, "Profile Updated",
                                        Toast.LENGTH_SHORT).show();


                            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            UriProfileImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), UriProfileImage);
                UserImage.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference UserProfileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis()
                        + ".jpg");

        if (UriProfileImage != null) {
            //progressbar.setVisibility(View.VISIBLE);
            UserProfileImageRef.putFile(UriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener
                            <UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progressbar.setVisibility(View.GONE);
                            ProfileImageUrl = taskSnapshot.getDownloadUrl().toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                           // progressbar.setVisibility(View.GONE);
                            Toast.makeText(UserProfileActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    });

        }

    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }



}

