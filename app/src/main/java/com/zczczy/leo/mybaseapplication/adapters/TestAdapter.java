package com.zczczy.leo.mybaseapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zczczy.leo.mybaseapplication.items.TestItemView_;
import com.zczczy.leo.mybaseapplication.model.BaseModelJson;
import com.zczczy.leo.mybaseapplication.model.PagerResult;
import com.zczczy.leo.mybaseapplication.model.TestModel;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;

/**
 * Created by Leo on 2016/5/27.
 */
@EBean
public class TestAdapter extends BaseUltimateRecyclerViewAdapter<TestModel> {


    @Override
    public void getMoreData(int pageIndex, int pageSize, boolean isRefresh, Object... objects) {
        this.isRefresh = isRefresh;
        BaseModelJson<PagerResult<TestModel>> result = new BaseModelJson<>();
        result.Successful = true;
        result.Data = new PagerResult<>();
        result.Data.ListData = new ArrayList<>();
        result.Data.RowCount = 1000;
        for (int i = (pageIndex - 1) * pageSize; i < pageSize * pageIndex; i++) {
            TestModel testModel = new TestModel();
            testModel.id = i;
            testModel.name = "测试" + i;
            result.Data.ListData.add(testModel);
        }
        afterGetMoreData(result);
    }

    @Override
    protected View onCreateItemView(ViewGroup parent) {
        return TestItemView_.build(parent.getContext());
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public long getItemId(int position) {
        // return position; <- this is not stable!

        // should returns stable value. IDs have to be kept the same value
        // even after its position has been changed.
        return getItems().get(position).id;
    }
}
