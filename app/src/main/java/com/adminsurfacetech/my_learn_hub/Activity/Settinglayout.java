package com.adminsurfacetech.my_learn_hub.Activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

import com.adminsurfacetech.my_learn_hub.R;

public class Settinglayout extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settinglayout);
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);

      //  setSupportActionBar(mToolbar);
       // setActionBarTitle("Settings");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
