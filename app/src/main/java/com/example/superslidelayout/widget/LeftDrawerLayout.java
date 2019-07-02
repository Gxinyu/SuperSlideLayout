package com.example.superslidelayout.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author gexinyu
 */
public class LeftDrawerLayout extends DrawerLayout {

    private boolean canScrollH;
    private int mEdgeSize;
    private float mDownX;

    public LeftDrawerLayout(@NonNull Context context) {
        this(context, null);
    }

    public LeftDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        float density = context.getResources().getDisplayMetrics().density;
        mEdgeSize = (int) (20 * density + 0.5f);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                if (mDownX <= mEdgeSize) {
                    canScrollH = !isDrawerOpen(GravityCompat.START);
                } else {
                    canScrollH = false;
                }
                break;

        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        if (direction < 0) {
            return canScrollH;
        }
        return super.canScrollHorizontally(direction);
    }
}
