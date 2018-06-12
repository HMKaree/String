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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    CircleImageView UserImage;
    TextView UserFollowing, UserSaved, UserName, UserLocation, UserAge, UserJob;
    FirebaseDatabase database;
    DatabaseReference UserProfileDatabase, savedBrands;
    FirebaseAuth mAuth;

    Button editProfileBtn, signOutBtn;
    private String currentUserId;
    //String brandId;
    int countSaves;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //brandId = database.getReference().child("BrandProfiles").getKey();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        UserProfileDatabase = database.getReference().child("Users").child(currentUserId).child("Profile");
        savedBrands = database.getReference().child("savedBrands").child(currentUserId).child("savedBrands");

        UserFollowing = findViewById(R.id.UserFollowing);
        UserSaved = findViewById(R.id.UserSaved);
        UserName = findViewById(R.id.UserName);
        UserLocation = findViewById(R.id.UserLocation);

        UserAge = findViewById(R.id.UserAge);
        UserJob = findViewById(R.id.UserJob);
        UserImage = findViewById(R.id.UserImage);
        editProfileBtn = findViewById(R.id.editProfileBtn);
        editProfileBtn.setOnClickListener(this);

        signOutBtn = findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(this);






        UserProfileDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChild("UserImage")) {
                    String profileImage = dataSnapshot.child("UserImage").getValue().toString();

                    Picasso.with(ProfileActivity.this).load(profileImage).placeholder(R.drawable.ic_black_profile)
                            .into(UserImage);
                }
                if(dataSnapshot.hasChild("UserName")) {
                    String profileName = dataSnapshot.child("UserName").getValue().toString();

                    UserName.setText(profileName);
                }
                if(dataSnapshot.hasChild("UserAge")) {
                    String profileAge = dataSnapshot.child("UserAge").getValue().toString();

                    UserAge.setText(profileAge);
                }
                if(dataSnapshot.hasChild("UserJob")) {
                    String profileJob = dataSnapshot.child("UserJob").getValue().toString();

                    UserJob.setText(profileJob);
                }
                if(dataSnapshot.hasChild("UserLocation")) {
                    String profileLocation = dataSnapshot.child("UserLocation").getValue().toString();

                    UserLocation.setText(profileLocation);
                }
                    //String profileEmail = dataSnapshot.child("identifier").getValue().toString();
                    //String brandFollows = dataSnapshot.child("UserFollowing").getValue().toString();
                    //String brandSaved = dataSnapshot.child("UserSaved").getValue().toString();



                    //UserEmail.setText(profileEmail);





                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        noOfSavedBrands();
        //noOfFollowedBrands();


}
    private void noOfSavedBrands() {
        savedBrands.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    countSaves = (int) dataSnapshot.getChildrenCount();
                    UserSaved.setText(Integer.toString(countSaves));
                }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void noOfFollowedBrands() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editProfileBtn:
                startActivity(new Intent(ProfileActivity.this, UserProfileActivity.class));
                break;

            case R.id.signOutBtn:
                mAuth.signOut();
                 startActivity(new Intent(ProfileActivity.this, LogInActivity.class));
                 finish();
                  break;
        }



    }
}

