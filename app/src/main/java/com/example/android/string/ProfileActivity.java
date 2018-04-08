package com.example.android.string;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    TextView UserFollowing, UserSaved, UserName, UserLocation, UserEmail, UserAge, UserJob;
    FirebaseDatabase database;
    DatabaseReference User;

    UserModel userModel = new UserModel();

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

        database = FirebaseDatabase.getInstance();
        User = database.getReference().child("UserModel");
    }
}
