package com.example.superslidelayout.sample.special;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.superslide.OnSlideListener;
import com.example.superslide.SlideBackKeeper;
import com.example.superslide.SlideLayoutImpl;
import com.example.superslide.SuperSlideLayout;
import com.example.superslidelayout.R;
import com.example.superslidelayout.adapter.ImageAdapter;
import com.example.superslidelayout.helper.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements OnSlideListener {

    private List<ImageView> mImageList = new ArrayList();
    private SuperSlideLayout superSlideview;
    private RelativeLayout llAlphaView;
    private SlideLayoutImpl activityImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ImmersiveTheme);
        setContentView(R.layout.activity_gallery);
        llAlphaView = findViewById(R.id.ll_alpha);
        superSlideview = findViewById(R.id.super_slideview);
        superSlideview.setOnSlideListener(this);
        //绑定activity
        activityImpl = SlideBackKeeper.getInstance()
                .createBuilder(this)
                .setForeground(R.color.transprent)
                .setMultiPointerEnable(true)
                .setOnSlideListener(this)
                .attachActivity(this);

        ViewPager viewPager = findViewById(R.id.view_pager);
        List<Bitmap> singleLineBitmap = DataHelper.getSingleLineBitmap(this, 5);
        int size = singleLineBitmap.size();
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(singleLineBitmap.get(i));
            mImageList.add(imageView);
        }
        ImageAdapter imageAdapter = new ImageAdapter(mImageList);
        viewPager.setAdapter(imageAdapter);
    }

    @Override
    public void onSlideStart(SlideLayoutImpl slideLayout) {
        if (slideLayout == activityImpl) {
            activityImpl.setBackgroundResource(R.color.slide_bakground_color);
        } else if (slideLayout == superSlideview) {
            activityImpl.setBackgroundResource(R.color.transprent);
        }
    }

    @Override
    public void onTouchOutside(SlideLayoutImpl slideLayout, boolean touchIntChild) {

    }

    @Override
    public void onSlideChange(SlideLayoutImpl slideLayout, float hPercent, float vPercent, float scaleYPercent, float alphaPercent) {
        if (slideLayout == superSlideview) {
            llAlphaView.setAlpha(alphaPercent);
        }
    }

    @Override
    public void onSlideRecover(SlideLayoutImpl slideLayout, boolean b) {

    }

    @Override
    public void onSlideFinish(SlideLayoutImpl slideLayout) {
        if (slideLayout == superSlideview) {
            onBackPressed();
        }
    }

}
