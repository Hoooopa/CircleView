package com.hoooopa.circleview;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.TimeZone;

public class CircleView extends View {

    //attr for background
    private int mColor ;
    private int mColorPressed ;
    //flag to control draw background
    private Boolean isPressed = false;
    //attr for text
    private int mTextColor;
    private int mTextSize;
    private String mText;
    //attr for time
    private int mTimeColor;
    private int mTimeSize;
    private String mTime = "00:00:00";
    //flag to control draw time : changes when get system's time
    private Boolean isFirst = true;
    //a thread to get System's time per second
    private TimeThread timeThread;

    private onCirleViewClickListenter onCirleViewClickListenter;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.CircleView);
        //Set color for background : unpressed or pressed
        mColor = a.getColor(R.styleable.CircleView_circle_color,Color.RED);
        mColorPressed = a.getColor(R.styleable.CircleView_circle_color_pressed,Color.BLUE);
        //Set text appearence : color, size and text
        mTextColor = a.getColor(R.styleable.CircleView_text_color,Color.WHITE);
        mTextSize = a.getDimensionPixelSize(R.styleable.CircleView_text_size,18);
        mText = a.getString(R.styleable.CircleView_text_content);
        //Set time appearence : color and size
        mTimeColor = a.getColor(R.styleable.CircleView_time_color,Color.WHITE);
        mTimeSize = a.getDimensionPixelSize(R.styleable.CircleView_time_size,18);
        a.recycle();
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * initialize thread : send msg to handler per second
     */
    private void init() {
        timeThread = new TimeThread();
        new Thread(timeThread).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width,height)/2;

        //draw background unpressed
        if (!isPressed){
            mPaint.setColor(mColor);
            canvas.drawCircle(width/2,height/2,radius,mPaint);

        }
        //draw background pressed
        if (isPressed){
            mPaint.setColor(mColorPressed);
            canvas.drawCircle(width/2,height/2,radius,mPaint);
        }
        /**
         * because mText don't have a default value,
         * so if you don't don't set "android:text_content" in xml,CircleView will give mText a default value "签到"
         */
        if (mText == null){
            mText = "签到";
        }

        //draw mText
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        float b = mPaint.measureText(mText);
        canvas.drawText(mText,width/2 - b/2,height*3/8,mPaint);

        //draw time
        if (!isFirst){
            mPaint.setColor(mTimeColor);
            mPaint.setTextSize(mTimeSize);
            float a = mPaint.measureText(mTime);
            canvas.drawText(mTime,width/2 - a/2,height * 6 /8,mPaint);
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                invalidate();
                if (onCirleViewClickListenter!=null){
                    onCirleViewClickListenter.onClick();
                }
                break;

            case MotionEvent.ACTION_UP:
                isPressed = false;
                invalidate();
                break;
            default:break;
        }
        return true;
    }

    /**
     * strongly proposal to use this fuc when you don't need CircleView anymore
     * 1. stop mHandler to release
     */
    public void stop(){

        mHandler.removeCallbacks(timeThread);
    }


    public void setOnCirleViewClickListenter(onCirleViewClickListenter onCirleViewClickListenter){
        this.onCirleViewClickListenter = onCirleViewClickListenter;
    }

    interface onCirleViewClickListenter{
        void onClick();
    }

    /**
     * Send msg to Handler per sencond
     */
    class TimeThread implements Runnable{

        @Override
        public void run() {
            Message msg = new Message();
            msg.what = 0;
            mHandler.sendMessage(msg);
            mHandler.postDelayed(this,1000); //send msg per sencond
        }
    }

    /**
     * 1. Handler will get system's time and set CircleView's time
     * 2. invalidate() CircleView to draw time
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    //Set CircleView's time
                    mTime = getTime();
                    isFirst = false;
                    invalidate();
                    break;
                    default:break;
            }
        }
    };

    /**
     * Get system's time
     * @return
     */
    public String getTime(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mMinute = String.valueOf(c.get(Calendar.MINUTE));
        String mSecond = String.valueOf(c.get(Calendar.SECOND));

        /**
         * 3 "if" below to modify string
         * For example : if the time is "13:5:1" , change it to "13:05:01"
         */
        if (mHour.length() == 1){
            mHour = "0" + mHour;
        }
        if (mMinute.length() == 1){
            mMinute = "0" + mMinute;
        }
        if (mSecond.length() == 1){
            mSecond = "0" + mSecond;
        }

        return mHour + ":" + mMinute + ":" + mSecond;
    }



}
