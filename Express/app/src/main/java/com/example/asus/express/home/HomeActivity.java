package com.example.asus.express.home;

import android.support.v4.app.Fragment;

import com.example.asus.express.SingleFragmentActivity;

public class HomeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return HomeFragment.newInstance();
    }

}
