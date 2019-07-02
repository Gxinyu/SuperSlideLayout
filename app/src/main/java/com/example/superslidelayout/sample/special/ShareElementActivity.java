package com.example.superslidelayout.sample.special;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.superslidelayout.R;
import com.example.superslidelayout.base.BaseFragmentAdapter;
import com.example.superslidelayout.helper.DataHelper;
import com.example.superslidelayout.helper.ListBean;
import com.example.superslidelayout.helper.RxBus;
import com.example.superslidelayout.helper.ScrollEvent;
import com.example.superslidelayout.sample.fragment.SlideImageFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShareElementActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private int selectedIndex;
    private ViewPager viewPager;
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ImmersiveTransitionTheme);
        setContentView(R.layout.activity_share_element);
        viewPager = findViewById(R.id.view_pager);
        selectedIndex = getIntent().getIntExtra("index", 0);

        //设置数据
        List<ListBean> bitmapList = DataHelper.getBitmapList(this);
        int size = bitmapList.size();
        for (int i = 0; i < size; i++) {
            mFragmentList.add(SlideImageFragment.getInstance(i));
        }
        //设置适配器
        BaseFragmentAdapter fragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager(), mFragmentList);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(selectedIndex);
        viewPager.addOnPageChangeListener(this);
        initShareElement();
    }

    private void initShareElement() {
        if (Build.VERSION.SDK_INT >= 21) {
            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    SlideImageFragment fragment = (SlideImageFragment) mFragmentList.get(viewPager.getCurrentItem());
                    sharedElements.clear();
                    sharedElements.put(viewPager.getCurrentItem() + "", fragment.getSharedElement());
                }
            });
        }
    }


    @Override
    public void supportFinishAfterTransition() {
        Intent data = new Intent();
        data.putExtra("index", viewPager.getCurrentItem());
        setResult(RESULT_OK, data);
        super.supportFinishAfterTransition();
    }


    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("index", viewPager.getCurrentItem());
        setResult(RESULT_OK, data);
        super.supportFinishAfterTransition();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        RxBus.get().post(new ScrollEvent(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
