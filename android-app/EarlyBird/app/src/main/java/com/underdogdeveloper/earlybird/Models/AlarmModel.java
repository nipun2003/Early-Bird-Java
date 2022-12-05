package com.underdogdeveloper.earlybird.Models;

public class AlarmModel {
//    If alarmstate have value 1 that mean alarm is enabled and if 0 that mean alarm is disabled
    int sno,hour,minute,alarmState,animate=0;
    String days;



    public AlarmModel(int hour, int minute, String days, int alarmState ){
        this.hour = hour;
        this.minute = minute;
        this.days = days;
        this.alarmState=alarmState;
    }

    public int getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(int alarmState) {
        this.alarmState = alarmState;
    }

    public AlarmModel() {
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
    public void setAnimate(int animate) {
        this.animate = animate;
    }

    public int getAnimate() {
        return animate;
    }
}
