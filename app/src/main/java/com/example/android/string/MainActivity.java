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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {

    ImageView camera;
    ImageView feed;
    ImageView saved;
    ImageView search;
    ImageView more;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

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


           /* case R.id.navigation_profile:
                Intent profileActivity = new Intent(this, ProfileActivity.class);
                startActivity(profileActivity);
                break;*/

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




    /*public void LogoPopUp(View view){
        TextView closePopUp;
        TextView BrandFollowing;
        TextView BrandSaves;
        Button UserSaved;

        logoDialog.setContentView(R.layout.logo_pop_up);
        closePopUp = findViewById(R.id.closePopUp);
        BrandFollowing = findViewById(R.id.BrandFollowing);
        BrandSaves = findViewById(R.id.BrandSaves);
        UserSaved = findViewById(R.id.UserSaved);

        UserSaved.setOnClickListener(new View.OnClickListener(){
            @Override
            public void OnClick(View v){
                logoDialog.dismiss();
            }

        });
        logoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(color.TRANSPARENT));
        logoDialog.show();

    }*/





