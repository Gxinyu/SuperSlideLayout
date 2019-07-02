package com.example.superslidelayout.sample.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.superslidelayout.R;
import com.example.superslidelayout.base.BaseFragment;
import com.example.superslidelayout.helper.DataHelper;

public class ImageFragment extends BaseFragment {

    private int index;

    /**
     * 获取
     *
     * @param i
     * @return
     */
    public static Fragment getInstance(int i) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", i);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    protected void getArgument(Bundle arguments) {
        index = arguments.getInt("index");
    }

    @Override
    protected int createLayout() {
        return R.layout.fragment_image;
    }

    @Override
    protected void initView(View rootView) {
        ImageView imageView = rootView.findViewById(R.id.imageView);
        Bitmap bitmapIndex = DataHelper.getBitmapIndex(mActivity, index);

        Glide.with(mActivity)
                .load(bitmapIndex)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(15)))
                .into(imageView);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mActivity, "长安", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void fineLoadData(boolean isVisible) {

    }
}
