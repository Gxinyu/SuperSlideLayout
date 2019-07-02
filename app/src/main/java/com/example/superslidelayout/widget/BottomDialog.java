package com.example.superslidelayout.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

/**
 * 如需修改动画、背景颜色、或者其他属性，请修改BottomDialogView
 * <p>
 * 并在此设置方法回调即可
 */
public class BottomDialog {

    private Context mContext;
    private int mHeight;//高度
    private Drawable mBackground;//背景
    private boolean mCancleOutside = true;
    private boolean mCancleBackpress = true;
    private BottomDialogView mBottomDialogView = null;
    private BottomDialogView.OnSlideWindowListener onSlideWindowListener;


    /**
     * 构造方法
     *
     * @param context
     */
    public BottomDialog(Context context) {
        mContext = context;
        mHeight = context.getResources().getDisplayMetrics().heightPixels * 9 / 16;
        mBackground = new ColorDrawable(0x99000000);
    }

    /**
     * 显示
     */
    public void show() {
        if (isShowing()) {
            return;
        }

        if (mContext instanceof Activity) {
            if (mBottomDialogView == null) {
                mBottomDialogView = new BottomDialogView(mContext);
            }
            mBottomDialogView.setHeight(mHeight);
            mBottomDialogView.setCancleBackpress(mCancleBackpress);
            mBottomDialogView.setCancleOutside(mCancleOutside);
            mBottomDialogView.setBackgroundWindowColor(mBackground);
            mBottomDialogView.attachWindow();
            if (onSlideWindowListener != null) {
                mBottomDialogView.setOnSlideWindowListener(onSlideWindowListener);
            }
        } else {
            throw new IllegalArgumentException("BottomDialog must attatch activity");
        }
    }

    /**
     * 消失
     */
    public void dismiss() {
        if (isShowing()) {
            mBottomDialogView.detacheWindow();
        }
    }

    /**
     * 点击外部是否可以取消
     *
     * @param cancleOutside
     */
    public void setCancleOutside(boolean cancleOutside) {
        this.mCancleOutside = cancleOutside;
    }

    /**
     * 点击返回键结束
     *
     * @param mCancleBackpress
     */
    public void setCancleBackpress(boolean mCancleBackpress) {
        this.mCancleBackpress = mCancleBackpress;
    }

    /**
     * 设置高度
     *
     * @param height
     */
    public void setHeight(int height) {
        mHeight = height;
    }

    /**
     * 设置背景
     *
     * @param mDrawable
     * @return
     */
    public void setBacground(Drawable mDrawable) {
        this.mBackground = mDrawable;
    }

    /**
     * 设置背景
     *
     * @param mResDrawable
     * @return
     */
    public void setBacground(@DrawableRes int mResDrawable) {
        Drawable drawable = mContext.getResources().getDrawable(mResDrawable);
        setBacground(drawable);
    }

    /**
     * 是否正在显示
     *
     * @return
     */
    public boolean isShowing() {
        return mBottomDialogView == null ? false : mBottomDialogView.isShowing();
    }

    /**
     * 设置滑动window的监听
     *
     * @param onSlideWindowListener
     */
    public void setOnSlideWindowListener(BottomDialogView.OnSlideWindowListener onSlideWindowListener) {
        this.onSlideWindowListener = onSlideWindowListener;
    }

}
