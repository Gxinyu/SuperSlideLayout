package com.example.superslidelayout.sample.basis;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import com.example.superslidelayout.R;
import com.example.superslidelayout.adapter.ImageAdapter;
import com.example.superslidelayout.base.BaseActivity;
import com.example.superslidelayout.helper.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends BaseActivity {

    private List<ImageView> mImageList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ViewPager viewPager = findViewById(R.id.view_pager);
        List<Bitmap> singleLineBitmap = DataHelper.getSingleLineBitmap(this, 5);
        int size = singleLineBitmap.size();
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(singleLineBitmap.get(i));
            mImageList.add(imageView);
        }
        ImageAdapter imageAdapter = new ImageAdapter(mImageList);
        viewPager.setAdapter(imageAdapter);
    }
}
