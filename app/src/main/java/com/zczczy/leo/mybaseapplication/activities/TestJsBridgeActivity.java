package com.zczczy.leo.mybaseapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.Button;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.Gson;
import com.luleo.baselibrary.activities.BaseActivity;
import com.zczczy.leo.mybaseapplication.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


/**
 * @author Created by LuLeo on 2016/8/30.
 *         you can contact me at :361769045@qq.com
 * @since 2016/8/30.
 */
@EActivity(R.layout.activity_test_js_bridge)
public class TestJsBridgeActivity extends BaseActivity {

    private final String TAG = "MainActivity";

    @ViewById
    BridgeWebView webView;

    @ViewById
    Button button;

    int RESULT_CODE = 0;

    ValueCallback<Uri> mUploadMessage;

    static class Location {
        String address;
    }

    static class User {
        String name;
        Location location;
        String testStr;
    }

    @AfterViews
    void afterView() {
        webView.setDefaultHandler(new DefaultHandler());

        webView.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                pickFile();
            }
        });

        webView.loadUrl("file:///android_asset/demo.html");

        webView.registerHandler("submitFromWeb", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
            }

        });

        User user = new User();
        Location location = new Location();
        location.address = "SDU";
        user.location = location;
        user.name = "大头鬼";

        webView.callHandler("functionInJs", new Gson().toJson(user), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {

            }
        });

        webView.send("hello");
    }

    @Click
    void button() {
        webView.callHandler("functionInJs", "data from Java", new CallBackFunction() {

            @Override
            public void onCallBack(String data) {
                // TODO Auto-generated method stub
                Log.i(TAG, "reponse data from js " + data);
            }

        });
    }


    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RESULT_CODE) {
            if (null == mUploadMessage) {
                return;
            }
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }


}
