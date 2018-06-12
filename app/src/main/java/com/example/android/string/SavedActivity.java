package com.example.android.string;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class SavedActivity extends AppCompatActivity {

    private RecyclerView savedRecyclerView;
    private FirebaseDatabase database;
    private DatabaseReference savedBrands, followedBrands, Brands;

    private FirebaseAuth mAuth;
    private String currentUserId;
    Boolean followChecker = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);


        //RecyclerView
        savedRecyclerView = findViewById(R.id.savedRecyclerview);
        savedRecyclerView.setHasFixedSize(true);
        savedRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //sending query to database
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();

        Brands = database.getReference().child("BrandProfiles");
        savedBrands = database.getReference().child("Users").child(currentUserId).child("savedBrands");
        followedBrands = database.getReference().child("Users").child(currentUserId).child("followedBrands");


        DisplaySavedBrands();
    }

    private void DisplaySavedBrands() {

        FirebaseRecyclerAdapter<Brand, savedViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Brand, savedViewHolder>
                        (
                                Brand.class,
                                R.layout.saved_cardview,
                                savedViewHolder.class,
                                savedBrands
                        ) {

                    @Override
                    protected void populateViewHolder(savedViewHolder viewHolder, Brand model, int position) {
                        final String postKey = getRef(position).getKey();
                        viewHolder.setBrandLogo(getApplicationContext(), model.getBrandLogo());
                        viewHolder.setBrandName(model.getBrandName());

                        viewHolder.setFollowingText(postKey);

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent clickPost = new Intent(SavedActivity.this, BrandProfileActivity.class);
                                clickPost.putExtra("postKey", postKey);
                                startActivity(clickPost);

                            }
                        });

                        viewHolder.followIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                followChecker = true;

                                followedBrands.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        if (followChecker.equals(true)) {

                                            if (dataSnapshot.hasChild(postKey)) {

                                                followedBrands.child(postKey).removeValue();
                                                followChecker = false;
                                            } else {
                                                Brands.addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                        //String brandKey = dataSnapshot.getKey();

                                                        final String logoBrand = dataSnapshot.child(postKey).child("BrandLogo").getValue().toString();
                                                        final String nameBrand = dataSnapshot.child(postKey).child("BrandName").getValue().toString();

                                                        followedBrands.child(postKey).child("BrandLogo").setValue(logoBrand);
                                                        followedBrands.child(postKey).child("BrandName").setValue(nameBrand);

                                                    }

                                                    @Override
                                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                                    }

                                                    @Override
                                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                                    }

                                                    @Override
                                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot
                                                                       dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot
                                                                     dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                followChecker = false;
                            }
                        });
                    }
                };


                            /*@Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });*/
                        savedRecyclerView.setAdapter(firebaseRecyclerAdapter);


                    }


                    public static class savedViewHolder extends RecyclerView.ViewHolder

                    {
                        View mView;

                        ImageButton followIcon;
                        ImageButton shareIcon;
                        ImageButton deleteIcon;
                        TextView followText;
                        DatabaseReference followedBrands, Brands;
                        private FirebaseAuth mAuth;
                        private String currentUserId;

                        public savedViewHolder(View itemView) {
                            super(itemView);
                            mView = itemView;

                            followIcon = mView.findViewById(R.id.followIcon);
                            shareIcon = mView.findViewById(R.id.shareIcon);
                            deleteIcon = mView.findViewById(R.id.deleteIcon);
                            followText = mView.findViewById(R.id.followingText);

                            mAuth = FirebaseAuth.getInstance();
                            currentUserId = mAuth.getCurrentUser().getUid();

                            Brands = FirebaseDatabase.getInstance().getReference().child("BrandProfiles");
                            followedBrands = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("followedBrands");
                        }

                        public void setBrandName(String brandName) {
                            TextView BrandName = mView.findViewById(R.id.BrandName);
                            BrandName.setText(brandName);
                        }

                        public void setBrandLogo(Context ctx, String brandLogo) {
                            ImageView BrandLogo = mView.findViewById(R.id.BrandLogo);
                            Picasso.with(ctx).load(brandLogo).into(BrandLogo);
                        }


                        public void setFollowingText(final String postKey) {
                            followedBrands.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(postKey)) {

                                        followText.setText("Following");
                                    } else {
                                        followText.setText("");
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }





   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        savedBrands.removeEventListener(BrandListener);


    }*/
                    }
                }
