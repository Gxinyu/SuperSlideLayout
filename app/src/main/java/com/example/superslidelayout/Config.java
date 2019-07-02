package com.example.superslidelayout;


import com.example.superslide.SuperSlideLayout;

public class Config {

    //支持滑动
    public static boolean SLIDE_ENABLE = true;
    //边缘
    public static int SLIDE_EDGE = Config.EDGE_LEFT;
    //默认阈值
    public static float SLIDE_THRESHOLD_RATE = 0.382F;
    //是否可以溢出父类
    public static boolean SLIDE_OVER_PARENT = false;
    //单一方向滑动
    public static boolean SLIDE_SINGLE_DIRECTION = true;
    //多点触摸
    public static boolean SLIDE_MULTI_TOUCH = true;
    //缩放
    public static boolean SCALE_ENABLE = false;
    public static float SCALE_RATE = 0.5F;
    public static float SCALE_MIN_SIZE = 0.3F;
    //背景透明度
    public static boolean ALPHA_ENABLE = true;
    public static float ALPHA_RATE = 0.5F;
    public static float ALPHA_MIN_SIZE = 0;
    //松手之后的滚动时间
    public static int SCROLL_TIME = 360;

    /**
     * 一共会有15中情况
     */
    //一个方向一面
    public static final int EDGE_LEFT = SuperSlideLayout.EDGE_LEFT;
    public static final int EDGE_RIGHT = SuperSlideLayout.EDGE_RIGHT;
    public static final int EDGE_TOP = SuperSlideLayout.EDGE_TOP;
    public static final int EDGE_BOTTOM = SuperSlideLayout.EDGE_BOTTOM;
    //同方向两面
    public static final int EDGE_LEFT_RIGHT = SuperSlideLayout.EDGE_LEFT | SuperSlideLayout.EDGE_RIGHT;
    public static final int EDGE_TOP_BOTTOM = SuperSlideLayout.EDGE_TOP | SuperSlideLayout.EDGE_BOTTOM;
    //互斥两面
    public static final int EDGE_LEFT_TOP = SuperSlideLayout.EDGE_LEFT | SuperSlideLayout.EDGE_TOP;
    public static final int EDGE_LEFT_BOTTOM = SuperSlideLayout.EDGE_LEFT | SuperSlideLayout.EDGE_BOTTOM;
    public static final int EDGE_RIGHT_TOP = SuperSlideLayout.EDGE_RIGHT | SuperSlideLayout.EDGE_TOP;
    public static final int EDGE_RIGHT_BOTTOM = SuperSlideLayout.EDGE_RIGHT | SuperSlideLayout.EDGE_BOTTOM;
    //三面
    public static final int EDGE_LEFT_RIGHT_TOP = SuperSlideLayout.EDGE_LEFT | SuperSlideLayout.EDGE_RIGHT | SuperSlideLayout.EDGE_TOP;
    public static final int EDGE_LEFT_RIGHT_BOTTOM = SuperSlideLayout.EDGE_LEFT | SuperSlideLayout.EDGE_RIGHT | SuperSlideLayout.EDGE_BOTTOM;
    public static final int EDGE_TOP_BOTTOM_LEFT = SuperSlideLayout.EDGE_TOP | SuperSlideLayout.EDGE_BOTTOM | SuperSlideLayout.EDGE_LEFT;
    public static final int EDGE_TOP_BOTTOM_RIGHT = SuperSlideLayout.EDGE_TOP | SuperSlideLayout.EDGE_BOTTOM | SuperSlideLayout.EDGE_RIGHT;
    //四面
    public static final int EDGE_ALL = SuperSlideLayout.EDGE_TOP | SuperSlideLayout.EDGE_BOTTOM | SuperSlideLayout.EDGE_LEFT | SuperSlideLayout.EDGE_RIGHT;

}
