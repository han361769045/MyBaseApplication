package com.zczczy.leo.mybaseapplication.activities;

import android.support.v7.widget.RecyclerView;

import com.luleo.baselibrary.activities.BaseUltimateRecyclerViewActivity;
import com.luleo.baselibrary.adapters.BaseUltimateRecyclerViewAdapter;
import com.zczczy.leo.mybaseapplication.R;
import com.zczczy.leo.mybaseapplication.adapters.TestAdapter;
import com.zczczy.leo.mybaseapplication.listener.OttoBus;
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

    @Bean
    void setOttoBus(OttoBus bus) {
        this.bus = bus;
    }


    @AfterViews
    void afterView() {
        myAdapter.setOnItemClickListener(new BaseUltimateRecyclerViewAdapter.OnItemClickListener<TestModel>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, TestModel obj, int position) {
                TestJsBridgeActivity_.intent(TestActivity.this).start();
            }

            @Override
            public void onHeaderClick(RecyclerView.ViewHolder viewHolder, int position) {

            }
        });

    }


    @Override
    public void afterLoadMore() {
        myAdapter.getMoreData(pageIndex, 10, isRefresh);
    }
}
