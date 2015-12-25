package com.summer.mho.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.summer.mho.R;

import java.util.Stack;

/**
 * 包名      com.summer.mho.base
 * 类名      BaseActivity
 * 创建时间   2015/12/22
 * 创建人     Summer
 * 类描述
 * 所属类     {@link }
 */
public class BaseActivity extends AppCompatActivity {

    private static Stack<Activity> stack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (stack == null) {
            stack = new Stack<>();
        }

        if (stack.contains(this)) {
            for (int i = 0; i < stack.size(); i++) {
                if (stack.firstElement().equals(this)) {
                    break;
                } else {
                    stack.pop();
                }
            }
        } else {
            stack.push(this);
        }
    }

    @Override
    public void onDestroy() {
        if (stack.contains(this)) {
            stack.pop();
        }
        super.onDestroy();
    }

    /**
     * 退出App
     */
    public static void exitApp(Context context) {

        new AlertDialog.Builder(context)
                .setTitle(R.string.exit_app_title)
                .setMessage(R.string.exit_app_content)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        while (stack.iterator().hasNext()) {
                            Activity activity = stack.firstElement();
                            activity.finish();
                            stack.pop();

                            System.exit(0);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
