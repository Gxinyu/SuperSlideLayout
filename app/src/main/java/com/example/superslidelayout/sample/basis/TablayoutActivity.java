package com.example.superslidelayout.sample.basis;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import com.example.superslidelayout.R;
import com.example.superslidelayout.base.BaseActivity;
import com.example.superslidelayout.base.BaseFragmentAdapter;
import com.example.superslidelayout.sample.fragment.ImageFragment;

import java.util.ArrayList;
import java.util.List;

public class TablayoutActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager vpChannel;
    private List<String> mTabNames = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout);
        mTabLayout =findViewById(R.id.tl_channel);
        vpChannel = findViewById(R.id.vp_channel);

        for (int i = 0; i < 10; i++) {
            mTabNames.add("秦时明月");
            mFragmentList.add(ImageFragment.getInstance(i));
        }
        //设置适配器
        BaseFragmentAdapter fragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager(), mTabNames,mFragmentList);
        vpChannel.setAdapter(fragmentAdapter);
        mTabLayout.setupWithViewPager(vpChannel);
    }
}
