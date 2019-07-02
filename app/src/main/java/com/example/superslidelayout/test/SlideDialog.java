package com.example.superslidelayout.test;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;

import com.example.superslide.SuperSlideLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author gexinyu
 */
public class SlideDialog {

    //滑进出屏幕的边缘(默认退出与进入相对，如果不特殊设置)
    public static final int EDGE_LEFT = SuperSlideLayout.EDGE_LEFT;
    public static final int EDGE_RIGHT = SuperSlideLayout.EDGE_RIGHT;
    public static final int EDGE_TOP = SuperSlideLayout.EDGE_TOP;
    public static final int EDGE_BOTTOM = SuperSlideLayout.EDGE_BOTTOM;

    @IntDef({EDGE_LEFT, EDGE_RIGHT, EDGE_TOP, EDGE_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SlideEdge {
    }

    //私有构造
    private SlideDialog() {
    }

    private static class SingleTonHoler {
        private static SlideDialog INSTANCE = new SlideDialog();
    }

    public static SlideDialog getInstance() {
        return SlideDialog.SingleTonHoler.INSTANCE;
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


    public final class Builder {

        public Context mContext;
        public int mSlideEdge;
        public @SlideEdge
        int mSlideIn, mSlideOut;//滑进和滑出边缘
        public int mGravity;//方位
        public int mWidth;//宽
        public int mHeight;//高
        public int mSideTime;//滑动时间
        public float mRadius;//圆角角度
        public Drawable mBackground;//背景
        public boolean mCancleOutside;
        public boolean mCancleBackPressed;

        //后续有宽高、圆角、方位、滑动时间、可滑动边缘


        Builder(Context context) {
            mContext = context;
            mBackground = new ColorDrawable(0x99000000);
        }

        /**
         * 设置可滑动的边缘
         *
         * @param slideEdge
         * @return
         */
        public Builder setSlideEdge(int slideEdge) {
            this.mSlideEdge = slideEdge;
            return this;
        }

        /**
         * 设置滑进的边缘
         *
         * @param slideIn
         * @return
         */
        public Builder setSlideIn(@SlideEdge int slideIn) {
            this.mSlideIn = slideIn;
            return this;
        }

        /**
         * 设置滑出边缘
         *
         * @param slideOut
         * @return
         */
        public Builder setSlideOut(@SlideEdge int slideOut) {
            this.mSlideOut = slideOut;
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
         * 设置方位
         *
         * @param mGravity
         */
        public Builder setGravity(int mGravity) {
            this.mGravity = mGravity;
            return this;
        }

        /**
         * 设置宽度
         *
         * @param mWidth
         */
        public Builder setWidth(int mWidth) {
            this.mWidth = mWidth;
            return this;
        }

        /**
         * 设置高度
         *
         * @param mHeight
         */
        public Builder setHeight(int mHeight) {
            this.mHeight = mHeight;
            return this;
        }


        /**
         * 设置圆角
         *
         * @param mRadius
         */
        public Builder setRadius(float mRadius) {
            this.mRadius = mRadius;
            return this;
        }


        /**
         * 设置滑动动画时间
         *
         * @param mSideTime
         */
        public Builder setSlideTime(int mSideTime) {
            this.mSideTime = mSideTime;
            return this;
        }


        /**
         * 点击外部消失
         *
         * @param cancleOutside
         * @return
         */
        public Builder setCancleOutside(boolean cancleOutside) {
            this.mCancleOutside = cancleOutside;
            return this;
        }

        /**
         * 点击返回键消失
         *
         * @param cancleBackPressed
         * @return
         */
        public Builder setCancleBackPressed(boolean cancleBackPressed) {
            this.mCancleBackPressed = cancleBackPressed;
            return this;
        }


    }
}
