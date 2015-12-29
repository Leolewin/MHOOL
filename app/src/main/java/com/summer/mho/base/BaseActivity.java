package com.summer.mho.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.summer.mho.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.Stack;

/**
 * 包名      com.summer.mho.base
 * 类名      BaseActivity
 * 创建时间   2015/12/22
 * 创建人     Summer
 * 类描述
 * 所属类     {@link }
 */
public class BaseActivity extends AutoLayoutActivity {

    private static Stack<Activity> stack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 友盟推送
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();

        String deviceInfo =  getDeviceInfo(this);

        // 友盟统计集成测试开关
        MobclickAgent.setDebugMode(false);

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


    public static String getDeviceInfo(Context context) {
        try{
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if( TextUtils.isEmpty(device_id) ){
                device_id = mac;
            }

            if( TextUtils.isEmpty(device_id) ){
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
