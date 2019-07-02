package com.example.superslidelayout.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.superslide.OnSlideListener;
import com.example.superslide.SlideLayoutImpl;
import com.example.superslide.SuperSlideLayout;
import com.example.superslidelayout.R;
import com.example.superslidelayout.adapter.CommentAdapter;

public class BottomDialogView extends FrameLayout implements OnSlideListener {

    protected Context mContext;
    protected ViewGroup mDecorView;
    private RecyclerView mRecyclerView;
    protected SuperSlideLayout superSlideLayout;
    //属性
    protected int mAnimationTime = 360;
    protected boolean mCancleOutside = true;
    protected boolean mCancleBackpress = true;
    protected boolean mIsShowing;//显示状态
    protected boolean mIsSliding;//正在滑动
    protected boolean mIsDismissing;//消失状态
    protected OnSlideWindowListener onSlideWindowListener;
    private LinearLayout llContent;

    public BottomDialogView(@NonNull Context context) {
        this(context, null);
    }

    public BottomDialogView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setClickable(true);
        setFocusable(true);
        View mChildView = LayoutInflater.from(context).inflate(R.layout.custom_comment, this);
        mRecyclerView = mChildView.findViewById(R.id.recyclerView);
        superSlideLayout = mChildView.findViewById(R.id.ssl_viewgroup);
        llContent = findViewById(R.id.ll_content);
        superSlideLayout.setOnSlideListener(this);
        mDecorView = (ViewGroup) ((Activity) mContext).getWindow().getDecorView();
        int width = context.getResources().getDisplayMetrics().heightPixels;
        //注入假数据
        setHeight(llContent, width * 9 / 16);
        injectData();
    }

    /**
     * 动态设置view的高度
     *
     * @param view
     * @param height
     */
    private void setHeight(View view, int height) {
        //检测不能超越屏幕
        int heightPixels = getResources().getDisplayMetrics().heightPixels;
        if (height > heightPixels) {
            height = heightPixels;
        } else if (height <= 0) {
            height = heightPixels * 9 / 16;
        }
        ViewGroup.LayoutParams para1;
        para1 = view.getLayoutParams();
        para1.height = height;
        view.setLayoutParams(para1);
    }

    /**
     * 设置数据
     */
    private void injectData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CommentAdapter commentAdapter = new CommentAdapter(getContext());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(commentAdapter);
    }

    /**
     * 添加到dectorView
     */
    public void attachWindow() {
        mDecorView.post(new Runnable() {
            @Override
            public void run() {
                ViewParent parent = getParent();
                if (parent != null) {
                    ViewGroup parentView = (ViewGroup) parent;
                    parentView.removeView(BottomDialogView.this);
                }

                LayoutParams layoutParams
                        = new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT);
                mDecorView.addView(BottomDialogView.this, layoutParams);

                mIsSliding = true;
                superSlideLayout.slideIn(SuperSlideLayout.EDGE_BOTTOM, mDecorView.getHeight(), mAnimationTime);
            }
        });
        performFocusState(true, mAnimationTime);
    }

    /**
     * 执行消失操作
     */
    public void detacheWindow() {
        if (isShowing() && !mIsDismissing) {
            mIsDismissing = true;
            int top = superSlideLayout.getChildAt(0).getTop();
            int slideOutDy = mDecorView.getHeight() - top;
            superSlideLayout.slideOut(SuperSlideLayout.EDGE_BOTTOM, slideOutDy, mAnimationTime);
            performFocusState(false, mAnimationTime);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * 焦点状态问题
     *
     * @param focus
     */
    private void performFocusState(final boolean focus, int mAnimationTime) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                //获取焦点
                View contentView = ((Activity) mContext).findViewById(android.R.id.content);
                contentView.setFocusable(!focus);
                contentView.setFocusableInTouchMode(!focus);
                if (focus) {
                    requestFocus();
                    setFocusableInTouchMode(true);
                    mIsShowing = true;
                    mIsSliding = false;
                    if (onSlideWindowListener != null) {
                        onSlideWindowListener.onShow();
                    }
                } else {
                    clearFocus();
                    // 移除弹窗
                    mDecorView.removeView(BottomDialogView.this);
                    mIsShowing = false;
                    mIsDismissing = false;
                    if (onSlideWindowListener != null) {
                        onSlideWindowListener.onDismiss();
                    }
                }
            }
        }, mAnimationTime);
    }

    /**
     * 动态设置view的高度
     *
     * @param height
     */
    public void setHeight(int height) {
        setHeight(llContent, height);
    }

    /**
     * 设置背景
     *
     * @param mDrawable
     * @return
     */
    public void setBackgroundWindowColor(Drawable mDrawable) {
        superSlideLayout.setBackground(mDrawable);
    }

    /**
     * 设置背景
     *
     * @param mResDrawable
     * @return
     */
    public void setBackgroundWindowColor(@DrawableRes int mResDrawable) {
        Drawable drawable = mContext.getResources().getDrawable(mResDrawable);
        setBackgroundWindowColor(drawable);
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
     * 是否正在显示
     *
     * @return
     */
    public boolean isShowing() {
        return mIsShowing;
    }


    /**
     * 点击返回键的时候监听
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mCancleBackpress) {
                detacheWindow();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 滑动组件的回调
     *
     * @param slideLayout
     */
    @Override
    public void onSlideStart(SlideLayoutImpl slideLayout) {
        //设置执行动画
        slideLayout.setScrollTime(mAnimationTime);
    }

    @Override
    public void onTouchOutside(SlideLayoutImpl slideLayout, boolean touchIntChild) {
        if (mCancleOutside && !touchIntChild) {
            detacheWindow();
        }
    }

    @Override
    public void onSlideChange(SlideLayoutImpl slideLayout, float hPercent, float vPercent, float scaleYPercent, float alphaPercent) {
        if (onSlideWindowListener != null) {
            onSlideWindowListener.onSlideChange(hPercent, vPercent);
        }
    }

    @Override
    public void onSlideRecover(SlideLayoutImpl slideLayout, boolean b) {

    }

    @Override
    public void onSlideFinish(SlideLayoutImpl slideLayout) {
        //滑动结束的话，
        performFocusState(false, mAnimationTime);
    }


    /**
     * 自定义的dialog监听
     */
    public interface OnSlideWindowListener {

        void onShow();

        void onSlideChange(float hPercent, float vPercent);

        void onDismiss();
    }


    public void setOnSlideWindowListener(OnSlideWindowListener onSlideWindowListener) {
        this.onSlideWindowListener = onSlideWindowListener;
    }
}
