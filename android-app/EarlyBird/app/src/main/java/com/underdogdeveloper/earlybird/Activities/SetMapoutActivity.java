package com.underdogdeveloper.earlybird.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.msquare.widget.mprogressbar.MProgressBar;
import com.sdsmdg.harjot.crollerTest.Croller;
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;
import com.underdogdeveloper.earlybird.R;

public class SetMapoutActivity extends AppCompatActivity {

    Toolbar toolbar;
    Croller croller;
    MProgressBar progressBar;
    Button saveMapout;
    TextView showMapoutTime;
    public static final int MAX_MAPOUT_TIME=100;
    int mapOut=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_mapout);
        initView();
        setSupportActionBar(toolbar);
        mapOut=getMapout();
        initaliseCroller();

        croller.setOnCrollerChangeListener(new OnCrollerChangeListener() {
            @Override
            public void onProgressChanged(Croller croller, int progress) {
                progressBar.setSecondaryProgress(progress);
                if((progress/10)<10){
                    showMapoutTime.setText("0"+progress/10);
                }
                else showMapoutTime.setText(""+(progress/10));
            }

            @Override
            public void onStartTrackingTouch(Croller croller) {

            }

            @Override
            public void onStopTrackingTouch(Croller croller) {
                mapOut=croller.getProgress()/10;
            }
        });
        saveMapout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeMapOut();
                startActivity(new Intent(SetMapoutActivity.this,MainActivity.class));
            }
        });

    }
    private void initView(){
        croller=findViewById(R.id.crollerMap);
        progressBar=findViewById(R.id.progressBarMap);
        toolbar=findViewById(R.id.toolBarSettings);
        saveMapout=findViewById(R.id.saveMapOut);
        showMapoutTime=findViewById(R.id.showMapOutTime);
    }
    private void initaliseCroller(){
        croller.setMin(10);
        croller.setMax(MAX_MAPOUT_TIME);
        croller.setProgress(mapOut*10);
    }
    private int getMapout(){
        SharedPreferences sharedPreferences=getSharedPreferences(String.valueOf(R.string.spMapout),MODE_PRIVATE);
        return sharedPreferences.getInt("mapOutTime",3);
    }
    private void storeMapOut(){
        SharedPreferences sharedPreferences=getSharedPreferences(String.valueOf(R.string.spMapout),MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("mapOutTime",mapOut);
        editor.apply();
    }
}