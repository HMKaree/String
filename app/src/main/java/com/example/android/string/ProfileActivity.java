package com.example.android.string;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView UserImage;
    TextView UserFollowing, UserSaved, UserName, UserLocation, UserEmail, UserAge, UserJob;
    FirebaseDatabase database;
    DatabaseReference UserProfileDatabase;
    FirebaseAuth mAuth;
    ValueEventListener UserProfileListener;
    Button editProfileBtn, signOutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        UserFollowing = findViewById(R.id.UserFollowing);
        UserSaved = findViewById(R.id.UserSaved);
        UserName = findViewById(R.id.UserName);
        UserLocation = findViewById(R.id.UserLocation);
        UserEmail = findViewById(R.id.UserEmail);
        UserAge = findViewById(R.id.UserAge);
        UserJob = findViewById(R.id.UserJob);
        UserImage = findViewById(R.id.UserImage);

        signOutBtn = findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        UserProfileDatabase = database.getReference().child("UserProfiles");

        //addUserChangeListener();
        //getUserDetailListener();
        mAuth = FirebaseAuth.getInstance();

        UserProfileDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot UserProfileSnapshot : dataSnapshot.getChildren()) {
                    UserModel userModel = UserProfileSnapshot.getValue(UserModel.class);
                    UserImage.setImageURI(Uri.parse(userModel.getUserImage().toString()));
                    UserName.setText(userModel.getUserName());
                    UserJob.setText(userModel.getUserJob());
                    UserAge.setText(userModel.getUserAge());
                    UserEmail.setText(userModel.getUserEmail());
                    UserSaved.setText(userModel.getUserSaved());
                    UserFollowing.setText(userModel.getUserFollowing());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    /*ValueEventListener(new ValueEventListener) {
        @Override
        public void onDataChange (DataSnapshot dataSnapshot){

            for (DataSnapshot userModelSnapshot : dataSnapshot.getChildren()) {

                UserModel userModel = userModelSnapshot.getValue(UserModel.class);
                if (userModel != null) {
                    UserName.getText().toString();
                    UserName.setText(userModel.getUserName());
                    UserLocation.setText(userModel.getUserLocation());
                    UserAge.setText(userModel.getUserAge());
                    UserEmail.setText(userModel.getUserEmail());
                    UserJob.setText(userModel.getUserJob());
                    UserFollowing.setText(userModel.getUserFollowing());
                    UserSaved.setText(userModel.getUserSaved());
                }
            }


            @Override
            public void onCancelled (DatabaseError databaseError){
                Toast.makeText(getApplicationContext(), "loading failed", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }*/









       /* ViewSwitcher switcher = findViewById(R.id.ageSwitcher);
        switcher.showNext();
        TextView UserAge = switcher.findViewById(R.id.UserAge);
        UserAge.setText(UserAge.getText().toString().trim());*/


      /* private void updateUserProfile(){
            userModel.setUserName("value");
        userModel.setUserLocation("Value");
        userModel.setUserJob("value");
        userModel.setUserAge("value");*/

      /*  UserProfiles.push().setValue(userModel);
        Toast.makeText(getApplicationContext(),"Your Profile has been updated", Toast.LENGTH_SHORT).show();*/

}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editProfileBtn:
                //toggleButton();
                //updateUserProfile();
                break;

            case R.id.signOutBtn:
                mAuth.signOut();
                 startActivity(new Intent(ProfileActivity.this, LogInActivity.class));
                 finish();
                  break;
        }



    }
}

