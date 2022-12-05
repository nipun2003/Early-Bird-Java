package com.underdogdeveloper.earlybird.Adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.underdogdeveloper.earlybird.Activities.MainActivity;
import com.underdogdeveloper.earlybird.Activities.SetAlarmActivity;
import com.underdogdeveloper.earlybird.BroadCasts.AlarmReciever;
import com.underdogdeveloper.earlybird.DbHandler;
import com.underdogdeveloper.earlybird.Models.AlarmModel;
import com.underdogdeveloper.earlybird.R;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.viewHolder> {
    ActionMode actionMode = null;
    private MainActivity mainActivity;
    Context context;
    ArrayList<AlarmModel> list;
    int count = 0;
    boolean isSelected = false;
    ArrayList<Integer> selectedAlarmSno;

    public AlarmAdapter(Context context, ArrayList<AlarmModel> list) {
        this.context = context;
        this.list = list;
        this.mainActivity = (MainActivity)context;
        selectedAlarmSno = new ArrayList<>();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_active_alarm, parent, false);
        return new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

//        getting individual alarm from the database
        Boolean alarmon = false;
        AlarmModel model = list.get(position);
        String[] days = model.getDays().split(" ");

        if(model.getAnimate()==1)
        holder.container.setAnimation(AnimationUtils.loadAnimation(context,R.anim.pop_in));
        if (model.getAlarmState() == 1) {
            alarmon = true;
        } else {
            alarmon = false;
        }
        if (alarmon) {
            holder.alarmOff.setVisibility(View.INVISIBLE);
            holder.alarmOff.setClickable(false);

            if (days != null) {

                for(int i=0; i<days.length; i++){
                    switch (days[i]){
                        case "sun":
                            holder.sun.setBackground(context.getDrawable(R.drawable.show_days_circle_on));
                            break;
                        case "mon":
                            holder.mon.setBackground(context.getDrawable(R.drawable.show_days_circle_on));
                            break;
                        case "tue":
                            holder.tue.setBackground(context.getDrawable(R.drawable.show_days_circle_on));
                            break;
                        case "wed":
                            holder.wed.setBackground(context.getDrawable(R.drawable.show_days_circle_on));
                            break;
                        case "thu":
                            holder.thr.setBackground(context.getDrawable(R.drawable.show_days_circle_on));
                            break;
                        case "fri":
                            holder.fri.setBackground(context.getDrawable(R.drawable.show_days_circle_on));
                            break;
                        case "sat":
                            holder.sat.setBackground(context.getDrawable(R.drawable.show_days_circle_on));
                            break;
                    }
                }
            }
            showActiveAlarm(holder);
            setAlarmOn(model);

        } else {
            if (days != null) {

                for(int i=0; i<days.length; i++){
                    switch (days[i]){
                        case "sun":
                            holder.sun.setBackground(context.getDrawable(R.drawable.show_days_circle_off));
                            break;
                        case "mon":
                            holder.mon.setBackground(context.getDrawable(R.drawable.show_days_circle_off));
                            break;
                        case "tue":
                            holder.tue.setBackground(context.getDrawable(R.drawable.show_days_circle_off));
                            break;
                        case "wed":
                            holder.wed.setBackground(context.getDrawable(R.drawable.show_days_circle_off));
                            break;
                        case "thu":
                            holder.thr.setBackground(context.getDrawable(R.drawable.show_days_circle_off));
                            break;
                        case "fri":
                            holder.fri.setBackground(context.getDrawable(R.drawable.show_days_circle_off));
                            break;
                        case "sat":
                            holder.sat.setBackground(context.getDrawable(R.drawable.show_days_circle_off));
                            break;
                    }
                }
            }
            showNonActiveAlarm(holder);
        }

        showTime(model.getHour(), holder, model.getMinute());

        // handling single click on alarm
        holder.editAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSelected){
                    if(selectedAlarmSno.contains(model.getSno())){
                        holder.alarm_selected.setVisibility(View.GONE);
                        selectedAlarmSno.remove(Integer.valueOf(model.getSno()));
                    }else{
                        holder.alarm_selected.setVisibility(View.VISIBLE);
                        selectedAlarmSno.add(model.getSno());
                    }
                    if(selectedAlarmSno.size() == 0){
                        isSelected=false;
                        actionMode=null;
                    }
                }else {
                    Intent intent = new Intent(context, SetAlarmActivity.class);
                    intent.putExtra("sno", model.getSno());
                    context.startActivity(intent);
                }
            }
        });

        // handling long click on alarm
        holder.editAlarm.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedAlarmSno.clear();
                isSelected=true;
                mainActivity.startActionMode(mActionModeCallback);

                if(selectedAlarmSno.contains(model.getSno())){
                    holder.alarm_selected.setVisibility(View.GONE);
                    selectedAlarmSno.remove(Integer.valueOf(model.getSno()));
                }else{
                    holder.alarm_selected.setVisibility(View.VISIBLE);
                    selectedAlarmSno.add(model.getSno());
                }

                if(selectedAlarmSno.size() == 0){
                    isSelected=false;
                    actionMode=null;
                }

                return true;
            }
        });


        holder.alarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = new TranslateAnimation(0, 0, 0, 150);
                animation.setDuration(350);
                holder.alarmOn.startAnimation(animation);
                model.setAlarmState(0);
                DbHandler handler = new DbHandler(context);
                handler.updateAlarm(model);
