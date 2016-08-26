package com.luleo.baselibrary.activities;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.leo.lu.hfrefreshrecyclerview.CustomHFRefreshRecyclerView;
import com.leo.lu.hfrefreshrecyclerview.HFRefreshRecyclerView;
import com.leo.lu.hfrefreshrecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;
import com.leo.lu.hfrefreshrecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.luleo.baselibrary.R;
import com.luleo.baselibrary.adapters.BaseUltimateRecyclerViewAdapter;
import com.luleo.baselibrary.listener.BaseOttoBus;
import com.luleo.baselibrary.model.BaseModel;
import com.luleo.baselibrary.viewgroup.MyTitleBar;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * Created by Leo on 2016/5/21.
 */
@EActivity(resName = "activity_ultimate_recycler_view")
public abstract class BaseUltimateRecyclerViewActivity<T> extends BaseActivity {

    @ViewById(resName = "my_title_bar")
    public MyTitleBar myTitleBar;

    @ViewById(resName = "ultimate_recycler_view")
    public CustomHFRefreshRecyclerView ultimateRecyclerView;

    public BaseUltimateRecyclerViewAdapter<T> myAdapter;

    @ViewById(resName = "empty_view")
    public TextView empty_view;

    public BaseOttoBus bus;

    public int layoutId;

    public LinearLayoutManager linearLayoutManager;

    public GridLayoutManager gridLayoutManager;

    public int pageIndex = 1;

    public MaterialHeader materialHeader;

    public StoreHouseHeader storeHouseHeader;

    public boolean isRefresh;

    @AfterInject
    public void afterBaseInject() {
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
    }

    @AfterViews
    public void afterRecyclerView() {
        if (bus != null) {
            bus.register(this);
        }
        ultimateRecyclerView.setHasFixedSize(false);
        //设置 layoutManger
        setLayoutManager();

        if (layoutId > 0) {
            //设置视差header
            enableParallaxHeader(layoutId);
        }

        //设置空视图
        enableEmptyViewPolicy();

        //启用加载更多
        enableLoadMore();

        //获取数据
        afterLoadMore();

        //设置 Material下拉刷新
        refreshingMaterial();
//        refreshingStringArray();

//        ultimateRecyclerView.setItemViewCacheSize();
        setItemDecoration(35, 35);

        ultimateRecyclerView.setAdapter(myAdapter);
    }

