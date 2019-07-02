package com.example.superslidelayout.sample.special;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.example.superslide.OnSlideListener;
import com.example.superslide.SlideBackKeeper;
import com.example.superslide.SlideLayoutImpl;
import com.example.superslide.SuperSlideLayout;
import com.example.superslidelayout.R;
import com.example.superslidelayout.adapter.SimpleRecyclerAdapter;

public class VideoDetailActivity extends AppCompatActivity implements OnSlideListener {

    private SlideLayoutImpl activityImpl;
    private LinearLayout llAlphaContent;
    private SuperSlideLayout superSlideview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ImmersiveTheme);
        setContentView(R.layout.activity_video_detail);
        superSlideview = findViewById(R.id.super_slideview);
        superSlideview.setOnSlideListener(this);
        llAlphaContent = findViewById(R.id.ll_alpha);
        //设置列表项
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        SimpleRecyclerAdapter simpleRecyclerAdapter = new SimpleRecyclerAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(simpleRecyclerAdapter);

        //绑定activity
        activityImpl = SlideBackKeeper.getInstance()
                .createBuilder(this)
                .setBacground(R.color.transprent)
                .setForeground(R.color.transprent)
                .setMultiPointerEnable(true)
                .setOnSlideListener(this)
                .attachActivity(this);
    }

    @Override
    public void onSlideStart(SlideLayoutImpl slideLayout) {

    }

    @Override
    public void onTouchOutside(SlideLayoutImpl slideLayout, boolean touchIntChild) {

    }

    @Override
    public void onSlideChange(SlideLayoutImpl slideLayout, float hPercent, float vPercent, float scalePercent, float alphaPercent) {
        if (slideLayout == superSlideview) {
            float v1 = (1 - alphaPercent) * 1000;
            v1 = v1 > 1 ? 1 : v1;
            llAlphaContent.setAlpha(1 - v1);
        }
    }

    @Override
    public void onSlideRecover(SlideLayoutImpl slideLayout, boolean overThreshold) {
        if (overThreshold) {
            onBackPressed();
        }
    }

    @Override
    public void onSlideFinish(SlideLayoutImpl slideLayout) {

    }
}
