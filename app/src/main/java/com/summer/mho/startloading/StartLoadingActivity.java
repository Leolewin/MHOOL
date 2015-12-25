package com.summer.mho.startloading;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.summer.mho.R;
import com.summer.mho.application.MyApplication;
import com.summer.mho.base.BaseActivity;
import com.summer.mho.main.MainActivity;
import com.summer.mho.utils.DBHelper;

public class StartLoadingActivity extends BaseActivity {

    private MyHandler myHandler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_loading);

        copyDB();
    }

    private void copyDB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);

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
                    Intent intent = new Intent(StartLoadingActivity.this, MainActivity.class);
                    startActivity(intent);
                    StartLoadingActivity.this.finish();
                    break;
                case 1:
                    Toast.makeText(MyApplication.getMyApplicationContext(), "数据库异常", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
