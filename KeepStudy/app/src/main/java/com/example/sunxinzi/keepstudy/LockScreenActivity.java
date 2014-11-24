package com.example.sunxinzi.keepstudy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.Map;
import java.util.Objects;


/**
 * Created by SunXinzi on 14/11/4.
 */

public class LockScreenActivity extends Activity{

    private static String TAG = "LockScreenActivity";
    private static int IS_FINISH = 1;

    private int hour;
    private int minute;
    protected static int screenHeight;
    private long first = 0, twice = 0, third = 0;
    private TextView mClock;

    private SliderRelativeLayout sliderLayout = null;

    private ImageView imgView_getup_arrow; // 动画图片
    private AnimationDrawable animArrowDrawable;

    private Context mContext;

    private View main;

    public static int MSG_LOCK_SUCESS = 1;

    private DataBaseOpenHelper dbHelper;

    private static class TimeObj{
        private String time;
        private float timeLength;

        public TimeObj(String time,Float timeLength){
            this.time = time;
            this.timeLength = timeLength;
        }

        public String getTime(){return time;};
        public float getTimeLength(){return timeLength;};

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = LockScreenActivity.this;

        main=getLayoutInflater().inflate(R.layout.lock_screen,null);
        setContentView(main);

        //hide system button
        hidingSystemButton();

//        /*设置全屏，无标题*/
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//
//        setContentView(R.layout.lock_screen);

        mClock = (TextView) findViewById(R.id.textClock);
        mClock.setTextColor(Color.rgb(0, 245, 255));

        hour = this.getIntent().getExtras().getInt("hour");
        minute = this.getIntent().getExtras().getInt("minute");
        log("hour = " + hour + "\n" + "minute = " + minute);

        new Thread(new MyThread()).start();

        initViews();

        startService(new Intent(LockScreenActivity.this, LockScreenService.class));

        sliderLayout.setMainHandler(mHandler);

    }

    public boolean onTouchEvent(MotionEvent event) {
        onResume();
        return true;
    }

    public class Counter extends CountDownTimer {

