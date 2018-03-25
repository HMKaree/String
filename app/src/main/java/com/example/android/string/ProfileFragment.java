package com.example.android.string;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by Hana Kari on 3/15/2018.
 */

public class ProfileFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, null);

       /* ImageView UserImageProfileView;
        EditText UserNameProfileView, UserAgeProfileView, UserJobProfileView;
        Uri ProfileImageView;

        UserImageProfileView = findViewbyId(R.id.UserImageProfileView);*/
    }
}
