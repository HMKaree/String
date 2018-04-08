package com.example.android.string;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    RecyclerView searchRecyclerView;
    FirebaseDatabase database;
    DatabaseReference Brands;

    ArrayList<Brand> brandList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //RecyclerView
        searchRecyclerView = findViewById(R.id.searchRecyclerview);
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        //sending query to database
        database = FirebaseDatabase.getInstance();
        Brands = database.getReference().child("Brands");
    }

    @Override
    protected void onStart () {
        super.onStart();


        // Read from the database
        Brands.addValueEventListener(new ValueEventListener() {
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

        SearchRecyclerViewAdapter adapter = new SearchRecyclerViewAdapter(brandList);
        searchRecyclerView.setAdapter(adapter);

    }


}
