package com.zczczy.leo.mybaseapplication.activities;

import com.zczczy.leo.mybaseapplication.R;
import com.zczczy.leo.mybaseapplication.adapters.TestAdapter;
import com.zczczy.leo.mybaseapplication.model.TestModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

/**
 * Created by Leo on 2016/5/27.
 */
@EActivity(R.layout.activity_ultimate_recycler_view)
public class TestActivity extends BaseUltimateRecyclerViewActivity<TestModel> {


    @Bean
    void setMyAdapter(TestAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    @AfterViews
    void afterView() {
    }


    @Override
    void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, 10, isRefresh);
    }
}
