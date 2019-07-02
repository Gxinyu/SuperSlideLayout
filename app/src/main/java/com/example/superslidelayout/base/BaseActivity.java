package com.example.superslidelayout.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.superslide.SlideBackKeeper;
import com.example.superslidelayout.Config;

/**
 * @author gexinyu
 */
public class BaseActivity extends AppCompatActivity {

    //统一加入
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定activity
        SlideBackKeeper.getInstance()
                .createBuilder(this)
                .setSlideEnable(Config.SLIDE_ENABLE)
                .setSlideEdge(Config.SLIDE_EDGE)
                .setSlideThresholdRate(Config.SLIDE_THRESHOLD_RATE)
                .setAlphaEnable(Config.ALPHA_ENABLE)
                .setAlphaRate(Config.ALPHA_RATE)
                .setMinAlpha(Config.ALPHA_MIN_SIZE)
                .setScaleEnable(Config.SCALE_ENABLE)
                .setScaleRate(Config.SCALE_RATE)
                .setMinScale(Config.SCALE_MIN_SIZE)
                .setOverflowParent(Config.SLIDE_OVER_PARENT)
                .setSingleDirection(Config.SLIDE_SINGLE_DIRECTION)
                .setMultiPointerEnable(Config.SLIDE_MULTI_TOUCH)
                .setScrollTime(Config.SCROLL_TIME)
                .setBacground(new ColorDrawable(0x99000000))
                .setForeground(new ColorDrawable(Color.WHITE))
                .setStatusBarBackground(new ColorDrawable(Color.TRANSPARENT))
                .attachActivity(this);
    }

}
