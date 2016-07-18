package com.zczczy.leo.mybaseapplication.items;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zczczy.leo.mybaseapplication.R;
import com.zczczy.leo.mybaseapplication.model.TestModel;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.springframework.util.StringUtils;

/**
 * Created by Leo on 2016/5/27.
 */

@EViewGroup(R.layout.goods_item_vertical)
public class TestItemView extends ItemView<TestModel> {

    @ViewById
    ImageView pic, img_add_cart;

    @ViewById
    TextView goods_name, goods_sell_count, goods_price, goods_bat_price, goods_delete_price;

    @StringRes
    String text_goods_sell_count, text_goods_price;

    @ViewById
    LinearLayout ll_bat_price, ll_price, ll_delete_price;

    public TestItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {
        if (!StringUtils.isEmpty(_data.GoodsImgSl)) {
            Glide.with(context).load(_data.GoodsImgSl).
                    centerCrop().into(pic);
        }

        goods_name.setText(_data.GodosName);
        goods_sell_count.setText(String.format(text_goods_sell_count, _data.GoodsXl));

        ll_bat_price.setVisibility(GONE);
        ll_delete_price.setVisibility(GONE);
        ll_price.setVisibility(VISIBLE);
        goods_price.setText(String.format(text_goods_price, _data.GoodsPrice));
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
