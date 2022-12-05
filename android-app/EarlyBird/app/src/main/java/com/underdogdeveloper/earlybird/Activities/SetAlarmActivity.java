package com.underdogdeveloper.earlybird.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.msquare.widget.mprogressbar.MProgressBar;
import com.sdsmdg.harjot.crollerTest.Croller;
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;
import com.underdogdeveloper.earlybird.DbHandler;
import com.underdogdeveloper.earlybird.Models.AlarmModel;
import com.underdogdeveloper.earlybird.Models.TimeModel;
import com.underdogdeveloper.earlybird.R;

import java.util.ArrayList;
import java.util.Arrays;

public class SetAlarmActivity extends AppCompatActivity {

//    Variable Declaration
    Boolean sun = false, mon = false, tue = false, wed = false, thu = false, fri = false, sat = false;
    ArrayList<String> days = new ArrayList<>();
    Button saveAlarm;
    Boolean isHour=true,isMinute=false,am=true,pm=false;
    Button sunday,monday,tuesday,wednesday,thursday,friday,saturday;
    int sno;
    Croller croller;
    TimeModel timeModel;
    MProgressBar progressBar;
    TextView hour,minute,meredian;
    int listSize;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        getSupportActionBar().hide();

//        Initialising the view

        initView();
        sno = getIntent().getIntExtra("sno", 0);

