package com.example.superslidelayout.sample.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.superslidelayout.R;
import com.example.superslidelayout.base.BaseFragment;
import com.example.superslidelayout.helper.ActivityHelpter;
import com.example.superslidelayout.sample.basis.CoordinatorActivity;
import com.example.superslidelayout.sample.basis.DrawerActivity;
import com.example.superslidelayout.sample.basis.GridActivity;
import com.example.superslidelayout.sample.basis.HScrollViewActivity;
import com.example.superslidelayout.sample.basis.ListViewActivity;
import com.example.superslidelayout.sample.basis.RecyclerActivity;
import com.example.superslidelayout.sample.basis.ScrollViewActivity;
import com.example.superslidelayout.sample.basis.TablayoutActivity;
import com.example.superslidelayout.sample.basis.ViewPagerActivity;
import com.example.superslidelayout.sample.basis.WebviewActivity;


public class BasisFragment extends BaseFragment implements View.OnClickListener {

    @Override
    protected void getArgument(Bundle arguments) {

    }

    @Override
    protected int createLayout() {
        return R.layout.fragment_basis;
    }

    @Override
    protected void initView(View rootView) {
        rootView.findViewById(R.id.btn_viewpager).setOnClickListener(this);
        rootView.findViewById(R.id.btn_listview).setOnClickListener(this);
        rootView.findViewById(R.id.btn_grid).setOnClickListener(this);
        rootView.findViewById(R.id.btn_recyclerview).setOnClickListener(this);
        rootView.findViewById(R.id.btn_scrollview).setOnClickListener(this);
        rootView.findViewById(R.id.btn_hscrollview).setOnClickListener(this);
        rootView.findViewById(R.id.btn_webview).setOnClickListener(this);
        rootView.findViewById(R.id.btn_tablayout).setOnClickListener(this);
        rootView.findViewById(R.id.btn_drawer).setOnClickListener(this);
        rootView.findViewById(R.id.btn_coordinatorlayout).setOnClickListener(this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void fineLoadData(boolean isVisible) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_viewpager) {
            ActivityHelpter.jumpTarget(mActivity, ViewPagerActivity.class);
        } else if (id == R.id.btn_listview) {
            ActivityHelpter.jumpTarget(mActivity, ListViewActivity.class);
        } else if (id == R.id.btn_grid) {
            ActivityHelpter.jumpTarget(mActivity, GridActivity.class);
        } else if (id == R.id.btn_recyclerview) {
            ActivityHelpter.jumpTarget(mActivity, RecyclerActivity.class);
        } else if (id == R.id.btn_scrollview) {
            ActivityHelpter.jumpTarget(mActivity, ScrollViewActivity.class);
        } else if (id == R.id.btn_hscrollview) {
            ActivityHelpter.jumpTarget(mActivity, HScrollViewActivity.class);
        } else if (id == R.id.btn_webview) {
            ActivityHelpter.jumpTarget(mActivity, WebviewActivity.class);
        } else if (id == R.id.btn_tablayout) {
            ActivityHelpter.jumpTarget(mActivity, TablayoutActivity.class);
        } else if (id == R.id.btn_drawer) {
            ActivityHelpter.jumpTarget(mActivity, DrawerActivity.class);
        } else if (id == R.id.btn_coordinatorlayout) {
            ActivityHelpter.jumpTarget(mActivity, CoordinatorActivity.class);
        }
    }
}
