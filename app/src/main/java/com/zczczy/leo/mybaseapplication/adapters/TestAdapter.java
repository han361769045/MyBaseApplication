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
        for (int i = (pageIndex - 1) * pageSize; i < pageSize*pageIndex; i++) {
            TestModel testModel = new TestModel();
            testModel.name = "测试" + i;
            result.Data.ListData.add(testModel);
        }
        afterGetMoreData(result);
    }

    @UiThread(delay = 0)
    void afterGetMoreData(BaseModelJson<PagerResult<TestModel>> result) {
        if (result == null) {
            result = new BaseModelJson<>();
        } else if (!result.Successful) {
//            AndroidTool.showToast(context, result.Error);
        } else {
            if (isRefresh) {
                clear();
            }
            setTotal(result.Data.RowCount);
            if (result.Data.ListData.size() > 0) {
                insertAll(result.Data.ListData, getItems().size());
            }
        }
        bus.post(result);
    }


    @Override
    protected View onCreateItemView(ViewGroup parent) {
        return TestItemView_.build(parent.getContext());
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
