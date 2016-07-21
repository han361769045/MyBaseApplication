package com.luleo.baselibrary.activities;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;


import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.StringRes;

/**
 * Created by Leo on 2016/4/27.
 */
@EActivity
public abstract class BaseActivity extends AppCompatActivity {

    @SystemService
    InputMethodManager inputMethodManager;

    @SystemService
    ConnectivityManager connectivityManager;

    @SystemService
    LayoutInflater layoutInflater;

    @ColorRes
    int line_color;

    @StringRes
    String no_net;
//
    @StringRes
    String empty_search, empty_order, empty_review, empty_no_review, empty_logistics;


    /**
     * 检查当前网络是否可用
     */
    public boolean isNetworkAvailable(Activity activity) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void finish() {
        closeInputMethod(this);
        super.finish();
    }


    //隐藏软键盘
    void closeInputMethod(Activity activity) {
        /*隐藏软键盘*/
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive()) {
                if (activity.getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                }
            }
        }
    }
}
