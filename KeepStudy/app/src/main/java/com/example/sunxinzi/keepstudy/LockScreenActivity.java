package com.example.sunxinzi.keepstudy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Method;

/**
 * Created by SunXinzi on 14/11/4.
 */

public class LockScreenActivity extends Activity {

    private static String TAG = "LockScreenActivity";
    private static int IS_FINISH = 1;

    private int hour;
    private int minute;
    protected static int screenHeight;
    private long first = 0, twice = 0, third = 0;
    TextView mClock;

    private SliderRelativeLayout sliderLayout = null;

    private ImageView imgView_getup_arrow; // 动画图片
    private AnimationDrawable animArrowDrawable;

    private Context mContext;

    public static int MSG_LOCK_SUCESS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = LockScreenActivity.this;
        /*设置全屏，无标题*/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.lock_screen);

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

    public class Counter extends CountDownTimer {

        public Counter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            first = millisUntilFinished / 1000;
            String time = null;
            if (first < 3600) {    //大于或等于一分钟，但小于一小时，显示分钟
                twice = first % 60;    //将秒转为分钟取余，余数为秒
                long mtmp = first / 60;    //将秒数转为分钟
                if (twice == 0) {
                    time = "00:" + (mtmp < 10 ? "0" + mtmp : mtmp) + ":00";
                    SendMessage(time);
                } else {
                    time = "00:" + (mtmp < 10 ? "0" + mtmp : mtmp) + ":" + (twice < 10 ? "0" + twice : twice);
                    SendMessage(time);
                }
            } else {
                twice = first % 3600;    //twice为余数 如果为0则小时为整数
                long mtmp = first / 3600;
                if (twice == 0) {
                    //只剩下小时
                    time = ("0" + first / 3600 + ":00:00");
                    SendMessage(time);
                } else {
                    if (twice < 60) {    //twice小于60 为秒
                        time = (mtmp < 10 ? "0" + mtmp : mtmp) + ":00:" + (twice < 10 ? "0" + twice : twice);
                        SendMessage(time);
                    } else {
                        third = twice % 60;    //third为0则剩下分钟 否则还有秒
                        long mtmp2 = twice / 60;
                        if (third == 0) {
                            time = (mtmp < 10 ? "0" + mtmp : mtmp) + ":" + (mtmp2 < 10 ? "0" + mtmp2 : mtmp2) + ":00";
                            SendMessage(time);
                        } else {
                            if (third > 9) {
                                time = (mtmp < 10 ? "0" + mtmp : mtmp) + ":" + (mtmp2 < 10 ? "0" + mtmp2 : mtmp2) + ":" + third;
                                SendMessage(time);
                            } else {
                                {
                                    time = (mtmp < 10 ? "0" + mtmp : mtmp) + ":" + (mtmp2 < 10 ? "0" + mtmp2 : mtmp2) + ":" + "0" + third;
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

        }
    }

    public void SendMessage(String time) {

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
                finish(); // 锁屏成功时，结束我们的Activity界面
        }
    };

    public class MyThread implements Runnable {

        @Override
        public void run() {

            log(TAG + "------————————————进入子线程");
            Looper.prepare();
            Counter counter = new Counter((hour * 3600 + minute * 60) * 1000, 1000);
            counter.start();
            Looper.loop();
        }
    }

    private Handler mTimerHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 1:
                    String time = (String) msg.obj;
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

    //屏蔽掉Back键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) return true ;
        else return super.onKeyDown(keyCode, event);
    }

    private static void log(String msg) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }
}