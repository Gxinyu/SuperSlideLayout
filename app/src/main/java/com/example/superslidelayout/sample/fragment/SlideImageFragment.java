package com.example.superslidelayout.sample.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.superslide.OnSlideListener;
import com.example.superslide.SlideLayoutImpl;
import com.example.superslide.SuperSlideLayout;
import com.example.superslidelayout.R;
import com.example.superslidelayout.base.BaseFragment;
import com.example.superslidelayout.helper.DataHelper;
import com.example.superslidelayout.helper.ListBean;
import com.example.superslidelayout.sample.special.ShareElementActivity;

import java.util.List;

public class SlideImageFragment extends BaseFragment implements OnSlideListener {

    private int index;
    private ImageView imageView;
    private ShareElementActivity elementActivity;

    public static SlideImageFragment getInstance(int i) {
        SlideImageFragment imageFragment = new SlideImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", i);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        elementActivity= (ShareElementActivity) context;
    }

    @Override
    protected void getArgument(Bundle arguments) {
        index = arguments.getInt("index");
    }

    @Override
    protected int createLayout() {
        return R.layout.fragment_slide_image;
    }

    @Override
    protected void initView(View rootView) {
        SuperSlideLayout superSlideview = rootView.findViewById(R.id.super_slideview);
        superSlideview.setOnSlideListener(this);

        imageView = rootView.findViewById(R.id.imageView);
        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                getActivity().supportStartPostponedEnterTransition();
                return true;
            }
        });
    }

    @Override
    protected void initListener() {
        //具体情况看项目中怎么使用
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().supportFinishAfterTransition();
//            }
//        });

    }

    @Override
    protected void fineLoadData(boolean isVisible) {

    }

    @Override
    public void onResume() {
        super.onResume();
        //设置适配器
        List<ListBean> bitmapList = DataHelper.getBitmapList(getActivity());
        Glide.with(getActivity())
                .load(bitmapList.get(index).bitmap)
                .into(imageView);
    }

    public View getSharedElement() {
        return imageView;
    }

    @Override
    public void onSlideStart(SlideLayoutImpl slideLayout) {

    }

    @Override
    public void onTouchOutside(SlideLayoutImpl slideLayout, boolean touchIntChild) {

    }

    @Override
    public void onSlideChange(SlideLayoutImpl slideLayout, float hPercent, float vPercent, float scalePercent, float alphaPercent) {

    }

    @Override
    public void onSlideRecover(SlideLayoutImpl slideLayout, boolean overThreshold) {
        if(overThreshold) {
            elementActivity.onBackPressed();
        }
    }

    @Override
    public void onSlideFinish(SlideLayoutImpl slideLayout) {

    }
}