    public void setItemDecoration(int leftMargin, int rightMargin) {
        paint.setStrokeWidth(1);
        paint.setColor(line_color);
        ultimateRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).margin(leftMargin, rightMargin).paint(paint).build());
    }

    /**
     * set layoutManager
     * you can  override
     */
    public void setLayoutManager() {
        verticalItem();
    }


    //线性布局
    public void verticalItem() {
        myAdapter.verticalAndHorizontal = BaseUltimateRecyclerViewAdapter.VerticalAndHorizontal.Vertical;
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);

    }

    //网格布局
    public void horizontalItem() {
        myAdapter.verticalAndHorizontal = BaseUltimateRecyclerViewAdapter.VerticalAndHorizontal.Horizontal;
        ultimateRecyclerView.setLayoutManager(gridLayoutManager);
    }

    public abstract void afterLoadMore();

    /**
     * 配置管理器
     *
     * @param rv
     */
    public void configLinearLayoutManager(HFRefreshRecyclerView rv) {
        ScrollSmoothLineaerLayoutManager mgm = new ScrollSmoothLineaerLayoutManager(this, LinearLayoutManager.VERTICAL, false, 300);
        rv.setLayoutManager(mgm);
    }

    public void configStaggerLayoutManager(HFRefreshRecyclerView rv) {
        StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(gaggeredGridLayoutManager);
    }

    /**
     * 设置 Material 下拉刷新
     */
    public void refreshingMaterial() {
        //启用刷新
        ultimateRecyclerView.setCustomSwipeToRefresh();
        materialHeader = new MaterialHeader(this);
        int[] colors = {Color.RED, Color.GRAY, Color.BLUE};

        materialHeader.setColorSchemeColors(colors);
        materialHeader.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        materialHeader.setPadding(0, 15, 0, 10);
        materialHeader.setPtrFrameLayout(ultimateRecyclerView.mPtrFrameLayout);
        ultimateRecyclerView.mPtrFrameLayout.removePtrUIHandler(storeHouseHeader);
        ultimateRecyclerView.mPtrFrameLayout.autoRefresh(false);
        ultimateRecyclerView.mPtrFrameLayout.setHeaderView(materialHeader);
        ultimateRecyclerView.mPtrFrameLayout.addPtrUIHandler(materialHeader);
        ultimateRecyclerView.mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                isRefresh = true;
                pageIndex = 1;
                afterLoadMore();
            }
        });
    }


    public void refreshingString() {
        ultimateRecyclerView.setCustomSwipeToRefresh();
        storeHouseHeader = new StoreHouseHeader(this);
        storeHouseHeader.initWithString("loading");
        ultimateRecyclerView.mPtrFrameLayout.removePtrUIHandler(materialHeader);
        ultimateRecyclerView.mPtrFrameLayout.setHeaderView(storeHouseHeader);
        ultimateRecyclerView.mPtrFrameLayout.addPtrUIHandler(storeHouseHeader);
        ultimateRecyclerView.mPtrFrameLayout.autoRefresh(false);
        ultimateRecyclerView.mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                isRefresh = true;
                pageIndex = 1;
                afterLoadMore();
            }
        });

    }

    @Subscribe
    public void notifyUI(BaseModel bm) {
        if (isRefresh) {
            linearLayoutManager.scrollToPosition(0);
            ultimateRecyclerView.mPtrFrameLayout.refreshComplete();
            ultimateRecyclerView.setRefreshing(false);
            isRefresh = false;
            if (myAdapter.getItems().size() < myAdapter.getTotal()) {
                if (!ultimateRecyclerView.isLoadMoreEnabled())
                    ultimateRecyclerView.reenableLoadmore();
            } else {
                if (ultimateRecyclerView.isLoadMoreEnabled())
                    ultimateRecyclerView.disableLoadmore();
            }
        } else if (pageIndex == 1) {
            linearLayoutManager.scrollToPosition(0);
        }
    }

    /**
     * 设置EmptyView
     */
    public void enableEmptyViewPolicy() {
        //  ultimateRecyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView.EMPTY_KEEP_HEADER_AND_LOARMORE);
        //    ultimateRecyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView.EMPTY_KEEP_HEADER);
        //  ultimateRecyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView.EMPTY_SHOW_LOADMORE_ONLY);
        ultimateRecyclerView.setEmptyView(R.layout.empty_view, HFRefreshRecyclerView.EMPTY_SHOW_LOADMORE_ONLY);
    }

    /**
     * 设置 启用 ParallaxHeader（视差header）
     * you can override
     */
    public void enableParallaxHeader(int layoutId) {
        View view = layoutInflater.inflate(layoutId, ultimateRecyclerView.mRecyclerView, false);
        ultimateRecyclerView.setParallaxHeader(view);
        ultimateRecyclerView.setOnParallaxScroll(new HFRefreshRecyclerView.OnParallaxScroll() {
            @Override
            public void onParallaxScroll(float percentage, float offset, View parallax) {

            }
        });
    }

    public void enableLoadMore() {
        ultimateRecyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        ultimateRecyclerView.setOnLoadMoreListener(new HFRefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                if (myAdapter.getItems().size() >= myAdapter.getTotal()) {
//                    AndroidTool.showToast(BaseUltimateRecyclerViewActivity.this, "没有更多的数据了！~");
                    ultimateRecyclerView.disableLoadmore();
                } else {
                    pageIndex++;
                    afterLoadMore();
                }
            }
        });
        ultimateRecyclerView.reenableLoadmore();
    }

    @Override
    public void finish() {
        bus.unregister(this);
        super.finish();
    }
}
