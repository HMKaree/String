package com.example.android.string;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    CardView cameraMenu;
    CardView feedMenu;
    CardView savedMenu;
    CardView searchMenu;
    CardView profileMenu;
    CardView moreMenu;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            cameraMenu = findViewById(R.id.navigation_camera);
            cameraMenu.setOnClickListener(this);

            feedMenu = findViewById(R.id.navigation_feed);
            feedMenu.setOnClickListener(this);

            savedMenu = findViewById(R.id.navigation_saved);
            savedMenu.setOnClickListener(this);

            searchMenu = findViewById(R.id.navigation_search);
            searchMenu.setOnClickListener(this);


            moreMenu = findViewById(R.id.navigation_more);
            moreMenu.setOnClickListener(this);

        }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.navigation_camera:
                    OpenCamera();
                    break;

                case R.id.navigation_feed:
                    Intent intent = new Intent(this, FeedActivity.class);
                    startActivity(intent);
                    break;


                case R.id.navigation_search:
                    Intent searchActivity = new Intent(this, SearchActivity.class);
                    startActivity(searchActivity);
                    break;


                case R.id.navigation_profile:
                    Intent profileActivity = new Intent(this, ProfileActivity.class);
                    startActivity(profileActivity);
                    break;

                case R.id.navigation_more:
                    Intent moreActivity = new Intent(this, MoreActivity.class);
                    startActivity(moreActivity);
                    break;
            }

    }









    //beginning of initial camera test
    public void OpenCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }
}

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            capturedImage.setImageBitmap(bp);
        }
    }*/
//end of initial camera test

//show pop up when logo has been recognized
/*
Dialog logoDialog;

public void LogoPopUp(View view){
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
@Ovaerride
public void OnClick(View v){
logoDialog.dismiss();
}

});
logoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(color.TRANSPARENT));
logoDialog.show();

}
 */




