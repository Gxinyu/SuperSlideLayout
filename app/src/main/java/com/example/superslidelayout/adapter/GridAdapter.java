package com.example.superslidelayout.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.superslidelayout.R;
import com.example.superslidelayout.helper.ListBean;

import java.util.List;


public class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ListBean> dataList;
    private Context context;

    public GridAdapter(Context context, List<ListBean> dataList) {
        this.dataList = dataList;
        this.context = context;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //创建ViewHolder的时候要
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gridview, parent, false);
        return new VH(view);
    }

    //绑定数据
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ListBean item = dataList.get(position);
        VH vhHolder = (VH) holder;
        Glide.with(context)
                .load(item.bitmap)
                .into(vhHolder.imageView);
        vhHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null) {
                    onItemClickListener.onItemClick(view,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        ImageView imageView;

        public VH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
