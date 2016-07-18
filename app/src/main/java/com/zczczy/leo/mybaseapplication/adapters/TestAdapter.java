package com.zczczy.leo.mybaseapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.mybaseapplication.items.TestItemView_;
import com.zczczy.leo.mybaseapplication.model.BaseModelJson;
import com.zczczy.leo.mybaseapplication.model.PagerResult;
import com.zczczy.leo.mybaseapplication.model.TestModel;

import org.androidannotations.annotations.EBean;

/**
 * Created by Leo on 2016/5/27.
 */
@EBean
public class TestAdapter extends BaseUltimateRecyclerViewAdapter<TestModel> {


    @Override
    public void getMoreData(int pageIndex, int pageSize, boolean isRefresh, Object... objects) {
        this.isRefresh = isRefresh;
        BaseModelJson<PagerResult<TestModel>> result =
                myRestClient.getGoodsInfoLikeWord(pageIndex, pageSize,"111", "1");
        afterGetMoreData(result);
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
