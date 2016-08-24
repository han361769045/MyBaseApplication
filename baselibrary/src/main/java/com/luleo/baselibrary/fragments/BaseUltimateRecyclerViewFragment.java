package com.luleo.baselibrary.fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.luleo.baselibrary.adapters.BaseUltimateRecyclerViewAdapter;
import com.luleo.baselibrary.listener.BaseOttoBus;
import com.luleo.baselibrary.model.BaseModel;
import com.luleo.baselibrary.viewgroup.MyTitleBar;
import com.marshalchen.ultimaterecyclerview.CustomUltimateRecyclerview;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * Created by Leo on 2016/5/21.
 */
@EFragment(resName = "activity_ultimate_recycler_view")
public abstract class BaseUltimateRecyclerViewFragment<T> extends BaseFragment {

    @ViewById
    public MyTitleBar myTitleBar;

    @ViewById
    public CustomUltimateRecyclerview ultimateRecyclerView;

    public BaseUltimateRecyclerViewAdapter<T> myAdapter;

    @ViewById
    public TextView empty_view;

    public BaseOttoBus bus;

    public LinearLayoutManager linearLayoutManager;

    public GridLayoutManager gridLayoutManager;

    public int pageIndex = 1;

    public MaterialHeader materialHeader;

    public StoreHouseHeader storeHouseHeader;

    public Paint paint = new Paint();

    public boolean isRefresh;

    @AfterViews
    public void afterRecyclerView() {
        bus.register(this);
        ultimateRecyclerView.setHasFixedSize(false);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        //设置 layoutManger
        verticalItem();

        //设置视差header
        enableParallaxHeader();

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

    public  void setItemDecoration(int leftMargin, int rightMargin) {
        paint.setStrokeWidth(1);
        paint.setColor(line_color);
        ultimateRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).margin(leftMargin, rightMargin).paint(paint).build());
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
//        ultimateRecyclerView.setAdapter(myAdapter);
    }

    public abstract void afterLoadMore();

    /**
     * 配置管理器
     *
     * @param rv
     */
    public void configLinearLayoutManager(UltimateRecyclerView rv) {
        ScrollSmoothLineaerLayoutManager mgm = new ScrollSmoothLineaerLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false, 300);
        rv.setLayoutManager(mgm);
    }

    public  void configStaggerLayoutManager(UltimateRecyclerView rv) {
        StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(gaggeredGridLayoutManager);
    }


    /**
     * 设置默认的 下拉刷新
     */
    public void defaultOnRefresh() {
        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                pageIndex = 1;
                afterLoadMore();
            }
        });
    }


    /**
     * 设置 Material 下拉刷新
     */
    public void refreshingMaterial() {
        //启用刷新
        ultimateRecyclerView.setCustomSwipeToRefresh();
        materialHeader = new MaterialHeader(getActivity());
        int[] colors = {Color.RED,Color.GRAY,Color.BLUE};
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
        storeHouseHeader = new StoreHouseHeader(getActivity());
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
//        ultimateRecyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView.EMPTY_SHOW_LOADMORE_ONLY);
    }


    /**
     * 设置 启用 ParallaxHeader（视差header）
     */
    public void enableParallaxHeader() {
//        ultimateRecyclerView.setParallaxHeader(getLayoutInflater().inflate(R.layout.parallax_recyclerview_header, ultimateRecyclerView.mRecyclerView, false));
//        ultimateRecyclerView.setOnParallaxScroll(new UltimateRecyclerView.OnParallaxScroll() {
//            @Override
//            public void onParallaxScroll(float percentage, float offset, View parallax) {
//
//            }
//        });
    }

    public void enableLoadMore() {
//        ultimateRecyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                if (myAdapter.getItems().size() >= myAdapter.getTotal()) {
//                    AndroidTool.showToast(BaseUltimateRecyclerViewActivity.this, "没有更多的数据了！~");
                    ultimateRecyclerView.disableLoadmore();
//                    myAdapter.notifyItemRemoved(itemsCount > 0 ? itemsCount - 1 : 0);
                } else {
                    pageIndex++;
                    afterLoadMore();
                }
            }
        });
        ultimateRecyclerView.reenableLoadmore();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            bus.unregister(this);
        } else {
            bus.register(this);
        }
    }
}
