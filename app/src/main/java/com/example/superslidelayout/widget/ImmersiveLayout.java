package com.example.superslidelayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * @author gexinyu
 * 作用可以实现系统状态栏沉浸式体验
 */
public class ImmersiveLayout extends FrameLayout {

    //新增
    private WindowInsetsCompat mLastInsets;
    private boolean mDrawStatusBarBackground;
    private Drawable mStatusBarBackground;

    public ImmersiveLayout(@NonNull Context context) {
        this(context, null);
    }

    public ImmersiveLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImmersiveLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        overlayStatusBar(context);
    }

    /**
     * 下面三个方法主要用于处理状态栏
     */
    private void overlayStatusBar(Context context) {
        //获取系统默认状态栏颜色
        if (ViewCompat.getFitsSystemWindows(this)) {
            ViewCompat.setOnApplyWindowInsetsListener(this,
                    new android.support.v4.view.OnApplyWindowInsetsListener() {
                        @Override
                        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat insets) {
                            final ImmersiveLayout immersiveLayout = (ImmersiveLayout) view;
                            immersiveLayout.setChildInsets(insets, insets.getSystemWindowInsetTop() > 0);
                            return insets.consumeSystemWindowInsets();
                        }
                    });


            //版本高于21才能采用透明状态栏
            if(Build.VERSION.SDK_INT >= 21) {

                int[] THEME_STATUSBAR = {android.R.attr.statusBarColor};
                final TypedArray a = context.obtainStyledAttributes(THEME_STATUSBAR);
                try {
                    mStatusBarBackground = a.getDrawable(0);
                } finally {
                    a.recycle();
                }
                setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }
    }

    @RestrictTo(LIBRARY_GROUP)
    public void setChildInsets(WindowInsetsCompat insets, boolean draw) {
        mLastInsets = insets;
        mDrawStatusBarBackground = draw;
        setWillNotDraw(!draw && getBackground() == null);
        requestLayout();
    }

    /**
     * 绘制状态栏
     *
     * @param c
     */
    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        if (mDrawStatusBarBackground && mStatusBarBackground != null) {
            final int inset;
            if (Build.VERSION.SDK_INT >= 21) {
                inset = mLastInsets != null
                        ? ((WindowInsetsCompat) mLastInsets).getSystemWindowInsetTop() : 0;
            } else {
                inset = 0;
            }
            if (inset > 0) {
                mStatusBarBackground.setBounds(0, 0, getWidth(), inset);
                mStatusBarBackground.draw(c);
            }
        }
    }
}
