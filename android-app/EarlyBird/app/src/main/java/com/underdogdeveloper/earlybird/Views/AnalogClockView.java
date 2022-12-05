package com.underdogdeveloper.earlybird.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.underdogdeveloper.earlybird.R;

import java.util.Calendar;

public class AnalogClockView extends View {
    Context context;
    private int MAX_HEIGHT_WIDTH=1080;
    private int height,width=0;
    private int padding=0;
    private int handLength,hourhandLength,minuteHandLength=0;
    private int radius=0;
    private int numeralStart=0;
    private int circleStroke=0;
    private int numeralStroke=0;
    private int handStroke = 0;
    private Paint paint=new Paint();
    private boolean isInit;
    private Rect rect=new Rect();
    private int dialCircleColor,centerCircleCircle,hourHandColor,minuteHandColor,secondHandColor;
    public AnalogClockView(Context context) {
        super(context);
        this.context=context;
    }

    public AnalogClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

//        getting the input from xml tags
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AnalogClockView, 0, 0);
        try {
            dialCircleColor = (int) a.getColor(R.styleable.AnalogClockView_setDialColor, Color.parseColor("#ECF1F8"));
            centerCircleCircle = (int) a.getColor(R.styleable.AnalogClockView_setCenterCircleColor, Color.parseColor("#FF2525"));
            hourHandColor = (int) a.getColor(R.styleable.AnalogClockView_setHourHandColor, Color.parseColor("#FFC03D"));
            minuteHandColor = (int) a.getColor(R.styleable.AnalogClockView_setMinuteHandColor, Color.parseColor("#351C35"));
            secondHandColor = (int) a.getColor(R.styleable.AnalogClockView_setSecondHandColor, Color.parseColor("#FF2525"));
        } finally {
            a.recycle();
        }
    }

    public AnalogClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void initClock(){
//        Initialising the clock
        height=getHeight();
        width=getWidth();
//        Default padding
        int min=Math.min(height,width);
        padding=(Math.max(height,width))/16;
        numeralStart=min/33;
        radius=min/2-padding;
        circleStroke= (int) ((min)/8.1);
        numeralStroke= (int) ((min)/81);
        handStroke=(int)((min)/54);
        handLength=min/20;
        hourhandLength=min/6;
        minuteHandLength=min/15;
        isInit=true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isInit){
            initClock();
        }
        drawCircle(canvas);
        drawNumeral(canvas);
        drawHands(canvas);
        drawCenter(canvas);

        postInvalidateDelayed(500);
        invalidate();
    }

    private void drawHand(Canvas canvas,double loc,boolean isHour,boolean isMinute){
        double angle=Math.PI*loc/30-Math.PI/2;
        paint.setColor(getResources().getColor(android.R.color.white));
        paint.setStrokeWidth(handStroke);
        int reverseHandlength=Math.min(height,width)/17;
        paint.setStrokeCap(Paint.Cap.ROUND);
        int handRadius=0;
        if(isHour){
            handRadius=radius-handLength-hourhandLength;
            paint.setColor(hourHandColor);
        }
        else if(isMinute){
            handRadius=radius-handLength-minuteHandLength;
            paint.setColor(minuteHandColor);
        }
        else {
            handRadius=radius-handLength;
            paint.setColor(secondHandColor);
        }
        canvas.drawLine((width/2),(height/2),
                (float)(width/2+Math.cos(angle)*handRadius),
                (float)(height/2+Math.sin(angle)*handRadius),paint);
        canvas.drawLine((width/2),(height/2),
                (float)(width/2-Math.cos(angle)*reverseHandlength),
                (float)(height/2-Math.sin(angle)*reverseHandlength),paint);
    }
    private void drawHands(Canvas canvas) {
        paint.setStrokeWidth(handStroke);
        Calendar c=Calendar.getInstance();
        float hour=c.get(Calendar.HOUR_OF_DAY);
        hour=hour>12?hour-12:hour;
        drawHand(canvas,(hour+c.get(Calendar.MINUTE)/60)*5f,true,false);
        drawHand(canvas,c.get(Calendar.MINUTE),false,true);
        drawHand(canvas,c.get(Calendar.SECOND),false,false);
    }

    private void drawNumeral(Canvas canvas) {
        int handRadius=radius-handLength+numeralStroke;
        int endPos=radius+numeralStroke;
        paint.setStrokeWidth(numeralStroke);
        paint.setColor(Color.WHITE);
        for(int i=1;i<=60;i++){
            String tmp=String.valueOf(i);
            double angle=Math.PI/30*(i-15);
            if(i%15!=0 && i%5==0){
                paint.setColor(Color.rgb(140,140,115));
            }
            else if(i%15==0){
                paint.setColor(Color.BLACK);
            }
            else {
                paint.setColor(Color.WHITE);
            }
            if(i==60){
                handRadius=radius-handLength+numeralStroke;
                endPos=endPos+(Math.min(height,width)/55);
            }
            paint.getTextBounds(tmp,0,tmp.length(),rect);
            int x=(int)(width/2+Math.cos(angle)*radius-rect.width()/2);
            int y=(int)(height/2+Math.sin(angle)*radius+rect.height()/2);
            canvas.drawLine((float)(width/2+Math.cos(angle)*handRadius),
                    (float)(height/2+Math.sin(angle)*handRadius),
                    (float)(width/2+Math.cos(angle)*endPos),
                    (float)(height/2+Math.sin(angle)*endPos),paint);

        }
    }

    private void drawCenter(Canvas canvas) {
        paint.setStrokeWidth(numeralStroke);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(centerCircleCircle);
        canvas.drawCircle(width/2,height/2,height/40,paint);
    }

    private void drawCircle(Canvas canvas) {
        paint.reset();
        paint.setColor(dialCircleColor);
        paint.setStrokeWidth(circleStroke);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(width/2,height/2,radius,paint);
    }

    public int getHourHandColor() {
        return hourHandColor;
    }

    public void setHourHandColor(int hourHandColor) {
        this.hourHandColor = hourHandColor;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setWidth(int width) {
        this.width = width;
        invalidate();
        requestLayout();
    }
    public int getDialCircleColor() {
        return dialCircleColor;
    }

    public void setDialCircleColor(int dialCircleColor) {
        this.dialCircleColor = dialCircleColor;
        invalidate();
        requestLayout();
    }

    public int getCenterCircleCircle() {
        return centerCircleCircle;
    }

    public void setCenterCircleCircle(int centerCircleCircle) {
        this.centerCircleCircle = centerCircleCircle;
        invalidate();
        requestLayout();
    }

    public int getMinuteHandColor() {
        return minuteHandColor;
    }

    public void setMinuteHandColor(int minuteHandColor) {
        this.minuteHandColor = minuteHandColor;
        invalidate();
        requestLayout();
    }

    public int getSecondHandColor() {
        return secondHandColor;
    }

    public void setSecondHandColor(int secondHandColor) {
        this.secondHandColor = secondHandColor;
        invalidate();
        requestLayout();
    }
}