        timeModel=new TimeModel();
        if(sno!=0) {
            setValues();
            tto12(timeModel.getHour());
            sethour(timeModel.getHour());
            setMinute(timeModel.getMinute());
        }
        else {
            listSize=getIntent().getIntExtra("listSize",0);
        }
//        Initialising the Croller
        initialiseCroller();

//        Getting the serial number of alarm which we have to update
        DbHandler handler = new DbHandler(this);

//        This is like a timePicker
        croller.setOnCrollerChangeListener(new OnCrollerChangeListener() {
            @Override
            public void onProgressChanged(Croller croller, int progress) {
                if (isHour) {
                    Double d = (progress) * 0.42;
                    progressBar.setSecondaryProgress(d.intValue());
                    sethour(progress/20);
                }
                else if(isMinute){
                    Double d=(progress)* 0.42;
                    progressBar.setSecondaryProgress(d.intValue());
                    setMinute(progress/4);
                }
            }

            @Override
            public void onStartTrackingTouch(Croller croller) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onStopTrackingTouch(Croller croller) {
                if(isHour){
                    int hourProgress=croller.getProgress()/20;
                    isHour=false;
                    isMinute=true;
                    timeModel.setHour(hourProgress);
                    initialiseCroller();
                }
                else if(isMinute){
                    timeModel.setMinute((croller.getProgress())/4);
                }

            }
        });

//        Saving the alarm
        saveAlarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int hourProgress = timeModel.getHour();
                if (hourProgress == 12 && am) {
                    timeModel.setHour(0);
                } else if (hourProgress == 12 && pm) {
                    timeModel.setHour(hourProgress);
                } else if (pm) {
                    timeModel.setHour(hourProgress + 12);
                } else {
                    timeModel.setHour(hourProgress);
                }
                AlarmModel model = new AlarmModel();
                String day = getDays();
                if (day.equals("")) {
                    Toast.makeText(SetAlarmActivity.this, "please select at least one day", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (sno != 0) {
                        model.setSno(sno);
                        model.setHour(timeModel.getHour());
                        model.setMinute(timeModel.getMinute());
                        model.setAlarmState(1);
                        model.setDays(day);
                        handler.updateAlarm(model);
//                    handler.close();
                    } else {
                        model.setSno(listSize+1);
                        model.setHour(timeModel.getHour());
                        model.setMinute(timeModel.getMinute());
                        model.setAlarmState(1);
                        model.setDays(day);
                        handler.addAlarm(model);
                    }
                    Intent intent=new Intent(SetAlarmActivity.this,MainActivity.class);
                    intent.putExtra("animate",0);
                    if(sno==0){
                        intent.putExtra("listS",listSize);
                    }
                    startActivity(intent);
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initialiseCroller(){
        if(isHour) {
            croller.setMin(20);
            hour.setBackground(SetAlarmActivity.this.getDrawable(R.drawable.time_selector_highlighter));
            minute.setBackgroundResource(0);
            if(timeModel.getHour() != 0){
                croller.setProgress(timeModel.getHour()*20);
            }else {
                croller.setProgress(240);
            }
            croller.setMax(240);
            croller.setEnabled(true);
        }
        else if(isMinute){
            minute.setBackground(SetAlarmActivity.this.getDrawable(R.drawable.time_selector_highlighter));
            hour.setBackgroundResource(0);
            croller.setMin(0);
            if(timeModel.getMinute()!=0){
                croller.setProgress((timeModel.getMinute())*4);
            }
            else {
                croller.setProgress(0);
            }
            croller.setMax(236);
            croller.setEnabled(true);
        }
    }
    private void sethour(int _hour){
       if(_hour<10){
           hour.setText("0"+_hour);
       }
       else {
           hour.setText(""+_hour);
       }
    }
    private void setMinute(int __minute){
        if(__minute<10){
            minute.setText("0"+__minute);
        }
        else {
            minute.setText(""+__minute);
        }
    }
    private void initView(){
        saveAlarm = findViewById(R.id.saveAlarm);
        hour=findViewById(R.id.alarmEditHour);
        minute=findViewById(R.id.alarmEditMinute);
        progressBar=findViewById(R.id.progressBar);
        croller=findViewById(R.id.croller);
        meredian=findViewById(R.id.alarmEditMeredian);
        sunday=findViewById(R.id.sunday);
        monday=findViewById(R.id.monday);
        tuesday=findViewById(R.id.tuesday);
        wednesday=findViewById(R.id.wednesday);
        thursday=findViewById(R.id.thursday);
        friday=findViewById(R.id.friday);
        saturday=findViewById(R.id.saturday);
    }

    private void setValues() {
        DbHandler database=new DbHandler(SetAlarmActivity.this);
        AlarmModel alarmModel=database.getAlarm(sno);
        database.close();
        timeModel.setHour(alarmModel.getHour());
        timeModel.setMinute(alarmModel.getMinute());
        String days[] =alarmModel.getDays().split(" ");
        if(Arrays.asList(days).contains("sun")){
            sun=true;
            sunday.setBackgroundResource(R.drawable.day_pick_on);
            sunday.setTextColor(getResources().getColor(R.color.text_color));
        }
        else {
            sun=false;
            sunday.setBackgroundResource(R.drawable.day_pick_off);
            sunday.setTextColor(getResources().getColor(R.color.white));
        }
        if(Arrays.asList(days).contains("mon")){
            mon=true;
            monday.setBackgroundResource(R.drawable.day_pick_on);
            monday.setTextColor(getResources().getColor(R.color.text_color));
        }
        else {
            mon=false;
            monday.setBackgroundResource(R.drawable.day_pick_off);
            monday.setTextColor(getResources().getColor(R.color.white));
        }
        if(Arrays.asList(days).contains("tue")){
            tue=true;
            tuesday.setBackgroundResource(R.drawable.day_pick_on);
            tuesday.setTextColor(getResources().getColor(R.color.text_color));
        }
        else {
            tue=false;
            tuesday.setBackgroundResource(R.drawable.day_pick_off);
            tuesday.setTextColor(getResources().getColor(R.color.white));
        }
        if(Arrays.asList(days).contains("wed")){
            wed=true;
            wednesday.setBackgroundResource(R.drawable.day_pick_on);
            wednesday.setTextColor(getResources().getColor(R.color.text_color));
        }
        else {
            wed=false;
            wednesday.setBackgroundResource(R.drawable.day_pick_off);
            wednesday.setTextColor(getResources().getColor(R.color.white));
        }
        if(Arrays.asList(days).contains("thu")){
            thu=true;
            thursday.setBackgroundResource(R.drawable.day_pick_on);
            thursday.setTextColor(getResources().getColor(R.color.text_color));
        }
        else {
            thu=false;
            thursday.setBackgroundResource(R.drawable.day_pick_off);
            thursday.setTextColor(getResources().getColor(R.color.white));
        }
        if(Arrays.asList(days).contains("fri")){
            fri=true;
            friday.setBackgroundResource(R.drawable.day_pick_on);
            friday.setTextColor(getResources().getColor(R.color.text_color));
        }
        else {
            fri=false;
            friday.setBackgroundResource(R.drawable.day_pick_off);
            friday.setTextColor(getResources().getColor(R.color.white));
        }
        if(Arrays.asList(days).contains("sat")){
            sat=true;
            saturday.setBackgroundResource(R.drawable.day_pick_on);
            saturday.setTextColor(getResources().getColor(R.color.text_color));
        }
        else {
            sat=false;
            saturday.setBackgroundResource(R.drawable.day_pick_off);
            saturday.setTextColor(getResources().getColor(R.color.white));
        }

        if(timeModel.getHour()>=12){
            meredian.setText("PM");
            pm=true;
            am=false;
        }
        else {
            meredian.setText("AM");
            am=true;
            pm=false;
        }
    }

    private void tto12(int hour) {
        if(hour==0) timeModel.setHour(12);
        else if(hour>0 && hour<=12) timeModel.setHour(hour);
        else if(hour>12)timeModel.setHour(hour-12);
    }

    private String getDays() {

//        This method returns all the days user selected in a string by space seperate
        String day = "";
        if (sun) {
            days.add("sun");
        }
        if (mon) {
            days.add("mon");
        }
        if (tue) {
            days.add("tue");
        }
        if (wed) {
            days.add("wed");
        }
        if (thu) {
            days.add("thu");
        }
        if (fri) {
            days.add("fri");
        }
        if (sat) {
            days.add("sat");
        }
        for (int i = 0; i < days.size(); i++) {
            day = day + days.get(i) + " ";
        }
        return day;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void selectHour(View view){
        isHour=true;
        hour.setBackground(SetAlarmActivity.this.getDrawable(R.drawable.time_selector_highlighter));
        isMinute=false;
        initialiseCroller();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void selectMinute(View view){
        isMinute=true;
        minute.setBackground(SetAlarmActivity.this.getDrawable(R.drawable.time_selector_highlighter));
        isHour=false;
        initialiseCroller();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void pickDay(View view) {

//        This method simply toggle the day selected or not
        int id = view.getId();
        switch (id) {
            case R.id.sunday:
                if (sun) {
                    sun = false;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_off));
                    ((Button)view).setTextColor(this.getColor(R.color.white));
                } else {
                    sun = true;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_on));
                    ((Button)view).setTextColor(this.getColor(R.color.text_color_2));
                }
                break;
            case R.id.monday:
                if (mon) {
                    mon = false;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_off));
                    ((Button)view).setTextColor(this.getColor(R.color.white));
                } else {
                    mon = true;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_on));
                    ((Button)view).setTextColor(this.getColor(R.color.text_color_2));
                }
                break;
            case R.id.tuesday:
                if (tue) {
                    tue = false;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_off));
                    ((Button)view).setTextColor(this.getColor(R.color.white));
                } else {
                    tue = true;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_on));
                    ((Button)view).setTextColor(this.getColor(R.color.text_color_2));
                }
                break;
            case R.id.wednesday:
                if (wed) {
                    wed = false;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_off));
                    ((Button)view).setTextColor(this.getColor(R.color.white));

                } else {
                    wed = true;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_on));
                    ((Button)view).setTextColor(this.getColor(R.color.text_color_2));
                }
                break;
            case R.id.thursday:
                if (thu) {
                    thu = false;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_off));
                    ((Button)view).setTextColor(this.getColor(R.color.white));
                } else {
                    thu = true;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_on));
                    ((Button)view).setTextColor(this.getColor(R.color.text_color_2));
                }
                break;
            case R.id.friday:
                if (fri) {
                    fri = false;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_off));
                    ((Button)view).setTextColor(this.getColor(R.color.white));
                } else {
                    fri = true;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_on));
                    ((Button)view).setTextColor(this.getColor(R.color.text_color_2));
                }
                break;
            case R.id.saturday:
                if (sat) {
                    sat = false;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_off));
                    ((Button)view).setTextColor(this.getColor(R.color.white));
                } else {
                    sat = true;
                    view.setBackground(this.getDrawable(R.drawable.day_pick_on));
                    ((Button)view).setTextColor(this.getColor(R.color.text_color_2));
                }
                break;
        }
    }
    public void setMeredian(View view){
        if(am){
            am=false;
            pm=true;
            meredian.setText("PM");
        }
        else if(pm) {
            am=true;
            pm=false;
            meredian.setText("AM");
        }
    }
}