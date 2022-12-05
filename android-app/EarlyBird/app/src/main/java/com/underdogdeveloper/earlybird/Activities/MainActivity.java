package com.underdogdeveloper.earlybird.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.underdogdeveloper.earlybird.Adapters.AlarmAdapter;
import com.underdogdeveloper.earlybird.DbHandler;
import com.underdogdeveloper.earlybird.Models.AlarmModel;
import com.underdogdeveloper.earlybird.R;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //    Variable declaration from activity main file
//    ImageView dayNightSwitch;
    private ActionMode actionMode = null;
    ImageView addNewAlarm;
//    ImageView addNewAlarm;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ArrayList<AlarmModel> list=new ArrayList<>();
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView instructText;
    View header;
    int animate;
    int listSize;

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.appTheme);
        setContentView(R.layout.activity_main);

//        Variable initialisation
        initView();
        animate=getIntent().getIntExtra("animate",1);
        listSize=getIntent().getIntExtra("listS",-1);

//        Setting up action bar for mainActivity
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        Setting up the navigationView used in MainActivity
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

//        Initialising the adapter for recyclerView and setting that adapter

//        Getting all the alarms saved in database using sqliteDatabase
        showContent(animate);
//        handler.close();

//        Tap on addNewAlarm button simply create a new alarm with default value 6:30 AM with Everyday
        addNewAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                defaultAlarm=new AlarmModel(6,30,"sun mon tue wed thu fri sat",1);
//                DbHandler handler=new DbHandler(MainActivity.this);
//                handler.addAlarm(defaultAlarm);
                startActivity(new Intent(MainActivity.this,SetAlarmActivity.class).putExtra("sno",0).putExtra("listSize",list.size()));
//                showContent(true);
//                handler.close();
//                finish();
//                overridePendingTransition(0, 0);
//                startActivity(getIntent());
//                overridePendingTransition(0, 0);
            }
        });

        // implementing on click listener for day_night_switch
//        header = navigationView.getHeaderView(0);
//        dayNightSwitch = (ImageView) header.findViewById(R.id.day_night_switch);
//        dayNightSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("prafull", "hello succeddedc");
//            }
//        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && addNewAlarm.getVisibility() == View.VISIBLE) {
                    addNewAlarm.setVisibility(View.GONE);
                } else if (dy < 0 && addNewAlarm.getVisibility() != View.VISIBLE) {
                    addNewAlarm.setVisibility(View.VISIBLE);
                }
            }});


    }
    private void showContent(int animation){
            AlarmAdapter adapter = new AlarmAdapter(this, list);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            AlarmModel model = new AlarmModel();
            DbHandler handler = new DbHandler(this);
            list.clear();
            int i = 0;
            while (true) {
                model = handler.getAlarm(i + 1);
                if(listSize!=-1 && i==listSize){
                    model.setAnimate(1);
                }
                else {
                    model.setAnimate(animation);
                }
                if (model.getSno() == 0) {
                    break;
                } else {
                    list.add(model);
                    i++;
                }
            }
            Collections.reverse(list);
            if (list.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                instructText.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                instructText.setVisibility(View.GONE);
            }

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        switch (id){
            case R.id.mapOut: startActivity(new Intent(MainActivity.this,SetMapoutActivity.class));
                 break;
            case R.id.about:startActivity(new Intent(MainActivity.this, InfoActivity.class));
                 break;
            case R.id.settings:startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                 break;
            case R.id.share:
                try{
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    String message = "Can't Wake Up \n Improve your bedtime routine "+
                            "\n It is a smart AI powered Alarm which will stop once the you Start Brushing"+
                            "\n https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName();
                    shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                    shareIntent.setType("text/plain");
                    startActivity(Intent.createChooser(shareIntent, "Share with"));
                } catch (Exception e){

                }
                break;
            case R.id.nav_rate:
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());
                try{
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }catch (Exception e){

                }
                break;
        }
        return false;
    }


    private void initView(){
//        This method is used for initialising of all views using in related layout

        addNewAlarm = findViewById(R.id.addMoreAlarm);
//        dayNightSwitch = findViewById(R.id.day_night_switch);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolBarDash);
        recyclerView=findViewById(R.id.recyclerView);
        instructText=findViewById(R.id.instructText);
    }
}