package com.underdogdeveloper.earlybird.BroadCasts;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.underdogdeveloper.earlybird.DbHandler;
import com.underdogdeveloper.earlybird.Models.AlarmModel;
import com.underdogdeveloper.earlybird.service.AlarmService;

import java.util.Arrays;
import java.util.Calendar;

public class AlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

//        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) ||
//                Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(intent.getAction())) {
//            Log.e("REBOOT", "REBOOT RECEIVED");
//            restoreOnBoot(context);
//        }else{
            if(alarmIsToday(intent)){
                startAlarmService(context);
            }
//        }

    }

    private boolean alarmIsToday(Intent intent){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        String days = intent.getStringExtra("days");
        String[] strArray = days.split(" ");
        Arrays.asList(strArray).contains("");

        switch(today) {
            case Calendar.SUNDAY:
                return Arrays.asList(strArray).contains("sun");
            case Calendar.MONDAY:
                return Arrays.asList(strArray).contains("mon");
            case Calendar.TUESDAY:
                return Arrays.asList(strArray).contains("tue");
            case Calendar.WEDNESDAY:
                return Arrays.asList(strArray).contains("wed");
            case Calendar.THURSDAY:
                return Arrays.asList(strArray).contains("thu");
            case Calendar.FRIDAY:
                return Arrays.asList(strArray).contains("fri");
            case Calendar.SATURDAY:
                return Arrays.asList(strArray).contains("sat");
        }

        return false;
    }

    public void startAlarmService(Context context){
        Intent intentService = new Intent(context, AlarmService.class);
        intentService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(intentService);
        }else {
            context.startService(intentService);
        }
    }

}