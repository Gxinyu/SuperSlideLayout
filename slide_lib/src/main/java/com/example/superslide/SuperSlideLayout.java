package com.example.superslide;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;


/**
 * 使用说明：
 * 1、一定要设置子View
 */
public class SuperSlideLayout extends FrameLayout implements SlideLayoutImpl {

    //四个方向
    public static final int EDGE_LEFT = 1 << 0;
    public static final int EDGE_RIGHT = 1 << 1;
    public static final int EDGE_TOP = 1 << 2;
    public static final int EDGE_BOTTOM = 1 << 3;
    public static final int EDGE_ALL = EDGE_LEFT | EDGE_TOP | EDGE_RIGHT | EDGE_BOTTOM;
    //两种方向
    public static final int DIRECTION_HORIZONTAL = 1 << 0;
    public static final int DIRECTION_VERTICAL = 1 << 1;
    public static final int DIRECTION_ALL = DIRECTION_HORIZONTAL | DIRECTION_VERTICAL;
    //三种状态
    public static final int STATE_IDLE = 1 << 0;
    public static final int STATE_DRAG = 1 << 1;
    public static final int STATE_RELEASE = 1 << 2;
    public int mCurrentState = STATE_IDLE;

    //自定义属性
    private boolean mSlideEnable;//可以滚动
    private int mSlideEdge;//滑动的方向
    private float mSlideThresholdRate;//结束比例
    private boolean mCheckThreshold;//是否需要判断阀值
    private boolean mAlphaEnable;//支持背景透明
    private float mAlphaRate;//背景透明度比例
    private float mMinAlpha;//背景最小透明度
    private boolean mScaleEnable;//支持缩放
    private float mScaleRate;//缩放比例
    private float mMinScale;//最小缩放比例
    private boolean mOverflowParent;//越过父类边界
    private boolean mSingleDirection;//单一方向
    private boolean mMultiPointerEnable;//多点触摸支持
    private int mScrollTime;//松手之后滑动时间

    private Context mContext;
    private Scroller mScroller;
    private Activity mActivity;//需要关闭的activity
    private boolean mIsBeingDragged;//是否拦截
    private int mDirection;//方向
    private float mDownX, mDownY; //按下的位置
    private boolean mPositiveX, mPositiveY;//正方向向量
    private int mMeasuredWidth, mMeasuredHeight;//宽高
    private View mChildRootView;//子视图
    private Drawable mBackground;//背景
    private Drawable mForeground;//前景
    private OnSlideListener mOnSlideListener;//滑动监听器

    private static final int INVALID_POINTER = -1;
    private int mActivePointerId = INVALID_POINTER;
    private boolean mCheckTouchInChild;//触点是否在子类中

    //处理系统状态栏
    private WindowInsetsCompat mLastInsets;
    private boolean mDrawStatusBarBackground;
    private Drawable mStatusBarBackground;

