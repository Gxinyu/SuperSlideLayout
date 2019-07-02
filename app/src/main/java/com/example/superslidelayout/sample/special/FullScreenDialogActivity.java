package com.example.superslidelayout.sample.special;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.superslide.OnSlideListener;
import com.example.superslide.SlideLayoutImpl;
import com.example.superslide.SuperSlideLayout;
import com.example.superslidelayout.R;
import com.example.superslidelayout.adapter.CommentAdapter;
import com.example.superslidelayout.helper.WindowUtil;

public class FullScreenDialogActivity extends AppCompatActivity implements OnSlideListener {

    private SuperSlideLayout sslParent, sslChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ImmersiveTheme2);
        setContentView(R.layout.activity_full_screen_dialog);
        sslChild = findViewById(R.id.ssl_child);
        sslParent = findViewById(R.id.ssl_parent);
        sslParent.setOnSlideListener(this);
        sslChild.setOnSlideListener(this);
        //设置高度
        LinearLayout llContent = findViewById(R.id.ll_content);
        double v = WindowUtil.getScreenHeight(this, true) - WindowUtil.getStatusBarHeight(this);
        setHeight(llContent, (int) v);

        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CommentAdapter commentAdapter = new CommentAdapter(this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(commentAdapter);
    }

    /**
     * 动态设置view的高度
     *
     * @param view
     * @param height
     */
    private void setHeight(View view, int height) {
        //检测不能超越屏幕
        int heightPixels = getResources().getDisplayMetrics().heightPixels;
        if (height > heightPixels) {
            height = heightPixels;
        } else if (height <= 0) {
            height = heightPixels * 9 / 16;
        }
        ViewGroup.LayoutParams para1;
        para1 = view.getLayoutParams();
        para1.height = height;
        view.setLayoutParams(para1);
    }

    @Override
    public void onSlideStart(SlideLayoutImpl slideLayout) {
        if (slideLayout == sslChild) {
            sslParent.setBackgroundResource(R.color.transprent);
        } else if (slideLayout == sslChild) {
            sslParent.setBackgroundResource(R.color.slide_bakground_color);
        }
    }

    @Override
    public void onTouchOutside(SlideLayoutImpl slideLayout, boolean touchIntChild) {


    }

    @Override
    public void onSlideChange(SlideLayoutImpl slideLayout, float hPercent, float vPercent, float scalePercent, float alphaPercent) {
        if (slideLayout == sslChild) {
            sslParent.getStatusBarBackground().mutate().setAlpha((int) (alphaPercent * 255));
        }
    }

    @Override
    public void onSlideRecover(SlideLayoutImpl slideLayout, boolean overThreshold) {
        if (slideLayout == sslChild) {
            sslParent.setBackgroundResource(R.color.slide_bakground_color);
        }
    }

    @Override
    public void onSlideFinish(SlideLayoutImpl slideLayout) {
        onBackPressed();
    }
}
