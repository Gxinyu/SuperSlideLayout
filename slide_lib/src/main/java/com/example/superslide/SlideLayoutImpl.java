package com.example.superslide;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * @author gexinyu
 */
public interface SlideLayoutImpl {

    /**
     * 禁用滑动
     *
     * @param mSlideEnable
     */
    void setSlideEnable(boolean mSlideEnable);

    /**
     * 是否禁用
     *
     * @return
     */
    boolean isSlideEnable();

    /**
     * 设置方向
     *
     * @param slideEdge
     */
    void setSlideEdge(int slideEdge);

    /**
     * 获取方向
     *
     * @return
     */
    int getSlideEdge();

    /**
     * 设置关闭阈值
     *
     * @param mSlideThresholdRate
     */
    void setSlideThresholdRate(float mSlideThresholdRate);

    /**
     * 获取阈值
     *
     * @return
     */
    float getSlideThresholdRate();

    /**
     * 背景透明
     *
     * @param mAlphaEnable
     */
    void setAlphaEnable(boolean mAlphaEnable);

    /**
     * 背景是否透明
     */
    boolean isAlphaEnable();

    /**
     * 背景透明度比例
     *
     * @param mBackgroundRate
     */
    void setAlphaRate(float mBackgroundRate);

    /**
     * 获取背景透明度比例
     */
    float getAlphaRate();

    /**
     * 设置最小的背景透明
     *
     * @param mMinAlpha
     */
    void setMinAlpha(float mMinAlpha);

    /**
     * 获取最小的背景透明
     */
    float getMinAlpha();

    /**
     * 设置背景缩放
     *
     * @param mScaleEnable
     */
    void setScaleEnable(boolean mScaleEnable);

    /**
     * 设置背景缩放
     *
     * @param mScaleEnable
     */
    boolean isScaleEnable(boolean mScaleEnable);

    /**
     * 缩放比例
     *
     * @param mScaleRate
     */
    void setScaleRate(float mScaleRate);

    /**
     * 获取缩放比例
     */
    float getScaleRate();

    /**
     * 设置最小的缩放
     *
     * @param mMinScale
     */
    void setMinScale(float mMinScale);

    /**
     * 获取最小的缩放
     */
    float getMinScale();

    /**
     * 单一方向
     *
     * @param mSingleDirection
     */
    void setSingleDirection(boolean mSingleDirection);

    /**
     * 单一方向
     */
    boolean isSingleDirection();

    /**
     * 是否超过父视图
     *
     * @param mOverflowParent
     */
    void setOverflowParent(boolean mOverflowParent);

    /**
     * 是否超过父视图
     */
    boolean isOverflowParent();

    /**
     * 是否支持多点触摸
     *
     * @param mMultiPointerEnable
     */
    void setMultiPointerEnable(boolean mMultiPointerEnable);

    /**
     * 是否支持多点触摸
     */
    boolean isMultiPointerEnable();

    /**
     * 设置松手后的滑动事件
     *
     * @param mScrollTime
     */
    void setScrollTime(int mScrollTime);

    /**
     * 设置松手后的滑动事件
     */
    int getScrollTime();

    /**
     * 设置背景
     *
     * @param background
     */

    void setBackground(Drawable background);

    void setBackgroundColor(@ColorInt int color);

    void setBackgroundResource(@DrawableRes int resid);

    /**
     * 获取背景
     *
     * @return
     */
    Drawable getBackground();

    /**
     * 设置前景
     *
     * @param foreground
     */

    void setForeground(Drawable foreground);

    void setForegroundColor(@ColorInt int color);

    void setForegroundResource(@DrawableRes int resid);

    /**
     * 获取前景
     *
     * @return
     */
    Drawable getForeground();

    /**
     * 设置状态栏
     *
     * @param bg
     */
    void setStatusBarBackground(@Nullable Drawable bg);

    void setStatusBarBackground(@DrawableRes int resId);

    void setStatusBarBackgroundColor(@ColorInt int color);

    /**
     * 获取系统状态栏
     *
     * @return
     */
    Drawable getStatusBarBackground();

    /**
     * 获取方向
     */
    int getDirection();


    /**
     * 滑进父类
     */
    void slideIn(int slideEdge, int distance);


    /**
     * 滑进父类
     */
    void slideIn(int slideEdge, int distance, int duration);

    /**
     * 滑出父类
     */
    void slideOut(int slideEdge, int distance);


    /**
     * 滑出父类
     */
    void slideOut(int slideEdge, int distance, int duration);


    /**
     * 重置
     */
    void reset();

    /**
     * 重置
     */
    void reset(int duration);

    /**
     * 滑动view
     *
     * @param view
     */
    void attachView(View view);

    /**
     * 滑动activity
     *
     * @param activity
     */
    void attachActivity(Activity activity);

}
