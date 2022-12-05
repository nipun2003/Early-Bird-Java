package com.underdogdeveloper.earlybird.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.underdogdeveloper.earlybird.R;

public class ShowAlarmActivity extends AppCompatActivity {

    Button mapOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarm);
        getSupportActionBar().hide();
        initView();
        mapOut.setOnClickListener(v -> startActivity(new Intent(ShowAlarmActivity.this, DetectorActivity.class)));
    }

    private void initView() {
        mapOut = findViewById(R.id.mapOut);
    }
}