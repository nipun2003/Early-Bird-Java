package com.underdogdeveloper.earlybird.BroadCasts;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.underdogdeveloper.earlybird.DbHandler;
import com.underdogdeveloper.earlybird.Models.AlarmModel;

import java.util.Calendar;

public class OnRebootReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("REBOOT", "REBOOT RECEIVED");
        restoreOnBoot(context);
    }

    public void restoreOnBoot(Context context){
        DbHandler handler = new DbHandler(context);
        AlarmModel model = new AlarmModel();

        int i=0;
        while (true){
            model = handler.getAlarm(i+1);

            if (model.getSno() == 0) {
                break;
            } else {
                setAlarmOn(context, model);
                i++;
            }
        }
    }

    private void setAlarmOn(Context context, AlarmModel model) {
        int hour = model.getHour();
        int minute = model.getMinute();

        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context,AlarmReciever.class);
        intent.putExtra("days", model.getDays());

        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0,intent,0);

        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);

        // if alarm time has already passed, increment day by 1
        Calendar calendarNow=Calendar.getInstance();
        if(calendar.before(calendarNow)){
            calendar.add(Calendar.DATE,1);
        }

        if(model.getAlarmState()==1){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            }
            else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
            }
        }
    }
}
