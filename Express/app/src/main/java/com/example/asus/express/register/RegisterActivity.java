package com.example.asus.express.register;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.asus.express.R;
import com.example.asus.express.SingleFragmentActivity;

public class RegisterActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return RegisterFragment.newInstance();
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_fragment;
    }

    public static Intent newIntent(Context context){
        Intent intent=new Intent(context,RegisterActivity.class);
        return intent;
    }
}
