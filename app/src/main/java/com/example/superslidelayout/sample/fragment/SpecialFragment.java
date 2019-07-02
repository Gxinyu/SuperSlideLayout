package com.example.superslidelayout.sample.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.superslidelayout.R;
import com.example.superslidelayout.base.BaseFragment;
import com.example.superslidelayout.helper.ActivityHelpter;
import com.example.superslidelayout.sample.basis.GridActivity;
import com.example.superslidelayout.sample.special.CommentActivity;
import com.example.superslidelayout.sample.special.FullScreenDialogActivity;
import com.example.superslidelayout.sample.special.GalleryActivity;
import com.example.superslidelayout.sample.special.SingleImgActivity;
import com.example.superslidelayout.sample.special.VideoDetailActivity;

public class SpecialFragment extends BaseFragment implements View.OnClickListener {

    private Button btnGallery;
    private Button btnComment, btnFull;
    private ImageView btnSingle, btnSingleVideo;
    private Button btnShareElement;

    @Override
    protected void getArgument(Bundle arguments) {

    }

    @Override
    protected int createLayout() {
        return R.layout.fragment_special;
    }

    @Override
    protected void initView(View rootView) {
        btnGallery = rootView.findViewById(R.id.btn_gallery);
        btnComment = rootView.findViewById(R.id.btn_comment);
        btnFull = rootView.findViewById(R.id.btn_full_comment);
        btnSingle = rootView.findViewById(R.id.btn_single);
        btnSingleVideo = rootView.findViewById(R.id.btn_single_video);
        btnShareElement = rootView.findViewById(R.id.btn_share_element);

    }

    @Override
    protected void initListener() {
        btnGallery.setOnClickListener(this);
        btnComment.setOnClickListener(this);
        btnFull.setOnClickListener(this);
        btnSingle.setOnClickListener(this);
        btnSingleVideo.setOnClickListener(this);
        btnShareElement.setOnClickListener(this);
    }

    @Override
    protected void fineLoadData(boolean isVisible) {

    }

    @Override
    public void onClick(View v) {
        if (v == btnGallery) {
            ActivityHelpter.jumpTarget(mActivity, GalleryActivity.class);
        } else if (v == btnComment) {
            ActivityHelpter.jumpTarget(mActivity, CommentActivity.class);
        } else if (v == btnFull) {
            ActivityHelpter.jumpTarget(mActivity, FullScreenDialogActivity.class);
        } else if (v == btnSingle) {
            Intent intent = new Intent(mActivity, SingleImgActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(mActivity, btnSingle, "single");
            startActivity(intent, options.toBundle());
        } else if (v == btnSingleVideo) {
            Intent intent = new Intent(mActivity, VideoDetailActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(mActivity, btnSingleVideo, "video");
            startActivity(intent, options.toBundle());
        } else if (v == btnShareElement) {
            ActivityHelpter.jumpTarget(mActivity, GridActivity.class);
        }
    }
}
