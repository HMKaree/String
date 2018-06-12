package com.example.android.string;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

public class CameraActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    Dialog logoDialog;
    TextView closePopUp;
    TextView BrandFollowing;
    TextView BrandSaves;
    Button UserSaved;
    TextView BrandName;
    TextView BrandCategory;
    ImageView BrandLogo;

    //CameraAdapter adapter;
    //RecyclerView cameraRecyclerView;
    //ImageView BrandLogo;
    //TextView BrandName;

    @Nullable
    private ClarifaiClient client;


    protected void onCreate(Bundle savedInstanceState) {
        client = new ClarifaiBuilder(getString(R.string.clarifai_api_key))
                // Optionally customize HTTP client via a custom OkHttp instance
                .client(new OkHttpClient.Builder()
                        .readTimeout(60, TimeUnit.SECONDS) // Increase timeout for poor mobile networks

                        // Log all incoming and outgoing data
                        // NOTE: You will not want to use the BODY log-level in production, as it will leak your API request details
                        // to the (publicly-viewable) Android log
                        .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log(String logString) {
                                Timber.e(logString);
                            }
                        }).setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build()
                )
                .buildSync(); // use build() instead to get a Future<ClarifaiClient>, if you don't want to block this thread

        super.onCreate(savedInstanceState);
        // Initialize our logging
        Timber.plant(new Timber.DebugTree());
    }

    @Override
    protected void onStart() {
        super.onStart();

        setContentView(R.layout.activity_camera);

        //BrandLogo =findViewById(R.id.BrandLogo);
        //BrandName = findViewById(R.id.BrandName);

        //concepts = new ArrayList<>();
        //Brands = new ArrayList<>();


        //super.onCreate();


        //private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            if (resultCode != RESULT_OK) {
                return;
            }
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null)
            {
                    final byte[] imageBytes = ClarifaiUtil.retrieveSelectedImage(this, data);
                    if (imageBytes != null) {
                        onImagePicked(imageBytes);
                    }

            }
        }

       /* switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if  (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    final byte[] imageBytes = ClarifaiUtil.retrieveSelectedImage(this, data);
                    if (imageBytes != null) {
                        onImagePicked(imageBytes);
                    }
                    break;
                }*/


    private void onImagePicked(@NonNull final byte[] imageBytes) {
        // Now we will upload our image to the Clarifai API


        new AsyncTask<Void, Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>>() {
            @Override
            protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(Void... params) {
                // The default Clarifai model that identifies concepts in images
//        //final ConceptModel generalModel = App.get().clarifaiClient().getDefaultModels().generalModel();


                return clarifaiClient().getModelByID("BrandLogo").
                        executeSync().get().asConceptModel().predict().
                        withInputs(ClarifaiInput.forImage(imageBytes)).executeSync();
                // Use this model to predict, with the image that the user just selected as the input
//        return generalModel.predict()
//            .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(imageBytes)))
//            .executeSync();}


            }

            @Override
            protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.error_while_contacting_api, Toast.LENGTH_SHORT).show();
                    return;
                }
                final List<ClarifaiOutput<Concept>> predictions = response.get();
                if (predictions.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.no_results_from_api, Toast.LENGTH_SHORT).show();

                    return;
                }

                LogoPopUp();
                //adapter.setData(predictions.get(0).data());
                //adapter.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
            }


        }.execute();
    }



    @NonNull
    public ClarifaiClient clarifaiClient() {
        final ClarifaiClient client = this.client;
        if (client == null) {
            throw new IllegalStateException("Cannot use Clarifai client before initialized");
        }
        return client;
    }


    public void LogoPopUp(){


        logoDialog = new Dialog(CameraActivity.this);
        logoDialog.setContentView(R.layout.logo_pop_up);
        logoDialog.setTitle("String");

        closePopUp = logoDialog.findViewById(R.id.closePopUp);
        BrandFollowing = logoDialog.findViewById(R.id.BrandFollowing);
        BrandSaves = logoDialog.findViewById(R.id.BrandSaves);
        UserSaved = logoDialog.findViewById(R.id.UserSaved);
        BrandLogo = logoDialog.findViewById(R.id.BrandLogo);
        BrandName = logoDialog.findViewById(R.id.BrandName);
        BrandCategory = logoDialog.findViewById(R.id.BrandCategory);

        UserSaved.setEnabled(true);
        closePopUp.setEnabled(true);

        UserSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        closePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             logoDialog.cancel();
            }
        });
        //logoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(color.TRANSPARENT));
        logoDialog.show();

    }



}

