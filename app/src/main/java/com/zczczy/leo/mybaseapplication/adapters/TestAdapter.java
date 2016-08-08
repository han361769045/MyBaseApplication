package com.zczczy.leo.mybaseapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.luleo.baselibrary.adapters.BaseUltimateRecyclerViewAdapter;
import com.luleo.baselibrary.listener.BaseOttoBus;
import com.luleo.baselibrary.model.BaseModelJson;
import com.luleo.baselibrary.model.PagerResult;
import com.zczczy.leo.mybaseapplication.items.TestItemView_;
import com.zczczy.leo.mybaseapplication.listener.OttoBus;
import com.zczczy.leo.mybaseapplication.model.TestModel;
import com.zczczy.leo.mybaseapplication.rest.MyRestClient;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

/**
 * Created by Leo on 2016/5/27.
 */
@EBean
public class TestAdapter extends BaseUltimateRecyclerViewAdapter<TestModel> {

    @RestService
    MyRestClient myRestClient;

    @Override
    public void getMoreData(int pageIndex, int pageSize, boolean isRefresh, Object... objects) {
        this.isRefresh = isRefresh;
        BaseModelJson<PagerResult<TestModel>> result =
                myRestClient.getGoodsInfoLikeWord(pageIndex, pageSize, "", "1");
        afterGetMoreData(result);
    }

    @Bean
    public void setBus(OttoBus bus) {
        this.bus = bus;
    }

    @Override
    protected View onCreateItemView(ViewGroup parent) {
        return TestItemView_.build(context);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

//    @Override
//    public long getItemId(int position) {
//        // return position; <- this is not stable!
//
//        // should returns stable value. IDs have to be kept the same value
//        // even after its position has been changed.
//        return getItems().get(position).id;
//    }
}
