package com.underdogdeveloper.earlybird.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.underdogdeveloper.earlybird.Activities.ShowAlarmActivity;
import com.underdogdeveloper.earlybird.R;

public class AlarmService extends Service {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private AudioManager audioManager;
    int currentSound;
    private Handler volumeHandler;
    private Runnable runnable;
    private final int DELAY_INTERVAL = 2*1000;

    @Override
    public void onCreate(){
        super.onCreate();
        audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        currentSound=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
            mediaPlayer.setVolume(2,2);
        }
        mediaPlayer.setLooping(true);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        Intent notificationIntent = new Intent(Intent.ACTION_MAIN, null);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.setClass(this, ShowAlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, notificationIntent, 0);

        // creating notification channel for higher android version
        String CHANNEL_ID = "ALARM_SERVICE_CHANNEL";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alarm Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            serviceChannel.setLightColor(Color.BLUE);
            serviceChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(serviceChannel);
        }

        // setting notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, true)

                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Hey Buddy !")
                .setContentText("It's time to Brush")

                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build();



        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = Build.VERSION.SDK_INT >= 20 ? pm.isInteractive() : pm.isScreenOn(); // check if screen is on
        if (!isScreenOn) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "myApp:notificationLock");
            wl.acquire(3000); //set your time in milliseconds
        }

        // Use builder.addAction(..) to add buttons to answer or reject the call.
//        NotificationManager notificationManager = mContext.getSystemService(
//                NotificationManager.class);
//        notificationManager.notify(Integer.parseInt(CHANNEL_ID), notification);




        mediaPlayer.start();

        long[] pattern = { 0, 100, 1000};
        vibrator.vibrate(pattern, 0);

        // adjusting volume
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,2,0);
        volumeHandler = new Handler();
        runnable = new Runnable() {
            int currentVolume = 2;
            @Override
            public void run() {
                currentVolume++;
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
                volumeHandler.postDelayed(this, DELAY_INTERVAL);
            }
        };
        volumeHandler.post(runnable);


       // starting notification
        startForeground(1, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,currentSound,0);
        mediaPlayer.stop();
        vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}