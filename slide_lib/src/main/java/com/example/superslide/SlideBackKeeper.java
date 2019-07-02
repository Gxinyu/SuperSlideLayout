package com.example.superslide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

public class SlideBackKeeper {

    //私有构造
    private SlideBackKeeper() {
    }

    private static class SingleTonHoler {
        private static SlideBackKeeper INSTANCE = new SlideBackKeeper();
    }

    public static SlideBackKeeper getInstance() {
        return SingleTonHoler.INSTANCE;
    }


    /**
     * 创建构造
     *
     * @param context
     * @return
     */
    public Builder createBuilder(Context context) {
        Builder builder = new Builder(context);
        return builder;
    }

    //*****************************构造builder***********************

    public final class Builder {

        public Context mContext;
        public boolean mSlideEnable = true;//是否可以滚动
        public int mSlideEdge = 1;//方向（默认右滑）
        public float mSlideThresholdRate = 0.382f;
        public boolean mCheckThreshold = true;//是否需要判断阀值
        public boolean mAlphaEnable = true;//支持背景透明
        public float mAlphaRate = 0.5f;//背景透明度比例
        public float mMinAlpha = 0;//背景最小透明度
        public boolean mScaleEnable;//支持缩放
        public float mScaleRate = 0.5f;//缩放比例
        public float mMinScale = 0.1f;//最小缩放比例
        public boolean mOverflowParent;//越过父类边界
        public boolean mSingleDirection = true;//单一方向
        public boolean mMultiPointerEnable;//多点触摸支持
        public int mScrollTime = 350;//松手之后滑动时间
        public Drawable mBackground;//背景
        public Drawable mForeground;//前景
        public Drawable mStatusBarBackground;//系统栏背景
        public OnSlideListener mOnSlideListener;

        Builder(Context context) {
            mContext = context;
            //默认的颜色背景和前景
            mBackground = new ColorDrawable(0x99000000);
            mForeground = new ColorDrawable(Color.WHITE);
            mStatusBarBackground = new ColorDrawable(Color.TRANSPARENT);
        }

        /**
         * 禁用滑动
         *
         * @param mSlideEnable
         */
        public Builder setSlideEnable(boolean mSlideEnable) {
            this.mSlideEnable = mSlideEnable;
            return this;
        }

        /**
         * 设置方向
         *
         * @param slideEdge
         */
        public Builder setSlideEdge(@IntRange(from = 1, to = 15) int slideEdge) {
            this.mSlideEdge = slideEdge;
            return this;
        }

        /**
         * 设置关闭阈值
         *
         * @param mSlideThresholdRate
         */
        public Builder setSlideThresholdRate(@FloatRange(from = 0, to = 1) float mSlideThresholdRate) {
            this.mSlideThresholdRate = mSlideThresholdRate;
            return this;
        }

        /**
         * 设置是否监测阀值
         *
         * @param mCheckThreshold
         */
        public Builder setCheckThreshold(boolean mCheckThreshold) {
            this.mCheckThreshold = mCheckThreshold;
            return this;
        }

        /**
         * 设置背景
         *
         * @param mDrawable
         * @return
         */
        public Builder setBacground(Drawable mDrawable) {
            this.mBackground = mDrawable;
            return this;
        }

        /**
         * 设置背景
         *
         * @param mResDrawable
         * @return
         */
        public Builder setBacground(@DrawableRes int mResDrawable) {
            Drawable drawable = mContext.getResources().getDrawable(mResDrawable);
            this.mBackground = drawable;
            return this;
        }


        /**
         * 设置前景
         *
         * @param mDrawable
         * @return
         */
        public Builder setForeground(Drawable mDrawable) {
            this.mForeground = mDrawable;
            return this;
        }

        /**
         * 设置前景
         *
         * @param mResDrawable
         * @return
         */
        public Builder setForeground(@DrawableRes int mResDrawable) {
            Drawable drawable = mContext.getResources().getDrawable(mResDrawable);
            this.mForeground = drawable;
            return this;
        }

        /**
         * 设置状态栏
         *
         * @param bg
         */
        public Builder setStatusBarBackground(@Nullable Drawable bg) {
            mStatusBarBackground = bg;
            return this;
        }

        public Builder setStatusBarBackground(@DrawableRes int resId) {
            mStatusBarBackground = resId != 0 ? ContextCompat.getDrawable(mContext, resId) : null;
            return this;
        }


        /**
         * 禁用透明
         *
         * @param mAlphaEnable
         * @return
         */
        public Builder setAlphaEnable(boolean mAlphaEnable) {
            this.mAlphaEnable = mAlphaEnable;
            return this;
        }

