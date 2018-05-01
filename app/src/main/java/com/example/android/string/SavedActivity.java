package com.example.android.string;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SavedActivity extends AppCompatActivity {

    RecyclerView savedRecyclerView;
    FirebaseDatabase database;
    DatabaseReference Brands;
    private ValueEventListener BrandListener;

    ArrayList<Brand> brandList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);


        //RecyclerView
        savedRecyclerView = findViewById(R.id.savedRecyclerview);
        savedRecyclerView.setHasFixedSize(true);
        savedRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //sending query to database
        database = FirebaseDatabase.getInstance();
        Brands = database.getReference().child("BrandProfiles/");
        brandList = new ArrayList<>();

        //BrandStorage = FirebaseStorage.getInstance().getReference("BrandPictures");
        //Brands.keepSynced(true);
    }

    @Override
    protected void onStart() {
        super.onStart();


        // Read from the database
        BrandListener = Brands.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot brandSnapshot : dataSnapshot.getChildren()) {
                    Brand brand = brandSnapshot.getValue(Brand.class);
                    brandList.add(brand);
                    //Log.d(TAG, "Value is: " + value);

                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        SavedRecyclerViewAdapter adapter = new SavedRecyclerViewAdapter(brandList);
        savedRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Brands.removeEventListener(BrandListener);


    }
}
