package com.example.android.string;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SavedActivity extends AppCompatActivity {

    RecyclerView savedRecyclerView;
    FirebaseDatabase database;
    DatabaseReference Brands;
    //private ValueEventListener BrandListener;

    //ArrayList<Brand> brandList;

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
        //brandList = new ArrayList<>();



        DisplayAllBrands();
    }

    private void DisplayAllBrands(){

        FirebaseRecyclerAdapter<Brand, savedViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Brand, savedViewHolder>
                        (
                                Brand.class,
                                R.layout.saved_cardview,
                                savedViewHolder.class,
                                Brands
                        )
                {

                    @Override
                    protected void populateViewHolder(savedViewHolder viewHolder, Brand model, int position)
                    {
                        final String postKey = getRef(position).getKey();
                        viewHolder.setBrandLogo(getApplicationContext(),model.getBrandLogo());
                        viewHolder.setBrandName(model.getBrandName());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Intent clickPost = new Intent(SavedActivity.this,BrandProfileActivity.class);
                                clickPost.putExtra("postKey", postKey);
                                startActivity(clickPost);

                            }
                        });
                    }
                };
        savedRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class savedViewHolder extends RecyclerView.ViewHolder

    {
        View mView;

        public savedViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setBrandName(String brandName)
        {
            TextView BrandName = mView.findViewById(R.id.BrandName);
            BrandName.setText(brandName);
        }

        public void setBrandLogo(Context ctx, String brandLogo)
        {
            ImageView BrandLogo = mView.findViewById(R.id.BrandLogo);
            Picasso.with(ctx).load(brandLogo).into(BrandLogo);
        }
    }

   /* @Override
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


    }*/
}
