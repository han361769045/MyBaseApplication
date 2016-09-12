package com.zczczy.leo.mybaseapplication.activities;

import android.widget.Button;

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

    @ViewById
    Button button;

    @AfterViews
    void afterView() {

    }

    @Click
    void button() {
    }

}
