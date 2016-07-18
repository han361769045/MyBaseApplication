package com.luleo.leolibrary.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.luleo.leolibrary.items.ItemView;
import com.marshalchen.ultimaterecyclerview.expanx.Util.BaseViewHolder;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2016/5/3.
 */
@EBean
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private List<T> items = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    private OnItemLongClickListener onItemLongClickListener;

    public VerticalAndHorizontal verticalAndHorizontal;

    @Background
    public abstract void getMoreData(Object... objects);

//    @RestService
//    MyRestClient myRestClient;
//
//    @App
//    MyApplication app;
//
//    @Pref
//    MyPrefs_ pre;

//    @StringRes
//    String no_net;
//
//    @Bean
//    MyErrorHandler myErrorHandler;
//
//    @AfterInject
//    void afterBaseInject() {
//        myRestClient.setRestErrorHandler(myErrorHandler);
//    }

    @RootContext
    Context context;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(onCreateItemView(parent, viewType));
    }

    /**
     * 设置 ItemView
     *
     * @param parent
     * @return
     */
    protected abstract View onCreateItemView(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, int position) {
        ItemView<T> itemView = (ItemView) viewHolder.itemView;
        itemView.init(items.get(position), this, viewHolder);
        setNormalClick(viewHolder);
    }

    /**
     * 设置普通itemclick事件
     *
     * @param viewHolder
     */
    private void setNormalClick(final BaseViewHolder viewHolder) {
        if (onItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(viewHolder, items.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
                }
            });
        }
        if (onItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(viewHolder, items.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public void insertAll(List<T> list, int position) {
        items.addAll(position, list);
        notifyItemInserted(position);
    }


    public void itemNotify(Object... objects) {

    }

    /**
     * Clear the list of the adapter
     */
    public void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void insertData(T t, int position) {
        items.add(position, t);
        notifyItemInserted(position);
    }

    public void deleteItem(T t, int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void deleteItemRange(List<T> t) {
        items.removeAll(t);
//        notifyItemRangeRemoved();
        notifyDataSetChanged();
    }


    public T getItemData(int position) {
        return items.size() < position + 1 ? null : items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    /**
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T> {

        void onItemClick(RecyclerView.ViewHolder viewHolder, T obj, int position);

    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemLongClickListener<T> {

        void onItemLongClick(RecyclerView.ViewHolder viewHolder, T obj, int position);
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public enum VerticalAndHorizontal {
        Vertical,
        Horizontal
    }
}
