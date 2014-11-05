package com.example.sunxinzi.keepstudy;

/**
 * Created by SunXinzi on 14/11/4.
 */

/**
 * LockScreenActivity（锁屏主界面）
 * 1.onCreate:初始化主界面
 * 1)      隐藏状态栏让屏幕全屏显示：
 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);     （隐藏还是有问题，有解决方法的欢迎联系）
 * 2)      设置静态布局 main
 * 3)      InitViews（）初始化main中各种组件，包括sliderLayout，myClock（数字钟）以及layout_bed
 * 4)      设置sliderLayout的异步控制Handler，收到解锁成功信息后退出界面。
 */

import java.lang.reflect.Method;

import com.touchscreentest.MyApp;
import com.touchscreentest.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LockScreenActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private static String TAG = "DONLOCK";
    private SliderRelativeLayout sliderLayout = null;

    protected static int screenHeight = 0;
    TextView myClock;
    private RelativeLayout layout_bed = null;

    MyApp appState = null;

    @SuppressWarnings("deprecation")
    private KeyguardManager.KeyguardLock mKeyguardLock = null;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //屏蔽默认屏保
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);//当锁屏的时候，显示该window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置窗体全屏
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();

        setContentView(R.layout.lock_screen);

        // 初始化视图
        sliderLayout = (SliderRelativeLayout) findViewById(R.id.slider_layout);
        myClock = (TextView) findViewById(R.id.textClock);
        layout_bed = (RelativeLayout) findViewById(R.id.layout_bed);
        Log.i(TAG, "init views");

        //startService(new Intent(TestLockScreenActivity.this,donService.class));
        appState = (MyApp) getApplicationContext();
        sliderLayout.setMainHandler(mHandler);
        sliderLayout.setAppState(appState);
        //timeHandler.postDelayed(AMorPM, 100);
        KeyguardManager mKeyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        mKeyguardLock = mKeyguardManager.newKeyguardLock("myLockScreen");

        appState = (MyApp) getApplicationContext();
        new Thread() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                try {

                    while (true) {
                        Message msg = new Message();
                        msg.what = MSG_UPDATE_CLOCK;
                        mHandler.sendMessage(msg);
                        sleep(500);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // 解除系统锁屏
        mKeyguardLock.disableKeyguard();
    }


    @SuppressWarnings("deprecation")
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        // 开启系统锁屏
        mKeyguardLock.reenableKeyguard();
    }

    private static final int MSG_UPDATE_CLOCK = 100;
    public static final int MSG_LOCK_SUCESS = 101;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_CLOCK:
                    int sc = appState.workmode == 1 ? appState.SecondCount : appState.countdown;
                    myClock.setText(String.format("%02d:%02d:%02d",
                            sc / 3600, sc % 3600 / 60, sc % 60));
                    if (appState.workmode != 1 && appState.countdown == 0) {
                        finish();
                    }
                    break;
                case MSG_LOCK_SUCESS:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    // 设置禁用通知栏
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        try {
            Object service = getSystemService("statusbar");
            Class<?> claz = Class.forName("android.app.StatusBarManager");
            Method expand = claz.getMethod("collapse");
            expand.invoke(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    //屏蔽掉Home键 public
    public void onAttachedToWindow() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);//锁屏时显示的对话框
        }
        super.onAttachedToWindow();
    }

    //屏蔽掉Back键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) return true;
        else return super.onKeyDown(keyCode, event);
    }
}
