package com.example.android.string;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity
{
    private EditText UserNameField, UserAgeField, UserJobField, UserLocationField;
    private Button saveProfileBtn;
   private CircleImageView userImage;

    private ProgressDialog loadingBar;


    private DatabaseReference StringDatabase;
    private FirebaseAuth mAuth;

    private StorageReference StringStorage;

    String currentUserId;
    final static int Gallery_Pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        StringDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Profile");

        StringStorage = FirebaseStorage.getInstance().getReference().child("ProfileImages");

        userImage = findViewById(R.id.UserImage);
        UserNameField = findViewById(R.id.UserNameField);
        UserAgeField = findViewById(R.id.UserAgeField);
        UserJobField = findViewById(R.id.UserJobField);
        UserLocationField = findViewById(R.id.UserLocationField);

        loadingBar = new ProgressDialog(this);
        saveProfileBtn = findViewById(R.id.SaveProfileBtn);


        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });

       userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Gallery_Pick);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null)
        {
            Uri uri = data.getData();


            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            if (resultCode == RESULT_OK)
            {

                loadingBar.setTitle("Profile Image");
                loadingBar.setMessage("Please wait while your profile is updated");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                Uri resultUri = result.getUri();

                StorageReference filePath = StringStorage.child(currentUserId + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(UserProfileActivity.this, "Profile image stored successfully", Toast.LENGTH_SHORT).show();

                             final String downloadUrl = task.getResult().getDownloadUrl().toString();

                            StringDatabase.child("UserImage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                                Intent setupIntent = new Intent(UserProfileActivity.this, UserProfileActivity.class);
                                                startActivity(setupIntent);
                                                // UserImage.setImageURI(uri);

                                                Toast.makeText(UserProfileActivity.this, "Image stored", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();

                                            } else {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(UserProfileActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }

                                        }
                                    });
                        }
                    }
                });
            }

            else
            {
                Toast.makeText(this, "Error Occurred: Image cropping failed, Try Again", Toast.LENGTH_LONG).show();
                loadingBar.dismiss();
            }

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


        else
            {

              loadingBar.setTitle("Loading");
            loadingBar.setMessage("Please wait while your profile is created");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);



           /* StorageReference filepath = StringStorage.child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUrl = taskSnapshot.getDownloadUrl().toString();*/

            HashMap userMap = new HashMap();

                userMap.put("UserId", currentUserId);
            userMap.put("UserName", displayUserName);
            userMap.put("UserAge", displayUserAge);
            userMap.put("UserLocation", displayUserLocation);
            userMap.put("UserJob", displayUserJob);
            //userMap.put("UserImage", downloadUrl);


            StringDatabase.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if (task.isSuccessful()) {

                        SendUserToMainActivity();
                        Toast.makeText(UserProfileActivity.this, "You have been registered", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                    else {
                        String message = task.getException().getMessage();
                        Toast.makeText(UserProfileActivity.this, " Error Occured: "
                                + message, Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                }
            });


        }
    }
    private void SendUserToMainActivity() {

        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}











