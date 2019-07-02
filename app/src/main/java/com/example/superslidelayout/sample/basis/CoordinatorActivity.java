package com.example.superslidelayout.sample.basis;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.superslide.SlideBackKeeper;
import com.example.superslidelayout.R;
import com.example.superslidelayout.adapter.SimpleRecyclerAdapter;

public class CoordinatorActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private RecyclerView mRecyclerView;
    private AppBarLayout mAppBarlayout;
    private TextView tvVideoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ImmersiveTransitionTheme);
        setContentView(R.layout.activity_coordinator);

        //绑定activity
        SlideBackKeeper.getInstance()
                .createBuilder(this)
                .setSlideEdge(5)
                .attachActivity(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //监听折叠状态
        mAppBarlayout = findViewById(R.id.appBarlayout);
        mAppBarlayout.addOnOffsetChangedListener(this);
        tvVideoName = findViewById(R.id.tv_video_name);

        //设置列表项
        mRecyclerView = findViewById(R.id.recyclerView);
        SimpleRecyclerAdapter simpleRecyclerAdapter = new SimpleRecyclerAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(simpleRecyclerAdapter);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScrollRange = appBarLayout.getTotalScrollRange();
        float v = Math.abs(verticalOffset) / maxScrollRange;
        tvVideoName.setAlpha(1 - v);
    }
}