package com.example.superslidelayout.sample.special;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.superslide.OnSlideListener;
import com.example.superslide.SlideLayoutImpl;
import com.example.superslide.SuperSlideLayout;
import com.example.superslidelayout.R;

public class SingleImgActivity extends AppCompatActivity implements OnSlideListener {

    private SuperSlideLayout superSlideview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ImmersiveTransitionTheme);
        setContentView(R.layout.activity_single_img);
        superSlideview = findViewById(R.id.super_slideview);
        superSlideview.setOnSlideListener(this);
    }

    @Override
    public void onSlideStart(SlideLayoutImpl slideLayout) {

    }

    @Override
    public void onTouchOutside(SlideLayoutImpl slideLayout, boolean touchIntChild) {

    }

    @Override
    public void onSlideChange(SlideLayoutImpl slideLayout, float hPercent, float vPercent, float scaleYPercent, float alphaPercent) {

    }

    @Override
    public void onSlideRecover(SlideLayoutImpl slideLayout, boolean overThreshold) {
        if(overThreshold) {
            onBackPressed();
        }
    }

    @Override
    public void onSlideFinish(SlideLayoutImpl slideLayout) {

    }
}