        public Counter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            first = millisUntilFinished / 1000;
            TimeObj time;
            if (first < 3600) {    //大于或等于一分钟，但小于一小时，显示分钟
                twice = first % 60;    //将秒转为分钟取余，余数为秒
                long mtmp = first / 60;    //将秒数转为分钟
                if (twice == 0) {
                    String t = "00:" + (mtmp < 10 ? "0" + mtmp : mtmp) + ":00";
                    float t_ = mtmp/60; //convert minutes to hours
                    time = new TimeObj(t,t_);
                    SendMessage(time);
                } else {
                    String t = "00:" + (mtmp < 10 ? "0" + mtmp : mtmp) + ":" + (twice < 10 ? "0" + twice : twice);
                    float t_ = mtmp/60; //convert minutes to hours
                    time = new TimeObj(t,t_);
                    SendMessage(time);
                }
            } else {
                twice = first % 3600;    //twice为余数 如果为0则小时为整数
                long mtmp = first / 3600;
                if (twice == 0) {
                    //只剩下小时
                    String t = ("0" + first / 3600 + ":00:00");
                    float t_ = first / 3600;
                    time = new TimeObj(t,t_);
                    SendMessage(time);
                } else {
                    if (twice < 60) {    //twice小于60 为秒
                        String t = (mtmp < 10 ? "0" + mtmp : mtmp) + ":00:" + (twice < 10 ? "0" + twice : twice);
                        float t_ = mtmp;
                        time = new TimeObj(t,t_);
                        SendMessage(time);
                    } else {
                        third = twice % 60;    //third为0则剩下分钟 否则还有秒
                        long mtmp2 = twice / 60;
                        if (third == 0) {
                            String t = (mtmp < 10 ? "0" + mtmp : mtmp) + ":" + (mtmp2 < 10 ? "0" + mtmp2 : mtmp2) + ":00";
                            float t_ = mtmp+(mtmp2/60);
                            time = new TimeObj(t,t_);
                            SendMessage(time);
                        } else {
                            if (third > 9) {
                                String t = (mtmp < 10 ? "0" + mtmp : mtmp) + ":" + (mtmp2 < 10 ? "0" + mtmp2 : mtmp2) + ":" + third;
                                float t_ = mtmp+(mtmp2/60);
                                time = new TimeObj(t,t_);
                                SendMessage(time);
                            } else {
                                {
                                    String t = (mtmp < 10 ? "0" + mtmp : mtmp) + ":" + (mtmp2 < 10 ? "0" + mtmp2 : mtmp2) + ":" + "0" + third;
                                    float t_ = mtmp+(mtmp2/60);
                                    time = new TimeObj(t,t_);
                                    SendMessage(time);
                                }
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onFinish() {
            //when time down, we should insert study data to database
            // and then close the activity, call finish();
            updateStudyInf(hour + minute/60);
            finish();
        }
    }

    public void SendMessage(TimeObj time) {


        Message message = Message.obtain();
        message.obj = time;
        message.what = IS_FINISH;
        mTimerHandler.sendMessage(message);

    }

    private void initViews() {
        sliderLayout = (SliderRelativeLayout) findViewById(R.id.slider_layout);
        //获得动画，并开始转动
        imgView_getup_arrow = (ImageView) findViewById(R.id.getup_arrow);
        animArrowDrawable = (AnimationDrawable) imgView_getup_arrow.getBackground();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //hide system button
        hidingSystemButton();
        //设置动画
        mHandler.postDelayed(AnimationDrawableTask, 300);  //开始绘制动画
    }

    @Override
    protected void onPause() {
        super.onPause();
        animArrowDrawable.stop();
    }

    //通过延时控制当前绘制bitmap的位置坐标
    private Runnable AnimationDrawableTask = new Runnable() {

        public void run() {
            animArrowDrawable.start();
            mHandler.postDelayed(AnimationDrawableTask, 300);
        }
    };

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            log(TAG + "handleMessage :  #### ");

            if (MSG_LOCK_SUCESS == msg.what)
                //when we unlock the screen, we should update data
                //and close the Activity
                updateStudyInf(hour + minute/60);
                finish(); // 锁屏成功时，结束我们的Activity界面
        }
    };

    public class MyThread implements Runnable {

        @Override
        public void run() {

            log(TAG + "------————————————进入子线程");
            Looper.prepare();
            Counter counter = new Counter((hour * 3600 + minute * 60) * 100, 1000);
            counter.start();
            Looper.loop();
        }
    }

    private Handler mTimerHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 1:
                    String time = ((TimeObj) msg.obj).getTime();
                    mClock.setText(time);
            }
        }
    };

    //屏蔽掉Home键
    @Override
    public void onAttachedToWindow() {
        if(android.os.Build.VERSION.SDK_INT< android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);//À¯∆¡ ±œ‘ æµƒ∂‘ª∞øÚ
        }
        super.onAttachedToWindow();
    }
//
//    //屏蔽掉Back键
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) return true ;
//        else return super.onKeyDown(keyCode, event);
//    }

    private static void log(String msg) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }

    private void updateStudyInf(float studyLength){
        dbHelper = new DataBaseOpenHelper(this);
        SQLiteDatabase mDB = dbHelper.getWritableDatabase();
        try{
            mDB.execSQL("UPDATE " + DataBaseOpenHelper.STUDY_TABLE_NAME + " SET end_time= 'datetime()', study_time_length = " + studyLength + " WHERE _id= " +
                    "(SELECT _id FROM " + DataBaseOpenHelper.STUDY_TABLE_NAME + " LIMIT 1 order by _id desc);");
        }catch (android.database.SQLException e) {
            e.printStackTrace();
        }
        mDB.close();
    }

    private void hidingSystemButton(){
        main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

}