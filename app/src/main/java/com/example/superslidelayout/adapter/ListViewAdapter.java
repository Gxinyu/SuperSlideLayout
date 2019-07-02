package com.example.superslidelayout.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.superslidelayout.R;
import com.example.superslidelayout.helper.ListBean;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gexinyu
 */
public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private List<ListBean> list = new ArrayList();

    public ListViewAdapter(Context context, List<ListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ListHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_listview, null);  //将布局转换成视图
            holder = new ListHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            //ViewHolder被复用
            holder = (ListHolder) convertView.getTag();
        }
//        Glide.with(context)
//                .load(list.get(position).bitmap)
//                .apply(RequestOptions.bitmapTransform(new RoundedCorners(15)))
//                .into(holder.imageView);

        holder.imageView.setImageBitmap(list.get(position).bitmap);

        if (Build.VERSION.SDK_INT >= 21) {
            holder.imageView.setTransitionName("共享元素"+position);
        }

        return convertView;
    }

    private class ListHolder {
        ImageView imageView;
    }
}
