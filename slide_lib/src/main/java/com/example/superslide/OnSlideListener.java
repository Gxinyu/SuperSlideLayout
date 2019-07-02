package com.example.superslide;

/**
 * @author gexinyu
 */
public interface OnSlideListener {

    //这些参数都需要带上当前的view，用于动态设置属性
    void onSlideStart(SlideLayoutImpl slideLayout);

    void onTouchOutside(SlideLayoutImpl slideLayout, boolean touchIntChild);

    //恢复默认状态回调
    void onSlideChange(SlideLayoutImpl slideLayout, float hPercent, float vPercent, float scalePercent, float alphaPercent);

    //恢复默认状态回调
    void onSlideRecover(SlideLayoutImpl slideLayout, boolean overThreshold);

    //滑动结束
    void onSlideFinish(SlideLayoutImpl slideLayout);
}
