package com.summer.mho.startloading;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sixth.adwoad.ErrorCode;
import com.sixth.adwoad.InterstitialAd;
import com.sixth.adwoad.InterstitialAdListener;
import com.summer.mho.R;
import com.summer.mho.application.MyApplication;
import com.summer.mho.base.BaseActivity;
import com.summer.mho.main.MainActivity;
import com.summer.mho.utils.DBHelper;

public class StartLoadingActivity extends BaseActivity implements InterstitialAdListener {

    private MyHandler myHandler = new MyHandler();

    @ViewInject(R.id.layout)
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_loading);
        ViewUtils.inject(this);
        showAD();
    }

    private void copyDB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);

                    DBHelper dbHelper = DBHelper.getInstance();

                    boolean isDBCopy = false;
                    boolean isDBOpen = false;

                    isDBCopy = dbHelper.copyDBIfNotExist();
                    isDBOpen = dbHelper.openDB();

                    Message msg = Message.obtain();

                    if (isDBCopy == true && isDBOpen == true) {
                        msg.what = 0;
                    } else {
                        msg.what = 1;
                    }
                    myHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    jump();
                    break;
                case 1:
                    Toast.makeText(MyApplication.getMyApplicationContext(), "数据库异常", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private InterstitialAd ad;
    public static byte FS_DESIRE_AD_FORM = 1;
    private String LOG_TAG = "Adwo full-screen ad";
    private void showAD() {
        ad = new InterstitialAd(this, "2c74632a76b44a2c83158e13c42b8c6b", true, this);
        // 设置全屏格式
        ad.setDesireAdForm(FS_DESIRE_AD_FORM);
        // 设置请求广告类型 可选。
        ad.setDesireAdType((byte) 0);
        // 开始请求全屏广告
        ad.prepareAd();
        Log.e(LOG_TAG, "onCreate");
    }
    @Override
    public void onReceiveAd() {
        Log.e(LOG_TAG, "onReceiveAd");
    }

    @Override
    public void onLoadAdComplete() {
        Log.e(LOG_TAG, "onLoadAdComplete");
        // 成功完成下载后，展示广告
        ad.displayAd();
    }

    @Override
    public void onFailedToReceiveAd(ErrorCode errorCode) {
        Log.e(LOG_TAG, "onFailedToReceiveAd");
        copyDB();
    }

    @Override
    public void onAdDismiss() {
        Log.e(LOG_TAG, "onAdDismiss");
        copyDB();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(LOG_TAG, "onDestroy");
        // 请在这里释放全屏广告资源
        if (ad != null) {
            ad.dismiss();
            ad = null;
        }
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 开始请求广告
        Log.e(LOG_TAG, "onAttachedToWindow");

    }

    @Override
    public void OnShow() {
        // TODO Auto-generated method stub
        Log.e(LOG_TAG, "OnShow");
    }


    private void jump() {
        Intent intent = new Intent(StartLoadingActivity.this, MainActivity.class);
        startActivity(intent);
        StartLoadingActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
    }
}