        /**
         * 设置透明比率
         *
         * @param mAlphaRate
         */
        public Builder setAlphaRate(float mAlphaRate) {
            this.mAlphaRate = mAlphaRate;
            return this;
        }

        /**
         * 最小透明度
         *
         * @param mMinAlpha
         * @return
         */
        public Builder setMinAlpha(@FloatRange(from = 0, to = 1) float mMinAlpha) {
            this.mMinAlpha = mMinAlpha;
            return this;
        }

        /**
         * 禁用缩放
         *
         * @param mScaleEnable
         * @return
         */
        public Builder setScaleEnable(boolean mScaleEnable) {
            this.mScaleEnable = mScaleEnable;
            return this;
        }

        /**
         * 设置缩放比率
         *
         * @param mScaleRate
         */
        public Builder setScaleRate(float mScaleRate) {
            this.mScaleRate = mScaleRate;
            return this;
        }

        /**
         * 最小缩放比
         *
         * @param mMinScale
         * @return
         */
        public Builder setMinScale(@FloatRange(from = 0.1, to = 0.9) float mMinScale) {
            this.mMinScale = mMinScale;
            return this;
        }

        /**
         * 单一方向
         *
         * @param mSingleDirection
         * @return
         */
        public Builder setSingleDirection(boolean mSingleDirection) {
            this.mSingleDirection = mSingleDirection;
            return this;
        }

        /**
         * 是否超过父视图
         *
         * @param mOverflowParent
         * @return
         */
        public Builder setOverflowParent(boolean mOverflowParent) {
            this.mOverflowParent = mOverflowParent;
            return this;
        }

        /**
         * 是否多点触摸
         *
         * @param mMultiPointerEnable
         */
        public Builder setMultiPointerEnable(boolean mMultiPointerEnable) {
            this.mMultiPointerEnable = mMultiPointerEnable;
            return this;
        }

        /**
         * 设置滚动时间
         *
         * @param mScrollTime
         * @return
         */
        public Builder setScrollTime(int mScrollTime) {
            this.mScrollTime = mScrollTime;
            return this;
        }

        /**
         * 监听滑动
         *
         * @param mOnSlideListener
         * @return
         */
        public Builder setOnSlideListener(OnSlideListener mOnSlideListener) {
            this.mOnSlideListener = mOnSlideListener;
            return this;
        }

        /**
         * 贴附view
         *
         * @param view
         */
        public SlideLayoutImpl attachView(View view) {
            return attachTarget(this, view);
        }

        /**
         * 贴附activity
         *
         * @param activity
         */
        public SlideLayoutImpl attachActivity(Activity activity) {
            return attachTarget(this, activity);
        }

    }

    /**
     * 绑定目标
     *
     * @param builder
     * @param target
     */
    private SlideLayoutImpl attachTarget(Builder builder, Object target) {
        if (builder.mContext != null) {
            if (builder.mSlideEnable) {
                //设置公共参数
                SuperSlideLayout superSlideLayout = new SuperSlideLayout(builder.mContext);
                superSlideLayout.setSlideEnable(builder.mSlideEnable);
                superSlideLayout.setSlideEdge(builder.mSlideEdge);
                superSlideLayout.setSlideThresholdRate(builder.mSlideThresholdRate);
                superSlideLayout.setCheckThreshold(builder.mCheckThreshold);
                superSlideLayout.setAlphaEnable(builder.mAlphaEnable);
                superSlideLayout.setAlphaRate(builder.mAlphaRate);
                superSlideLayout.setMinAlpha(builder.mMinAlpha);
                superSlideLayout.setScaleEnable(builder.mScaleEnable);
                superSlideLayout.setScaleRate(builder.mScaleRate);
                superSlideLayout.setMinScale(builder.mMinScale);
                superSlideLayout.setOverflowParent(builder.mOverflowParent);
                superSlideLayout.setSingleDirection(builder.mSingleDirection);
                superSlideLayout.setMultiPointerEnable(builder.mMultiPointerEnable);
                superSlideLayout.setScrollTime(builder.mScrollTime);
                superSlideLayout.setBackground(builder.mBackground);
                superSlideLayout.setForeground(builder.mForeground);
                superSlideLayout.setStatusBarBackground(builder.mStatusBarBackground);
                superSlideLayout.setOnSlideListener(builder.mOnSlideListener);

                if (target instanceof View) {
                    superSlideLayout.attachView((View) target);
                } else if (target instanceof Activity) {
                    superSlideLayout.attachActivity((Activity) target);
                }

                return superSlideLayout;
            }
        }

        return null;
    }
}
