package com.example.android.string;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

import com.microsoft.appcenter.AppCenter; import com.microsoft.appcenter.analytics.Analytics; import com.microsoft.appcenter.crashes.Crashes;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {

    private ImageView camera;
    private ImageView feed;
    private ImageView saved;
    private ImageView search;
    private ImageView more;

    private FirebaseAuth mAuth;
    private DatabaseReference userProfiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //AppCenter.start(getApplication(), "66b038ff-c10a-4028-ace4-e8674e471e37", Analytics.class, Crashes.class);

        mAuth = FirebaseAuth.getInstance();
        userProfiles = FirebaseDatabase.getInstance().getReference().child("UserProfiles");

        camera = findViewById(R.id.navigation_camera);
        feed = findViewById(R.id.navigation_feed);
        saved = findViewById(R.id.navigation_saved);
        search = findViewById(R.id.navigation_search);
        more = findViewById(R.id.navigation_more);

        camera.setOnClickListener(this);
        feed.setOnClickListener(this);
        saved.setOnClickListener(this);
        search.setOnClickListener(this);
        more.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();


        if(currentUser == null){
            sendUserToLoginActivity();
        }

        else {
            CheckIfUserExists();
        }
    }

    private void CheckIfUserExists() {

        final String current_user_id = mAuth.getCurrentUser().getUid();
        userProfiles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(current_user_id)){

                    Intent setupActivity = new Intent(MainActivity.this, RegisterActivity.class);
                    setupActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LogInActivity.class );
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())

        {
            case R.id.navigation_camera:
                Intent camera = new Intent(this, CameraActivity.class);
                startActivity(camera);
                //dispatchTakePictureIntent();
                break;

            case R.id.navigation_feed:
                Intent intent = new Intent(this, FeedActivity.class);
                startActivity(intent);
                break;

            case R.id.navigation_saved:
                Intent savedActivity = new Intent(this, SavedActivity.class);
                startActivity(savedActivity);
                break;


            case R.id.navigation_search:
                Intent searchActivity = new Intent(this, SearchActivity.class);
                startActivity(searchActivity);
                break;



            case R.id.navigation_more:
                Intent moreActivity = new Intent(this, MoreActivity.class);
                startActivity(moreActivity);
                break;
        }
    }



}


   /* public void pickImage() {
        startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image*//*"), PICK_IMAGE);
    }*/

    /*private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }*/









