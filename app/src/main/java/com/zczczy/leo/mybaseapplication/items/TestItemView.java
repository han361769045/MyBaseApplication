package com.zczczy.leo.mybaseapplication.items;

import android.content.Context;
import android.widget.TextView;

import com.zczczy.leo.mybaseapplication.R;
import com.zczczy.leo.mybaseapplication.model.TestModel;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Leo on 2016/5/27.
 */

@EViewGroup(R.layout.activity_test_item)
public class TestItemView extends ItemView<TestModel> {

    @ViewById
    TextView test;

    public TestItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {
        test.setText(_data.name);
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
