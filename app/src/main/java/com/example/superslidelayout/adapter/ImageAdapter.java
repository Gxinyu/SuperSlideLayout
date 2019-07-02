package com.example.superslidelayout.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gexinyu
 */
public class ImageAdapter extends PagerAdapter {

    private List<ImageView> mImageList = new ArrayList();

    public ImageAdapter(List<ImageView> mImageList) {
        this.mImageList = mImageList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mImageList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(mImageList.get(arg1));
    }

    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(mImageList.get(arg1));
        return mImageList.get(arg1);
    }
}
