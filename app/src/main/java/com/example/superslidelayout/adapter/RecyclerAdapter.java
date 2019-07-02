package com.example.superslidelayout.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.superslidelayout.R;
import com.example.superslidelayout.helper.ListBean;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gexinyu
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<ListBean> list = new ArrayList<>();

    public RecyclerAdapter(Context mContext, List<ListBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_listview, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(mContext)
                .load(list.get(position).bitmap)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(15)))
                .into(holder.iamgeView);
    }

    //防止刷新闪烁问题
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iamgeView;
        public ViewHolder(View itemView) {
            super(itemView);
            iamgeView = itemView.findViewById(R.id.imageView);
        }
    }


}
