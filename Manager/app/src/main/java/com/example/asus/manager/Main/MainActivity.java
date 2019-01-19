package com.example.asus.manager.Main;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.asus.manager.R;
import com.example.asus.manager.SingleFragmentActivity;

public class MainActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return MainFragment.newInstance();
    }
}
