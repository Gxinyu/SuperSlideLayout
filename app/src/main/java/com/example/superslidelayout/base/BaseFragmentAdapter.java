package com.example.superslidelayout.base;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gexinyu
 */
public class BaseFragmentAdapter extends FragmentStatePagerAdapter {

    private List<String> mTabNames = new ArrayList<>();
    private List<Fragment> mFragmens = new ArrayList<Fragment>();


    public BaseFragmentAdapter(FragmentManager fm, List<Fragment> mList) {
        super(fm);
        this.mFragmens = mList;
    }


    public BaseFragmentAdapter(FragmentManager fm, List<String> mTabNames, List<Fragment> mFragmens) {
        super(fm);
        this.mTabNames = mTabNames;
        this.mFragmens = mFragmens;
    }


    @Override
    public Fragment getItem(int i) {
        return mFragmens.get(i);
    }

    @Override
    public int getCount() {
        return mFragmens != null ? mFragmens.size() : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTabNames != null && mTabNames.size() > 0) {
            return mTabNames.get(position);
        }

        return super.getPageTitle(position);
    }
}
