package com.example.superslidelayout.sample.basis;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import com.example.superslidelayout.R;
import com.example.superslidelayout.adapter.GridAdapter;
import com.example.superslidelayout.base.BaseActivity;
import com.example.superslidelayout.helper.DataHelper;
import com.example.superslidelayout.helper.ListBean;
import com.example.superslidelayout.helper.RxBus;
import com.example.superslidelayout.helper.ScrollEvent;
import com.example.superslidelayout.sample.special.ShareElementActivity;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class GridActivity extends BaseActivity implements GridAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private GridAdapter adapter;
    private GridLayoutManager lm;
    private Bundle bundle;
    private int mBackIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        recyclerView = findViewById(R.id.recyclerView);
        List<ListBean> bitmapList = DataHelper.getBitmapList(this);
        adapter = new GridAdapter(this, bitmapList);
        adapter.setOnItemClickListener(this);
        //初始化布局管理器
        lm = new GridLayoutManager(this, 2);
        //设置布局管理器
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        RxBus.get().toFlowable(ScrollEvent.class).subscribe(new Consumer<ScrollEvent>() {
            @Override
            public void accept(ScrollEvent scrollEvent) throws Exception {
                //需要判断是否可见，如果不可见就需要滚动
                recyclerView.smoothScrollToPosition(scrollEvent.position);
            }
        });
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        bundle = new Bundle(data.getExtras());
        //由于当前Item可能不可见，需要滚动到可见位置
        mBackIndex = bundle.getInt("index", 0);
        if (Build.VERSION.SDK_INT >= 21) {
            setExitSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    if (bundle != null) {
                        sharedElements.clear();
                        names.clear();
                        View itemView = lm.findViewByPosition(mBackIndex);
                        ImageView imageView = itemView.findViewById(R.id.imageView);
                        sharedElements.put(mBackIndex + "", imageView);
                        bundle = null;
                    }
                }
            });
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, ShareElementActivity.class);
        intent.putExtra("index", position);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, view, position + "");// mAdapter.get(position).getUrl()
        startActivity(intent, options.toBundle());
    }
}
