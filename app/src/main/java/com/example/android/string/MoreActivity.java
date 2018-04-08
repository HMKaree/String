package com.example.android.string;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener{

    CardView learnMore, userGuide, contactUs,Profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        Profile = findViewById(R.id.Profile);
        learnMore = findViewById(R.id.learnMore);
        userGuide = findViewById(R.id.userGuide);
        contactUs = findViewById(R.id.contactUs);

        Profile.setOnClickListener(this);
        learnMore.setOnClickListener(this);
        userGuide.setOnClickListener(this);
        contactUs.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.Profile:
                Intent ProfileActivity = new Intent(this, ProfileActivity.class);
                startActivity(ProfileActivity);
                break;

            case R.id.learnMore:
                Intent learMoreActivity = new Intent(this, LearnMore.class);
                startActivity(learMoreActivity);
                break;

           case R.id.userGuide:
                Intent userGuideActivity = new Intent(this, UserGuide.class);
                startActivity(userGuideActivity);
                break;

            case R.id.contactUs:
                Intent contactUsActivity = new Intent(this, ContactUs.class);
                startActivity(contactUsActivity);
                break;
        }

    }
}
