package com.leo.lu.hfrefreshrecyclerview.grid;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import com.leo.lu.hfrefreshrecyclerview.HFRefreshViewAdapter;

/**
 * Created by hesk on 24/8/15.
 */
public class BasicGridLayoutManager extends GridLayoutManager {
    private final HFRefreshViewAdapter mAdapter;
    protected int headerSpan = 2;


    protected SpanSizeLookup mSpanSizeLookUp = new SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            if (mAdapter.getItemViewType(position) == HFRefreshViewAdapter.VIEW_TYPES.FOOTER) {
                return getSpanCount();
            } else if (mAdapter.getItemViewType(position) == HFRefreshViewAdapter.VIEW_TYPES.HEADER) {
                return getSpanCount();
            } else
                return getNormalSpanCount(position);
        }
    };

    protected int getSpanInterval(int position) {
        int mIntervalHeader = getSpanCount() * 10;
        int h = position % mIntervalHeader == 0 ? getSpanCount() : 1;
        return h;

    }

    protected int getHeaderSpanCount(int n) {
        return headerSpan;
    }

    protected int getNormalSpanCount(int item_position) {
        return 1;
    }

    protected SpanSizeLookup decideSpanSizeCal() {
        return mSpanSizeLookUp;
    }

    public BasicGridLayoutManager(Context context, int spanCount, HFRefreshViewAdapter mAdapter) {
        super(context, spanCount);
        this.mAdapter = mAdapter;
        setSpanSizeLookup(decideSpanSizeCal());
    }

    public BasicGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout, HFRefreshViewAdapter mAdapter) {
        super(context, spanCount, orientation, reverseLayout);
        this.mAdapter = mAdapter;
        setSpanSizeLookup(decideSpanSizeCal());
    }
}
