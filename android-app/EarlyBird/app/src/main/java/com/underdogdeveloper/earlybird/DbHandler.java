package com.underdogdeveloper.earlybird;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.underdogdeveloper.earlybird.Models.AlarmModel;

import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {
    private static final String dbName="alarmDb";
    private static final int version=1;

    public DbHandler(Context context){
        super(context,dbName,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

//        Creatting the alarm table in sql
        String sql="CREATE TABLE ALARM (sno INTEGER, HOUR INTEGER, MINUTE INTEGER, DAYS TEXT,ALARMSTATE INTEGER)";
        db.execSQL(sql);
    }
    public void addAlarm(AlarmModel alarm){

//        Insert the alarm data in sql database
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("sno", alarm.getSno());
        values.put("HOUR",alarm.getHour());
        values.put("MINUTE",alarm.getMinute());
        values.put("DAYS",alarm.getDays());
        values.put("ALARMSTATE",alarm.getAlarmState());
        long k=database.insert("ALARM",null,values);
        Log.d("MyTag",Long.toString(k));
        database.close();
    }

    //        get the alarm data from database
    public AlarmModel getAlarm(int sno){
        SQLiteDatabase database=this.getReadableDatabase();
        AlarmModel alarmModel=new AlarmModel();
        Cursor cursor=database.query("ALARM",new String[]{"sno,HOUR,MINUTE,DAYS,ALARMSTATE"},"sno=?",new String[]{String.valueOf(sno)},null,null,null);
        if(cursor!=null && cursor.moveToFirst()){
            alarmModel.setSno(cursor.getInt(0));
            alarmModel.setHour(cursor.getInt(1));
            alarmModel.setMinute(cursor.getInt(2));
            alarmModel.setDays(cursor.getString(3));
            alarmModel.setAlarmState(cursor.getInt(4));
        }
        return alarmModel;
    }

//    update the alarm in database
    public void updateAlarm(AlarmModel alarmModel){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("HOUR",alarmModel.getHour());
        values.put("MINUTE",alarmModel.getMinute());
        values.put("DAYS",alarmModel.getDays());
        values.put("ALARMSTATE",alarmModel.getAlarmState());
        database.update("ALARM",values,"sno=?",new String[]{String.valueOf(alarmModel.getSno())});
    }

//    delete the alarm from database
    public boolean deleteAlarm(String sno){
        SQLiteDatabase database = this.getWritableDatabase();

        long r=database.delete("ALARM", "sno=?", new String[]{sno});

        database.close();
        if(r<=0)
            return false;
        else
            return true;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // update sno of alarms
    public void updateSno(){
        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues values=new ContentValues();
//        long databaseCount = DatabaseUtils.queryNumEntries(database, "ALARM");
        ArrayList<AlarmModel> alarmModels = new ArrayList<>();
        int count=1;

        Cursor cursor = database.rawQuery("select * from ALARM", null);
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                AlarmModel alarmModel = new AlarmModel();
                alarmModel.setSno(count);
                alarmModel.setHour(cursor.getInt(1));
                alarmModel.setMinute(cursor.getInt(2));
                alarmModel.setDays(cursor.getString(3));
                alarmModel.setAlarmState(cursor.getInt(4));

                alarmModels.add(alarmModel);
                count++;
            }
            database.delete("ALARM", null, null);

            for(int i=0; i<alarmModels.size(); i++){
                addAlarm(alarmModels.get(i));
            }

        }

    }
}
