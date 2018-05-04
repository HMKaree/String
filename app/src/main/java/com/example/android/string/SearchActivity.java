package com.example.android.string;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.android.string.R.menu.navigation;

public class SearchActivity extends AppCompatActivity {


    private RecyclerView searchRecyclerView;
    private FirebaseDatabase database;
    private DatabaseReference Brands;

   // private EditText searchBox;
   // private ImageButton searchBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        //RecyclerView
        searchRecyclerView = findViewById(R.id.searchRecyclerview);
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //sending query to database
        database = FirebaseDatabase.getInstance();
        Brands = database.getReference().child("BrandProfiles");

       /* searchBox = findViewById(R.id.searchBox);
        searchBtn = findViewById(R.id.searchBtn);*/

       /* searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String searchInput = searchBox.getText().toString();
                
                searchBrands(searchInput);
            }
        });*/


        DisplayAllBrands();
    }




    //private void searchBrands(String searchInput)
       /* Toast.makeText(this, " Searching...", Toast.LENGTH_LONG)
                .show();

        Query BrandQuery = Brands.orderByChild("BrandName")
                .startAt(searchInput).endAt(searchInput + "\uf8ff");*/
       private void DisplayAllBrands(){

        FirebaseRecyclerAdapter<Brand, searchViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Brand, searchViewHolder>
                        (
                             Brand.class,
                             R.layout.search_cardview,
                               searchViewHolder.class,
                                Brands
                        )
                {
            @Override
            protected void populateViewHolder(searchViewHolder viewHolder, Brand model, int position)
                    {
                        final String postKey = getRef(position).getKey();
                   viewHolder.setBrandLogo(getApplicationContext(),model.getBrandLogo());
                   viewHolder.setBrandName(model.getBrandName());

                   viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v)
                       {
                         Intent clickPost = new Intent(SearchActivity.this,BrandProfileActivity.class);
                        clickPost.putExtra("postKey", postKey);
                         startActivity(clickPost);

                       }
                   });
            }
        };
        searchRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    public static class searchViewHolder extends RecyclerView.ViewHolder

    {
         View mView;

        public searchViewHolder(View itemView)
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

   /* private void searchBrands(String searchInput)
    {
        Toast.makeText(this, " Searching...", Toast.LENGTH_LONG)
                .show();

        Query BrandQuery = Brands.orderByChild("BrandName")
                .startAt(searchInput).endAt(searchInput + "\uf8ff");
    }*/
}



   /* @Override
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


        searchRecyclerView.setAdapter(adapter);

    }


    }
*/