    private static final Interpolator sInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };
    private boolean mOverThreshold;

    /**
     * 构造
     *
     * @param context
     */
    public SuperSlideLayout(Context context) {
        this(context, null);
    }

    public SuperSlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperSlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setWillNotDraw(false);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setFocusable(true);
        //可自定义动画差值器
        mScroller = new Scroller(context, sInterpolator);
        //需要自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SuperSlideLayout);
        mSlideEnable = array.getBoolean(R.styleable.SuperSlideLayout_ssl_slideEnable, true);
        mSlideEdge = array.getInteger(R.styleable.SuperSlideLayout_ssl_slideEdge, EDGE_LEFT);
        mSlideThresholdRate = array.getFloat(R.styleable.SuperSlideLayout_ssl_thresholdRate, 0.382f);
        mCheckThreshold = array.getBoolean(R.styleable.SuperSlideLayout_ssl_checkThreshold, true);
        mAlphaEnable = array.getBoolean(R.styleable.SuperSlideLayout_ssl_alphaEnable, true);
        mAlphaRate = array.getFloat(R.styleable.SuperSlideLayout_ssl_alphaRate, 0.5f);
        mMinAlpha = array.getFloat(R.styleable.SuperSlideLayout_ssl_minAlpha, 0);
        mScaleEnable = array.getBoolean(R.styleable.SuperSlideLayout_ssl_scaleEnable, false);
        mScaleRate = array.getFloat(R.styleable.SuperSlideLayout_ssl_scaleRate, 0.382f);
        mMinScale = array.getFloat(R.styleable.SuperSlideLayout_ssl_minScale, 0.3f);
        mOverflowParent = array.getBoolean(R.styleable.SuperSlideLayout_ssl_overflowParent, false);
        mSingleDirection = array.getBoolean(R.styleable.SuperSlideLayout_ssl_singleDirection, true);
        mMultiPointerEnable = array.getBoolean(R.styleable.SuperSlideLayout_ssl_multiPointer, true);
        mScrollTime = array.getInt(R.styleable.SuperSlideLayout_ssl_scrollTime, 1500);
        mStatusBarBackground = array.getDrawable(R.styleable.SuperSlideLayout_ssl_statusBarBackground);
        array.recycle();
        //约束默认值
        mSlideThresholdRate = mSlideThresholdRate < 0 ? 0 : mSlideThresholdRate;
        mSlideThresholdRate = mSlideThresholdRate > 1 ? 1 : mSlideThresholdRate;
        mAlphaRate = mAlphaRate < 0 ? 0 : mAlphaRate;
        mAlphaRate = mAlphaRate > 1 ? 1 : mAlphaRate;
        mMinAlpha = mMinAlpha < 0 ? 0 : mMinAlpha;
        mMinAlpha = mMinAlpha > 1 ? 1 : mMinAlpha;
        mScaleRate = mScaleRate < 0 ? 0 : mScaleRate;
        mScaleRate = mScaleRate > 1 ? 1 : mScaleRate;
        mMinScale = mMinScale < 0 ? 0 : mMinScale;
        mMinScale = mMinScale > 1 ? 1 : mMinScale;

        if (ViewCompat.getImportantForAccessibility(this)
                == ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            ViewCompat.setImportantForAccessibility(this,
                    ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }

        //处理状态栏颜色值
        overlayStatusBar(mContext);
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
                            final SuperSlideLayout superSlideLayout = (SuperSlideLayout) view;
                            superSlideLayout.setChildInsets(insets, insets.getSystemWindowInsetTop() > 0);
                            return insets.consumeSystemWindowInsets();
                        }
                    });

            //版本高于21才能采用透明状态栏
            if (Build.VERSION.SDK_INT >= 21) {
                if (mStatusBarBackground == null) {
                    //默认情况没有，则取系统主题里面的
                    int[] THEME_STATUSBAR = {android.R.attr.statusBarColor};
                    final TypedArray a = context.obtainStyledAttributes(THEME_STATUSBAR);
                    try {
                        mStatusBarBackground = a.getDrawable(0);
                    } finally {
                        a.recycle();
                    }
                }

                //设置沉浸式
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


    /**
     * 绑定子视图
     */
    public void attachView(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null) {
                ViewGroup parentView = (ViewGroup) parent;
                parentView.removeView(view);
                mChildRootView = view;
                addView(view);
                parentView.addView(this);
            }
        } else {
            throw new NullPointerException("ready to attach child view is null");
        }
    }

    /**
     * 绑定Activity
     */
    public void attachActivity(Activity activity) {
        if (activity != null) {
            mActivity = activity;
            ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
            mChildRootView = decorView.getChildAt(0);//contentview+titlebar
            View contentView = decorView.findViewById(android.R.id.content);
            Drawable contentViewBackground = contentView.getBackground();
            if (contentViewBackground == null) contentView.setBackground(mForeground);
            decorView.removeView(mChildRootView);
            addView(mChildRootView);
            decorView.addView(this);
        } else {
            throw new NullPointerException("ready to attach activity is null");
        }
    }

    @Override
    public void addView(View child) {
        addView(child, -1);
    }

    @Override
    public void addView(View child, int index) {
        checkOnlyChild();
        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        addView(child, -1, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        checkOnlyChild();
        super.addView(child, index, params);
    }

    /**
     * 直属的子视图只能有一个
     */
    private void checkOnlyChild() {
        if (getChildCount() > 0) {
            throw new IllegalStateException("SuperSlideLayout can host only one direct child");
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mChildRootView = getChildAt(0);
        mBackground = getBackground();
        mForeground = getForeground();
        if (mChildRootView != null && mForeground != null) {
            mChildRootView.setBackground(mForeground);
            setForeground(null);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        mMeasuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight();
    }

    @Override
    protected void onDetachedFromWindow() {
        if ((mScroller != null) && !mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();
        int pointerIndex;

        //第一步：监测是否含子类
        boolean checkNullChild = checkNullChild();
        if (!mSlideEnable || checkNullChild) {
            return super.onInterceptTouchEvent(event);
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //判断触点是否在子view中
                mDownX = event.getX();
                mDownY = event.getY();
                mActivePointerId = event.getPointerId(0);
                mCheckTouchInChild = checkTouchInChild(mChildRootView, mDownX, mDownY);

                //判断是否触点是否在子类外部
                if (!mCheckTouchInChild) {
                    if (mOnSlideListener != null) {
                        mOnSlideListener.onTouchOutside(this, mCheckTouchInChild);
                    }
                    return super.onInterceptTouchEvent(event);
                }

                mScroller.computeScrollOffset();
                if (mCurrentState != STATE_IDLE) {
                    mScroller.abortAnimation();
                    mIsBeingDragged = true;
                    disallowInterceptTouchEvent();
                } else {
                    mIsBeingDragged = false;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                //计算移动距离 判定是否滑动
                pointerIndex = event.findPointerIndex(mActivePointerId);
                if (pointerIndex == INVALID_POINTER) {
                    break;
                }

                float dx = event.getX(pointerIndex) - mDownX;
                float dy = event.getY(pointerIndex) - mDownY;
                mIsBeingDragged = chechkCanDrag(dx, dy);
                if (mIsBeingDragged) {
                    performDrag(event, dx, dy, pointerIndex);
                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged) {
                    revertOriginalState(getScrollY(), getScrollY(), false);
                }
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;
        }


        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();
        int pointerIndex;

        //第一步：监测是否含子类
        boolean checkNullChild = checkNullChild();
        if (checkNullChild || !mSlideEnable) {
            return super.onTouchEvent(event);
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                mActivePointerId = event.getPointerId(0);
                mCheckTouchInChild = checkTouchInChild(mChildRootView, mDownX, mDownY);

                if (mIsBeingDragged) {
                    disallowInterceptTouchEvent();
                }

                break;
            case MotionEvent.ACTION_MOVE:
                //如果触点不在子类中直接返回
                if (!mCheckTouchInChild) {
                    break;
                }
                //检测触点
                pointerIndex = event.findPointerIndex(mActivePointerId);
                if (pointerIndex == INVALID_POINTER) {
                    break;
                }

                float dx = event.getX(pointerIndex) - mDownX;
                float dy = event.getY(pointerIndex) - mDownY;
                performDrag(event, dx, dy, pointerIndex);

                break;
            case MotionEvent.ACTION_UP:
                // 根据手指释放时的位置决定回弹还是关闭,只要有一方超越就结束
                pointerIndex = event.findPointerIndex(mActivePointerId);
                if (pointerIndex == INVALID_POINTER) {
                    break;
                }

                performRelease();
                mActivePointerId = INVALID_POINTER;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                //第二步：监测触点范围(必须有子类才去监测触点范围)
                if (mMultiPointerEnable) {
                    pointerIndex = event.getActionIndex();
                    mDownX = (int) event.getX(pointerIndex);
                    mDownY = (int) event.getY(pointerIndex);
                    mActivePointerId = event.getPointerId(pointerIndex);
                    mCheckTouchInChild = checkTouchInChild(mChildRootView, mDownX, mDownY);
                    if (mIsBeingDragged) {
                        disallowInterceptTouchEvent();
                    }
                }

                break;

            case MotionEvent.ACTION_POINTER_UP:
                //也可以做边缘释放，后期可以添加
                if (mMultiPointerEnable) {
                    onSecondaryPointerUp(event);
                    mCheckTouchInChild = checkTouchInChild(mChildRootView, mDownX, mDownY);
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged) {
                    revertOriginalState(getScrollY(), getScrollY(), false);
                }
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                return false;
        }

        return true;
    }


    /**
     * 拖拽操作
     *
     * @param event
     * @param dx
     * @param dy
     * @param pointerIndex
     */
    private void performDrag(MotionEvent event, float dx, float dy, int pointerIndex) {
        if (mIsBeingDragged) {
            disallowInterceptTouchEvent();
            //触发监听 UP的时候取消监听
            if (mOnSlideListener != null && mCurrentState != STATE_DRAG) {
                mOnSlideListener.onSlideStart(this);
            }

            mCurrentState = STATE_DRAG;
            int scrollX = getScrollX();
            int scrollY = getScrollY();

            if (mDirection == DIRECTION_HORIZONTAL) {
                boolean slideWelt = mPositiveX ? scrollX >= dx : scrollX <= dx;
                if (slideWelt && !mOverflowParent) {
                    scrollTo(0, 0);
                } else {
                    scrollBy((int) -dx, 0);
                }
            } else if (mDirection == DIRECTION_VERTICAL) {
                boolean slideWelt = mPositiveY ? scrollY >= dy : scrollY <= dy;
                if (slideWelt && !mOverflowParent) {
                    scrollTo(0, 0);
                } else {
                    scrollBy(0, (int) -dy);
                }
            } else if (mDirection == DIRECTION_ALL) {
                boolean limitX = mPositiveX ? scrollX >= dx : scrollX <= dx;
                boolean limitY = mPositiveY ? scrollY >= dy : scrollY <= dy;
                int realDx = limitX ? mOverflowParent ? (int) -dx : 0 : (int) -dx;
                int realDy = limitY ? mOverflowParent ? (int) -dy : 0 : (int) -dy;
                scrollBy(realDx, realDy);
            }

            //绘制背景
            invalidateBackground(scrollX, scrollY);
            mDownX = event.getX(pointerIndex);
            mDownY = event.getY(pointerIndex);
        } else {
            mIsBeingDragged = chechkCanDrag(dx, dy);
        }
    }

    /**
     * 释放第二次触点
     *
     * @param ev
     */
    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = ev.getActionIndex();
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mDownX = ev.getX(newPointerIndex);
            mDownY = ev.getY(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    /**
     * 检测触点是否在当前view中
     */
    private boolean checkTouchInChild(View childView, float x, float y) {
        if (childView != null) {
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            //需要加上已经滑动的距离
            float left = childView.getLeft() - scrollX;
            float right = childView.getRight() - scrollX;
            float top = childView.getTop() - scrollY;
            float bottom = childView.getBottom() - scrollY;
            if (y >= top && y <= bottom && x >= left
                    && x <= right) {
                return true;
            }
        }
        return false;
    }


    /**
     * 监测是否有子类
     * 无子视图禁止拖动
     *
     * @return
     */
    private boolean checkNullChild() {
        mChildRootView = getChildAt(0);
        return getChildCount() == 0;
    }

    /**
     * 不让父类拦截事件
     */
    private void disallowInterceptTouchEvent() {
        final ViewParent parent = getParent();
        if (parent != null)
            parent.requestDisallowInterceptTouchEvent(true);
    }

    /**
     * 检测是否可以拖拽
     *
     * @param dx
     * @param dy
     * @return
     */
    private boolean chechkCanDrag(float dx, float dy) {
        boolean mMinTouchSlop = checkEdgeAndTouchSlop(dx, dy);
        boolean chcekScrollPriority = chcekScrollPriority(dx, dy);
        boolean checkCanScrolly = checkCanScrolly(dx, dy);
        return mMinTouchSlop && chcekScrollPriority && !checkCanScrolly;
    }

    /**
     * 边缘滚动
     *
     * @param dx
     * @param dy
     * @return
     */
    private boolean checkEdgeAndTouchSlop(float dx, float dy) {
        boolean mMinTouch = false;
        if (mSlideEdge == EDGE_LEFT) {
            mDirection = DIRECTION_HORIZONTAL;
            mPositiveX = dx > 0;
            mMinTouch = mPositiveX;
        } else if (mSlideEdge == EDGE_RIGHT) {
            mDirection = DIRECTION_HORIZONTAL;
            mPositiveX = dx > 0;
            mMinTouch = -dx > 0;
        } else if (mSlideEdge == EDGE_TOP) {
            mDirection = DIRECTION_VERTICAL;
            mPositiveY = dy > 0;
            mMinTouch = mPositiveY;
        } else if (mSlideEdge == EDGE_BOTTOM) {
            mDirection = DIRECTION_VERTICAL;
            mPositiveY = dy > 0;
            mMinTouch = -dy > 0;
        } else if (mSlideEdge == (EDGE_LEFT | EDGE_RIGHT)) {
            mDirection = DIRECTION_HORIZONTAL;
            mPositiveX = dx > 0;
            mMinTouch = Math.abs(dx) > 0;

        } else if (mSlideEdge == (EDGE_TOP | EDGE_BOTTOM)) {
            mDirection = DIRECTION_VERTICAL;
            mPositiveY = dy > 0;//正方向
            mMinTouch = Math.abs(dy) > 0;
        } else if (mSlideEdge == (EDGE_LEFT | EDGE_TOP)) {
            if (mSingleDirection) {
                boolean slideX = Math.abs(dx) > Math.abs(dy);
                mDirection = slideX ? DIRECTION_HORIZONTAL : DIRECTION_VERTICAL;
                mPositiveX = dx > 0;
                mPositiveY = dy > 0;
                mMinTouch = slideX ? dx > 0 : dy > 0;
            } else {
                mDirection = DIRECTION_ALL;
                mPositiveX = dx > 0;
                mPositiveY = dy > 0;
                mMinTouch = mPositiveX && mPositiveY;
            }
        } else if (mSlideEdge == (EDGE_LEFT | EDGE_BOTTOM)) {
            if (mSingleDirection) {
                boolean slideX = Math.abs(dx) > Math.abs(dy);
                mDirection = slideX ? DIRECTION_HORIZONTAL : DIRECTION_VERTICAL;
                mPositiveX = dx > 0;
                mPositiveY = dy > 0;
                mMinTouch = slideX ? dx > 0 : -dy > 0;
            } else {
                mDirection = DIRECTION_ALL;
                mPositiveX = dx > 0;
                mPositiveY = dy > 0;
                mMinTouch = mPositiveX && !mPositiveY;
            }
        } else if (mSlideEdge == (EDGE_RIGHT | EDGE_TOP)) {
            if (mSingleDirection) {
                boolean slideX = Math.abs(dx) > Math.abs(dy);
                mDirection = slideX ? DIRECTION_HORIZONTAL : DIRECTION_VERTICAL;
                mPositiveX = dx > 0;
                mPositiveY = dy > 0;
                mMinTouch = slideX ? -dx > 0 : dy > 0;
            } else {
                mDirection = DIRECTION_ALL;
                mPositiveX = dx > 0;
                mPositiveY = dy > 0;
                mMinTouch = !mPositiveX && mPositiveY;
            }
        } else if (mSlideEdge == (EDGE_RIGHT | EDGE_BOTTOM)) {
            if (mSingleDirection) {
                boolean slideX = Math.abs(dx) > Math.abs(dy);
                mDirection = slideX ? DIRECTION_HORIZONTAL : DIRECTION_VERTICAL;
                mPositiveX = dx > 0;
                mPositiveY = dy > 0;

                mMinTouch = slideX ? -dx > 0 : -dy > 0;
            } else {
                mDirection = DIRECTION_ALL;
                mPositiveX = dx > 0;
                mPositiveY = dy > 0;
                mMinTouch = !mPositiveX && !mPositiveY;
            }
        } else if (mSlideEdge == (EDGE_LEFT | EDGE_RIGHT | EDGE_TOP)) {

            if (mSingleDirection) {
                boolean slideX = Math.abs(dx) > Math.abs(dy);
                mDirection = slideX ? DIRECTION_HORIZONTAL : DIRECTION_VERTICAL;
                mPositiveX = dx > 0;
                mPositiveY = dy > 0;
                mMinTouch = slideX ? Math.abs(dx) > 0 : dy > 0;
            } else {
                mDirection = DIRECTION_ALL;
                mPositiveX = dx > 0;
                mPositiveY = dy > 0;
                mMinTouch = mPositiveY;
            }
        } else if (mSlideEdge == (EDGE_LEFT | EDGE_RIGHT | EDGE_BOTTOM)) {
            boolean slideX = Math.abs(dx) > Math.abs(dy);
            if (mSingleDirection) {
                //必须只有一种情况的下
                mDirection = slideX ? DIRECTION_HORIZONTAL : DIRECTION_VERTICAL;
                mPositiveX = dx > 0;
                mPositiveY = dy > 0;
                mMinTouch = slideX ? Math.abs(dx) > 0 : -dy > 0;
            } else {
                mDirection = DIRECTION_ALL;
                mPositiveX = dx >= 0;
                mPositiveY = dy >= 0;
                mMinTouch = !mPositiveY;
            }
        } else if (mSlideEdge == (EDGE_TOP | EDGE_BOTTOM | EDGE_LEFT)) {
            if (mSingleDirection) {
                boolean slideX = Math.abs(dx) > Math.abs(dy);
                mDirection = slideX ? DIRECTION_HORIZONTAL : DIRECTION_VERTICAL;
                mPositiveX = dx > 0;
                mPositiveY = dy > 0;
                mMinTouch = slideX ? dx > 0 : Math.abs(dy) > 0;
            } else {
                mDirection = DIRECTION_ALL;
                mPositiveX = dx > 0;
                mPositiveY = dy > 0;
                mMinTouch = mPositiveX;
            }
        } else if (mSlideEdge == (EDGE_TOP | EDGE_BOTTOM | EDGE_RIGHT)) {
            if (mSingleDirection) {
                boolean slideX = Math.abs(dx) > Math.abs(dy);
                mDirection = slideX ? DIRECTION_HORIZONTAL : DIRECTION_VERTICAL;
                mPositiveX = dx > 0;
                mPositiveY = dy > 0;
                mMinTouch = slideX ? -dx > 0 : Math.abs(dy) > 0;
            } else {
                mDirection = DIRECTION_ALL;
                mPositiveX = dx >= 0;
                mPositiveY = dy >= 0;
                mMinTouch = !mPositiveX;
            }
        } else if (mSlideEdge == EDGE_ALL) {
            if (mSingleDirection) {
                boolean slideX = Math.abs(dx) >= Math.abs(dy);
                mDirection = slideX ? DIRECTION_HORIZONTAL : DIRECTION_VERTICAL;
                mPositiveX = dx > 0;
                mPositiveY = dy > 0;
                mMinTouch = slideX ? Math.abs(dx) > 0 : Math.abs(dy) > 0;
            } else {
                mDirection = DIRECTION_ALL;
                mPositiveX = dx > 0;
                mPositiveY = dy > 0;
                mOverflowParent = true;
                mMinTouch = true;
            }
        }
        return mMinTouch;
    }

    /**
     * 优先在某个方向上滚动
     */
    private boolean chcekScrollPriority(float dx, float dy) {
        if (mDirection == DIRECTION_HORIZONTAL) {
            return Math.abs(dx) - Math.abs(dy) > 0;
        } else if (mDirection == DIRECTION_VERTICAL) {
            return Math.abs(dy) - Math.abs(dx) > 0;
        } else {
            //互斥方向的话无优先级
            return true;
        }
    }

    /**
     * 检测是否可以滚动
     *
     * @return
     */
    private boolean checkCanScrolly(float dx, float dy) {
        //如果优先处理子类View的滚动事件的话，需要先处理子类的，然后才交给自己
        if (mDirection == DIRECTION_HORIZONTAL) {
            return canScrollHorizontally(this, false, (int) dx, (int) mDownX, (int) mDownY);
        } else if (mDirection == DIRECTION_VERTICAL) {
            return canScrollVertically(this, false, (int) dy, (int) mDownX, (int) mDownY);
        } else if (mDirection == DIRECTION_ALL) {
            boolean canScrollH2 = canScrollHorizontally(this, false, (int) dx, (int) mDownX, (int) mDownY);
            boolean canScrollV2 = canScrollVertically(this, false, (int) dy, (int) mDownX, (int) mDownY);
            return canScrollH2 || canScrollV2;
        }
        return false;
    }

    /**
     * 释放手势
     */
    private void performRelease() {
        if (mIsBeingDragged) {
            int scrollX = getScrollX();
            int scrollY = getScrollY();

            mOverThreshold = checkThreshold(scrollX, scrollY);
            if (mCheckThreshold && mOverThreshold) {
                int endScrollX = scrollX < 0 ? -scrollX - mMeasuredWidth : mMeasuredWidth - scrollX;
                int endScrollY = scrollY < 0 ? -scrollY - mMeasuredHeight : mMeasuredHeight - scrollY;
                endScrollX = mDirection == DIRECTION_VERTICAL ? 0 : endScrollX;
                endScrollY = mDirection == DIRECTION_HORIZONTAL ? 0 : endScrollY;
                smoothllyScroll(scrollX, scrollY, endScrollX, endScrollY, mScrollTime);
            } else {
                revertOriginalState(scrollX, scrollY, mOverThreshold);
            }
        }
    }

    /**
     * 检测阈值
     *
     * @return
     */
    private boolean checkThreshold(int scrollX, int scrollY) {
        if (mDirection == DIRECTION_HORIZONTAL) {
            return Math.abs(scrollX) > mMeasuredWidth * mSlideThresholdRate;
        } else if (mDirection == DIRECTION_VERTICAL) {
            return Math.abs(scrollY) > mMeasuredHeight * mSlideThresholdRate;
        } else {
            boolean xThreshold = Math.abs(scrollX) > mMeasuredWidth * mSlideThresholdRate;
            boolean yThreshold = Math.abs(scrollY) > mMeasuredHeight * mSlideThresholdRate;
            return xThreshold || yThreshold;
        }
    }

    /**
     * 恢复初始状态
     *
     * @param scrollX
     * @param scrollY
     */
    private void revertOriginalState(int scrollX, int scrollY, boolean overThreshold) {
        //恢复真正的状态
        smoothllyScroll(scrollX, scrollY, -scrollX, -scrollY, mScrollTime);
        //监听
        if (mOnSlideListener != null)
            mOnSlideListener.onSlideRecover(this, overThreshold);
    }

    /**
     * 绘制背景
     * 缩放和背景颜色渐变
     *
     * @param scrollX
     * @param scrollY
     */
    private void invalidateBackground(int scrollX, int scrollY) {
        //计算滑动比例
        float mPercentSlideX = (scrollX * 1.0f) / mMeasuredWidth;
        float mPercentSlideY = (scrollY * 1.0f) / mMeasuredHeight;

        float maxPercent = Math.max(Math.abs(mPercentSlideX), Math.abs(mPercentSlideY));
        float mMaxScal = 0, mMaxAlpha = 0;

        //设置缩放
        if (mScaleEnable && mChildRootView != null) {
            //限制缩放最小值
            mMaxScal = maxPercent / mScaleRate;
            float limitScal = mMaxScal > 1 - mMinScale ? 1 - mMinScale : mMaxScal;
            mMaxScal = 1 - limitScal;
            mChildRootView.setScaleX(mMaxScal);
            mChildRootView.setScaleY(mMaxScal);
        }

        //设置背景
        if (mAlphaEnable) {
            float maxAlpha = maxPercent / mAlphaRate;
            float limitAlpha = maxAlpha > 1 - mMinAlpha ? 1 - mMinAlpha : maxAlpha;
            mMaxAlpha = 1 - limitAlpha;
            if (mBackground != null && mAlphaEnable) {
                mBackground.mutate().setAlpha((int) ((mMaxAlpha) * 255));
            }
        }

        //相对于屏幕的比例
        if (mOnSlideListener != null)
            mOnSlideListener.onSlideChange(this,
                    mPercentSlideX, mPercentSlideY,
                    mMaxScal, mMaxAlpha);
    }

    /**
     * 平滑滑动
     */
    private void smoothllyScroll(int startX, int startY, int endX, int endY, int mScrollTime) {
        smoothllyScroll(startX, startY, endX, endY, true, mScrollTime);
    }

    /**
     * 平滑滑动
     *
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param computeTime 计算滑动时间
     * @param mScrollTime
     */
    public void smoothllyScroll(int startX, int startY, int endX, int endY, boolean computeTime, int mScrollTime) {
        mCurrentState = STATE_RELEASE;
        int duration;
        if (computeTime) {
            //计算百分比时间
            float offsetXPercent = Math.abs(endX) * 1f / mMeasuredWidth;
            float offsetYPercent = Math.abs(endY) * 1f / mMeasuredHeight;
            duration = (int) (Math.max(offsetXPercent, offsetYPercent) * mScrollTime);
        } else {
            duration = mScrollTime;
        }

        mScroller.startScroll(startX, startY, endX, endY, duration);
        ViewCompat.postInvalidateOnAnimation(this);
    }


    /**
     * 平滑的滚动到最终位置
     */
    @Override
    public void computeScroll() {
        int oldX = getScrollX();
        int oldY = getScrollY();
        if (!mScroller.isFinished() && mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            //位置改变才去滑动
            if (oldX != x || oldY != y) {
                scrollTo(x, y);
                //绘制背景 如果是不检测阈值并且超过阈值则不绘制
                if (mCheckThreshold || !mOverThreshold) {
                    invalidateBackground(x, y);
                }
            }
            ViewCompat.postInvalidateOnAnimation(this);
            return;
        } else {
            boolean originalState = Math.abs(oldX) == 0 && Math.abs(oldY) == 0;
            boolean outParent = Math.abs(oldX) >= mMeasuredWidth || Math.abs(oldY) >= mMeasuredHeight;
            //释放状态
            if (originalState || outParent) {
                mCurrentState = STATE_IDLE;
                mIsBeingDragged = false;
            }
            if (outParent) {
                if (mOnSlideListener != null) mOnSlideListener.onSlideFinish(this);
                if (mActivity != null) mActivity.finish();
            }
        }

    }

    /**
     * 当window焦点改变的时候回调
     *
     * @param hasWindowFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus && mIsBeingDragged) {
            //如果未归位，则条用
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            revertOriginalState(scrollX, scrollY, false);
        }
    }

    /**
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 重写水平和垂直方向上是否可以滚动
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * 当前触点所在iew
     * 水平方向上是否
     * 可以滚动
     *
     * @param v
     * @param dx
     * @param x
     * @param y
     * @return
     */
    private boolean canScrollHorizontally(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) v;
            final int scrollX = getScrollX();
            final int scrollY = getScrollY();
            final int count = group.getChildCount();
            for (int i = count - 1; i >= 0; i--) {
                final View child = group.getChildAt(i);
                boolean checkTouchInChild = checkTouchInChild(child, x, y);
                if (checkTouchInChild && canScrollHorizontally(child, true, dx,
                        x + scrollX - child.getLeft(),
                        y + scrollY - child.getTop()))
                    return true;
            }
        }

        return checkV && v.canScrollHorizontally(-dx);
    }

    /**
     * 当前触点所在iew
     * 垂直方向上是否
     * 可以滚动
     *
     * @param v
     * @param dy
     * @param x
     * @param y
     * @return
     */
    private boolean canScrollVertically(View v, boolean checkV, int dy, int x, int y) {
        if (v instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) v;
            final int scrollX = v.getScrollX();
            final int scrollY = v.getScrollY();
            final int count = group.getChildCount();
            for (int i = count - 1; i >= 0; i--) {
                final View child = group.getChildAt(i);
                //只有当触点在view之内才判断
                boolean checkTouchInChild = checkTouchInChild(child, x, y);
                if (checkTouchInChild && canScrollVertically(child, true, dy,
                        x + scrollX - child.getLeft(),
                        y + scrollY - child.getTop()))
                    return true;
            }
        }

        return checkV && v.canScrollVertically(-dy);
    }


    /**+++++++++++++++++++++++++++++++++++++++++++++
     * 动态设置属性
     * +++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * 禁用滑动
     *
     * @param mSlideEnable
     */
    @Override
    public void setSlideEnable(boolean mSlideEnable) {
        this.mSlideEnable = mSlideEnable;
    }

    /**
     * 是否禁用
     *
     * @return
     */
    @Override
    public boolean isSlideEnable() {
        return mSlideEnable;
    }

    /**
     * 设置方向
     *
     * @param slideEdge
     */
    @Override
    public void setSlideEdge(
            @IntRange(from = SuperSlideLayout.EDGE_LEFT, to = SuperSlideLayout.EDGE_ALL) int slideEdge) {
        this.mSlideEdge = slideEdge;
    }

    /**
     * 获取方向
     *
     * @return
     */
    @Override
    public int getSlideEdge() {
        return mSlideEdge;
    }

    /**
     * 设置关闭阈值
     *
     * @param mSlideThresholdRate
     */
    @Override
    public void setSlideThresholdRate(@FloatRange(from = 0, to = 1) float mSlideThresholdRate) {
        this.mSlideThresholdRate = mSlideThresholdRate;
    }

    /**
     * 获取阈值
     *
     * @return
     */
    @Override
    public float getSlideThresholdRate() {
        return mSlideThresholdRate;
    }


    /**
     * 设置是否监测阀值
     *
     * @param mCheckThreshold
     */
    public void setCheckThreshold(boolean mCheckThreshold) {
        this.mCheckThreshold = mCheckThreshold;
    }

    /**
     * 是否监测阀值
     *
     * @return
     */
    public boolean isCheckThreshold() {
        return mCheckThreshold;
    }

    /**
     * 背景透明
     *
     * @param mAlphaEnable
     */
    @Override
    public void setAlphaEnable(boolean mAlphaEnable) {
        this.mAlphaEnable = mAlphaEnable;
    }

    /**
     * 背景是否透明
     */
    @Override
    public boolean isAlphaEnable() {
        return mAlphaEnable;
    }

    /**
     * 背景透明度比例
     *
     * @param mBackgroundRate
     */
    @Override
    public void setAlphaRate(@FloatRange(from = 0, to = 1) float mBackgroundRate) {
        this.mAlphaRate = mBackgroundRate;
    }

    /**
     * 获取背景透明度比例
     */
    @Override
    public float getAlphaRate() {
        return mAlphaRate;
    }

    /**
     * 设置最小的背景透明
     *
     * @param mMinAlpha
     */
    @Override
    public void setMinAlpha(@FloatRange(from = 0, to = 1) float mMinAlpha) {
        this.mMinAlpha = mMinAlpha;
    }

    /**
     * 获取最小的背景透明
     */
    @Override
    public float getMinAlpha() {
        return mMinAlpha;
    }

    /**
     * 设置背景缩放
     *
     * @param mScaleEnable
     */
    @Override
    public void setScaleEnable(boolean mScaleEnable) {
        this.mScaleEnable = mScaleEnable;
    }

    /**
     * 设置背景缩放
     *
     * @param mScaleEnable
     */
    @Override
    public boolean isScaleEnable(boolean mScaleEnable) {
        return mScaleEnable;
    }

    /**
     * 缩放比例
     *
     * @param mScaleRate
     */
    public void setScaleRate(@FloatRange(from = 0, to = 1) float mScaleRate) {
        this.mScaleRate = mScaleRate;
    }

    /**
     * 获取缩放比例
     */
    @Override
    public float getScaleRate() {
        return mScaleRate;
    }

    /**
     * 设置最小的缩放
     *
     * @param mMinScale
     */
    @Override
    public void setMinScale(@FloatRange(from = 0.1, to = 0.9) float mMinScale) {
        this.mMinScale = mMinScale;
    }

    /**
     * 获取最小的缩放
     */
    @Override
    public float getMinScale() {
        return mMinScale;
    }

    /**
     * 单一方向
     *
     * @param mSingleDirection
     */
    @Override
    public void setSingleDirection(boolean mSingleDirection) {
        this.mSingleDirection = mSingleDirection;
    }

    /**
     * 单一方向
     */
    public boolean isSingleDirection() {
        return mSingleDirection;
    }

    /**
     * 是否超过父视图
     *
     * @param mOverflowParent
     */
    @Override
    public void setOverflowParent(boolean mOverflowParent) {
        this.mOverflowParent = mOverflowParent;
    }

    /**
     * 是否超过父视图
     */
    @Override
    public boolean isOverflowParent() {
        return mOverflowParent;
    }

    /**
     * 是否支持多点触摸
     *
     * @param mMultiPointerEnable
     */
    @Override
    public void setMultiPointerEnable(boolean mMultiPointerEnable) {
        this.mMultiPointerEnable = mMultiPointerEnable;
    }

    /**
     * 是否支持多点触摸
     */
    @Override
    public boolean isMultiPointerEnable() {
        return mMultiPointerEnable;
    }

    /**
     * 设置松手后的滑动事件
     *
     * @param mScrollTime
     */
    @Override
    public void setScrollTime(int mScrollTime) {
        this.mScrollTime = mScrollTime;
    }

    /**
     * 设置松手后的滑动事件
     */
    @Override
    public int getScrollTime() {
        return mScrollTime;
    }


    /**
     * 设置滑动监听
     *
     * @param mOnSlideListener
     */
    public void setOnSlideListener(OnSlideListener mOnSlideListener) {
        this.mOnSlideListener = mOnSlideListener;
    }

    /**
     * 设置背景
     *
     * @param background
     */
    @Override
    public void setBackground(Drawable background) {
        mBackground = background;
        super.setBackground(background);
        invalidate();
    }

    @Override
    public void setBackgroundColor(int color) {
        setBackground(new ColorDrawable(color));
    }

    @Override
    public void setBackgroundResource(@DrawableRes int resId) {
        Drawable drawable = resId != 0 ? ContextCompat.getDrawable(getContext(), resId) : null;
        setBackground(drawable);
    }

    /**
     * 设置前景
     *
     * @param foreground
     */
    @Override
    public void setForeground(Drawable foreground) {
        mForeground = foreground;
        invalidate();
    }

    @Override
    public void setForegroundColor(int color) {
        setForeground(new ColorDrawable(color));
    }

    @Override
    public void setForegroundResource(@DrawableRes int resId) {
        Drawable drawable = resId != 0 ? ContextCompat.getDrawable(getContext(), resId) : null;
        setForeground(drawable);
    }

    /**
     * 获取方向
     */
    @Override
    public int getDirection() {
        return mDirection;
    }

    /**
     * 设置状态栏
     * @param bg
     */
    @Override
    public void setStatusBarBackground(@Nullable Drawable bg) {
        mStatusBarBackground = bg;
        invalidate();
    }

    @Override
    public void setStatusBarBackground(@DrawableRes int resId) {
        mStatusBarBackground = resId != 0 ? ContextCompat.getDrawable(getContext(), resId) : null;
        invalidate();
    }

    @Override
    public void setStatusBarBackgroundColor(@ColorInt int color) {
        setStatusBarBackground(new ColorDrawable(color));
    }

    /**
     * 获取系统状态栏
     * @return
     */
    @Nullable
    @Override
    public Drawable getStatusBarBackground() {
        return mStatusBarBackground;
    }

    @Override
    public void slideIn(int slideEdge, int distance) {
        slideIn(EDGE_LEFT, distance, mScrollTime);
    }

    @Override
    public void slideIn(int slideEdge, int distance, int duration) {
        if (slideEdge == EDGE_LEFT) {
            smoothllyScroll(distance, 0, -distance, 0, false, duration);
        } else if (slideEdge == EDGE_RIGHT) {
            smoothllyScroll(-distance, 0, distance, 0, false, duration);
        } else if (slideEdge == EDGE_TOP) {
            smoothllyScroll(0, distance, 0, -distance, false, duration);
        } else if (slideEdge == EDGE_BOTTOM) {
            smoothllyScroll(0, -distance, 0, distance, false, duration);
        }
    }

    /**
     * 滑出父类
     *
     * @param slideEdge
     */
    @Override
    public void slideOut(int slideEdge, int distance) {
        slideOut(EDGE_RIGHT, distance, mScrollTime);
    }

    @Override
    public void slideOut(int slideEdge, int distance, int duration) {
        if (slideEdge == EDGE_LEFT) {
            smoothllyScroll(0, 0, distance, 0, duration);
        } else if (slideEdge == EDGE_RIGHT) {
            smoothllyScroll(0, 0, -distance, 0, duration);
        } else if (slideEdge == EDGE_TOP) {
            smoothllyScroll(0, 0, 0, distance, duration);
        } else if (slideEdge == EDGE_BOTTOM) {
            smoothllyScroll(0, 0, 0, -distance, duration);
        }
    }

    /**
     * 重置状态
     */
    @Override
    public void reset() {
        reset(mScrollTime);
    }

    @Override
    public void reset(int duration) {
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        smoothllyScroll(scrollX, scrollY, -scrollX, -scrollY, duration);
    }


}
