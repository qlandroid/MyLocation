package cn.ql.location.change.adapter;

import android.content.Context;

import com.amap.api.services.help.Tip;

import java.util.List;

/**
 * Created by Administrator on 2017-4-13.
 */
public class TextAdapter extends CommonAdapter<Tip> {


    public TextAdapter(Context context, List<Tip> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void setItemContent(ViewHolder holder, Tip cityBean) {
        holder.setText(android.R.id.text1,cityBean.getName());
    }
}