//                handler.close();
                Handler handler1=new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (days != null) {

                            for(int i=0; i<days.length; i++){
                                switch (days[i]){
                                    case "sun":
                                        holder.sun.setBackground(context.getDrawable(R.drawable.show_days_circle_off));
                                        break;
                                    case "mon":
                                        holder.mon.setBackground(context.getDrawable(R.drawable.show_days_circle_off));
                                        break;
                                    case "tue":
                                        holder.tue.setBackground(context.getDrawable(R.drawable.show_days_circle_off));
                                        break;
                                    case "wed":
                                        holder.wed.setBackground(context.getDrawable(R.drawable.show_days_circle_off));
                                        break;
                                    case "thu":
                                        holder.thr.setBackground(context.getDrawable(R.drawable.show_days_circle_off));
                                        break;
                                    case "fri":
                                        holder.fri.setBackground(context.getDrawable(R.drawable.show_days_circle_off));
                                        break;
                                    case "sat":
                                        holder.sat.setBackground(context.getDrawable(R.drawable.show_days_circle_off));
                                        break;
                                }
                            }
                        }
                        showNonActiveAlarm(holder);
                    }
                },350);
                setAlarmOn(model);
            }
        });
        holder.alarmOff.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Animation animation = new TranslateAnimation(0, 0, 0, -150);
                animation.setDuration(350);
                holder.alarmOff.setAnimation(animation);

                holder.alarmOff.setVisibility(View.INVISIBLE);
                holder.alarmOff.setClickable(false);
                model.setAlarmState(1);
                DbHandler handler = new DbHandler(context);
                handler.updateAlarm(model);
