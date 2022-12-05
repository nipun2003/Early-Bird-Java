package com.underdogdeveloper.earlybird.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.underdogdeveloper.earlybird.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().hide();
    }
}