package com.example.asus.express.profile;

import android.support.v4.app.Fragment;

import com.example.asus.express.SingleFragmentActivity;


public class ProfileActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return ProfileFragment.newInstance();
    }
}