//                handler.close();
                Handler handler1=new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (days != null) {

                            for(int i=0; i<days.length; i++){
                                switch (days[i]){
                                    case "sun":
                                        holder.sun.setBackground(context.getDrawable(R.drawable.show_days_circle_on));
                                        break;
                                    case "mon":
                                        holder.mon.setBackground(context.getDrawable(R.drawable.show_days_circle_on));
                                        break;
                                    case "tue":
                                        holder.tue.setBackground(context.getDrawable(R.drawable.show_days_circle_on));
                                        break;
                                    case "wed":
                                        holder.wed.setBackground(context.getDrawable(R.drawable.show_days_circle_on));
                                        break;
                                    case "thu":
                                        holder.thr.setBackground(context.getDrawable(R.drawable.show_days_circle_on));
                                        break;
                                    case "fri":
                                        holder.fri.setBackground(context.getDrawable(R.drawable.show_days_circle_on));
                                        break;
                                    case "sat":
                                        holder.sat.setBackground(context.getDrawable(R.drawable.show_days_circle_on));
                                        break;
                                }
                            }
                        }
                        showActiveAlarm(holder);
                    }
                },350);
                setAlarmOn(model);
            }
        });
    }

    private void setAlarmOn(AlarmModel model) {
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
//                alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP,
//                        calendar.getTimeInMillis(),
//                        pendingIntent
//                );
                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), null), pendingIntent);
            }
            else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
            }

            Log.e("praf", "alarm set for sno = "+model.getSno()+" at "+hour+":"+minute);
        }
        else {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(context, "Alarm turned off", Toast.LENGTH_SHORT).show();
            Log.e("praf", "sno = "+model.getSno()+" cancelled");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showActiveAlarm(viewHolder holder) {
        holder.editAlarm.setBackground(context.getDrawable(R.drawable.active_alarm_bg));
        holder.showTime.setTextColor(context.getColor(R.color.text_color_2));
        holder.timeAmPm.setTextColor(context.getColor(R.color.Amcolor));

        holder.alarmOn.setVisibility(View.VISIBLE);
        holder.alarmOn.setClickable(true);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showNonActiveAlarm(viewHolder holder) {
//        This method is used to show non active alarm
        holder.editAlarm.setBackground(context.getDrawable(R.drawable.non_active_alarm_bg));
        holder.timeAmPm.setTextColor(context.getColor(R.color.Am_pm_off));
        holder.showTime.setTextColor(context.getColor(R.color.daysShowTextColoroff));

        holder.alarmOn.setVisibility(View.INVISIBLE);
        holder.alarmOn.setClickable(false);

        holder.alarmOff.setVisibility(View.VISIBLE);
        holder.alarmOff.setClickable(true);

    }

    private void showTime(int hour, viewHolder holder, int minute) {
        String strHour, strMinute;
        if (hour < 12) {
            hour = hour;
            holder.timeAmPm.setText("AM");
        } else if (hour == 12) {
            hour = hour;
            holder.timeAmPm.setText("PM");
        } else {
            hour = hour - 12;
            holder.timeAmPm.setText("PM");
        }
        if (hour < 10 && hour > 0) {
            strHour = "0" + String.valueOf(hour);
        } else if (hour == 0) {
            strHour = "12";
        } else {
            strHour = String.valueOf(hour);
        }
        if (minute < 10) {
            strMinute = "0" + String.valueOf(minute);
        } else {
            strMinute = String.valueOf(minute);
        }
        holder.showTime.setText(strHour + ":" + strMinute);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        DbHandler handler;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.on_long_selected, menu);
            handler = new DbHandler(context.getApplicationContext());
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.on_long_delete:
                    for(int i=0; i<selectedAlarmSno.size(); i++){
                        boolean isDeleted = handler.deleteAlarm(selectedAlarmSno.get(i).toString());
//                        if(isDeleted){
//                        }
                    }
                    selectedAlarmSno.clear();
                    mode.finish();
                    isSelected = false;
                    return true;

                default:
                    return false;

            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode=null;
            isSelected = false;
            selectedAlarmSno.clear();
            handler.updateSno();
            handler.close();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);

        }

    };

    public class viewHolder extends RecyclerView.ViewHolder {
        FrameLayout editAlarm;
        TextView timeAmPm, showTime;
        ImageView alarm_selected;
        TextView sun, mon, tue, wed, thr, fri, sat;
        Button alarmOn, alarmOff;
        ConstraintLayout container;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            editAlarm = itemView.findViewById(R.id.editAlarm);
            timeAmPm = itemView.findViewById(R.id.timeAmPm);
            showTime = itemView.findViewById(R.id.showTime);
            alarmOn = itemView.findViewById(R.id.alarmOn);
            alarmOff = itemView.findViewById(R.id.alarmOff);
            container=itemView.findViewById(R.id.containerAlarmShow);

            sun = itemView.findViewById(R.id.day_sun);
            mon = itemView.findViewById(R.id.day_mon);
            tue = itemView.findViewById(R.id.day_tue);
            wed = itemView.findViewById(R.id.day_wed);
            thr = itemView.findViewById(R.id.day_thr);
            fri = itemView.findViewById(R.id.day_fri);
            sat = itemView.findViewById(R.id.day_sat);

            alarm_selected = itemView.findViewById(R.id.alarm_selected);
        }
    }
}