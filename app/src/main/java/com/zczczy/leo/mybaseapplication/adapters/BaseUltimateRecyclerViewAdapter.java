package com.zczczy.leo.mybaseapplication.adapters;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeItemManagerImpl;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeItemManagerInterface;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeLayout;
import com.zczczy.leo.mybaseapplication.items.BaseUltimateViewHolder;
import com.zczczy.leo.mybaseapplication.items.ItemView;
import com.zczczy.leo.mybaseapplication.listener.OttoBus;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.internal.ViewHelper;

/**
 * Created by Leo on 2016/5/27.
 */
@EBean
public abstract class BaseUltimateRecyclerViewAdapter<T> extends UltimateViewAdapter<BaseUltimateViewHolder> implements SwipeItemManagerInterface {

    public VerticalAndHorizontal verticalAndHorizontal;
    protected SwipeItemManagerImpl mItemManger = new SwipeItemManagerImpl(this);
    @Bean
    OttoBus bus;
    boolean isRefresh;
    private List<T> items = new ArrayList<>();
    private int total = 0;
    private boolean isFirstOnly = true;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mDuration = 300;
    private int mLastPosition = 5;
    private OnItemClickListener<T> onItemClickListener;
    private OnItemLongClickListener<T> onItemLongClickListener;

    /**
     * 获取更多的数据
     *
     * @param pageIndex
     * @param pageSize
     * @param objects
     */
    @Background
    public abstract void getMoreData(int pageIndex, int pageSize, boolean isRefresh, Object... objects);


    @Override
    public void onBindViewHolder(BaseUltimateViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPES.NORMAL) {
            ItemView<T> itemView = (ItemView) viewHolder.itemView;
            itemView.init(items.get(customHeaderView != null ? position - 1 : position), this, viewHolder);
            if (viewHolder.swipeLayout != null) {
                mItemManger.updateConvertView(viewHolder, position);
            }
            setNormalClick(viewHolder);
        } else if (getItemViewType(position) == VIEW_TYPES.HEADER) {
            setHeaderClick(viewHolder);
            onBindHeaderViewHolder(viewHolder, position);
        } else if (getItemViewType(position) == VIEW_TYPES.FOOTER) {

        }
        if (!isFirstOnly || position > mLastPosition) {
            for (Animator anim : getAdapterAnimations(viewHolder.itemView, AdapterAnimationType.ScaleIn)) {
                anim.setDuration(mDuration).start();
                anim.setInterpolator(mInterpolator);
            }
            mLastPosition = position;
        } else {
            ViewHelper.clear(viewHolder.itemView);
        }
    }

    @Override
    public BaseUltimateViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public BaseUltimateViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public BaseUltimateViewHolder onCreateViewHolder(ViewGroup parent) {

        final View view = onCreateItemView(parent);
        final BaseUltimateViewHolder baseViewHolder = new BaseUltimateViewHolder(view);
        SwipeLayout swipeLayout = baseViewHolder.swipeLayout;
        if (swipeLayout != null) {
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                @Override
                public void onDoubleClick(SwipeLayout layout, boolean surface) {

                }
            });
            swipeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SwipeLayout) v).close();
                }
            });
            swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOpenItems().get(0) != -1) {
                        closeItem(getOpenItems().get(0));
                        closeAllExcept(null);
                    } else {
                        view.performClick();
                    }
                }
            });
            swipeLayout.getSurfaceView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (getOpenItems().get(0) != -1) {
                        //closeItem(getOpenItems().get(0));
                        closeAllExcept(null);
                    } else {
                        view.performLongClick();
                    }
                    return false;
                }
            });
        }
        return baseViewHolder;
    }


    /**
     * 设置 ItemView
     *
     * @param parent
     * @return
     */
    protected abstract View onCreateItemView(ViewGroup parent);

    /**
     * allow resource layout id to be introduced
     *
     * @param mLayout res id
     */
    public void setCustomLoadMoreView(Context context, @LayoutRes int mLayout) {
        View h_layout = LayoutInflater.from(context).inflate(mLayout, null);
        setCustomLoadMoreView(h_layout);
    }

    /**
     * 设置普通itemclick事件
     *
     * @param viewHolder
     */
    private void setNormalClick(final BaseUltimateViewHolder viewHolder) {
        if (onItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOpenItems().get(0) != -1 && viewHolder.swipeLayout != null) {
                        //closeItem(getOpenItems().get(0));
                        closeAllExcept(null);
                    } else {
                        onItemClickListener.onItemClick(viewHolder, items.get(customHeaderView != null ? viewHolder.getAdapterPosition() - 1 : viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
                    }
                }
            });
        }
        if (onItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (getOpenItems().get(0) != -1 && viewHolder.swipeLayout != null) {
                        //closeItem(getOpenItems().get(0));
                        closeAllExcept(null);
                    } else {
                        onItemLongClickListener.onItemLongClick(viewHolder, items.get(customHeaderView != null ? viewHolder.getAdapterPosition() - 1 : viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 设置头部点击事件
     *
     * @param viewHolder
     */
    private void setHeaderClick(final BaseUltimateViewHolder viewHolder) {

        if (onItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOpenItems().get(0) != -1) {
                        //closeItem(getOpenItems().get(0));
                        closeAllExcept(null);
                    } else {
                        onItemClickListener.onHeaderClick(viewHolder, viewHolder.getAdapterPosition());
                    }
                }
            });
        }
        if (onItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (getOpenItems().get(0) != -1) {
                        //closeItem(getOpenItems().get(0));
                        closeAllExcept(null);
                    } else {
                        onItemLongClickListener.onHeaderLongClick(viewHolder, viewHolder.getAdapterPosition());
                    }
                    return false;
                }
            });
        }

    }


    /**
     * allow resource layout id to be introduced
     *
     * @param mLayout res id
     */
    public void setCustomLoadMoreView(@LayoutRes int mLayout) {
//        View h_layout = LayoutInflater.from(context).inflate(mLayout, null);
//        setCustomLoadMoreView(h_layout);
    }


    @Override
    public int getAdapterItemCount() {
        return items.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return -1;
    }

    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }


    @Override
    public SwipeItemManagerImpl.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(SwipeItemManagerImpl.Mode mode) {
        mItemManger.setMode(mode);
    }


    ///不实现
    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }


    /**
     * set adapter item click
     *
     * @param onItemClickListener onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void insertAll(List<T> list, int position) {
        items.addAll(position, list);
        if (customHeaderView != null) position++;
        notifyItemInserted(position);
    }

    /**
     * Clear the list of the adapter
     */
    public void clear() {
        int size = items.size();
        items.clear();
        total = 0;
        notifyItemRangeRemoved(customHeaderView != null ? 1 : 0, size);
    }

    public enum VerticalAndHorizontal {
        Vertical,
        Horizontal
    }

    public interface OnItemClickListener<T> {

        void onItemClick(RecyclerView.ViewHolder viewHolder, T obj, int position);

        void onHeaderClick(RecyclerView.ViewHolder viewHolder, int position);

    }

    public interface OnItemLongClickListener<T> {

        void onItemLongClick(RecyclerView.ViewHolder viewHolder, T obj, int position);

        void onHeaderLongClick(RecyclerView.ViewHolder viewHolder, int position);

    }
}
