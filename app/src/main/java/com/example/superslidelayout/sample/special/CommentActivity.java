package com.example.superslidelayout.sample.special;

import android.os.Bundle;
import android.view.View;

import com.example.superslidelayout.R;
import com.example.superslidelayout.base.BaseActivity;
import com.example.superslidelayout.helper.WindowUtil;
import com.example.superslidelayout.widget.BottomDialog;


public class CommentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
    }

    /**
     * 有渐变背景
     *
     * @param view
     */
    public void showComment(View view) {
        BottomDialog bottomDialog = new BottomDialog(this);
        bottomDialog.show();
    }

    /**
     * 无背景
     *
     * @param view
     */
    public void showComment2(View view) {
        BottomDialog bottomDialog = new BottomDialog(this);
        bottomDialog.setBacground(R.color.transprent);
        bottomDialog.show();
    }

    /**
     * 全屏
     * 也可以去掉状态栏。自由设置即可
     *
     * @param view
     */
    public void showComment3(View view) {
        double v = WindowUtil.getScreenHeight(this, true) - WindowUtil.getStatusBarHeight(this);
        BottomDialog bottomDialog = new BottomDialog(this);
        bottomDialog.setHeight((int) v);
        bottomDialog.show();
    }

    /**
     * 点击外部不消失
     *
     * @param view
     */
    public void showComment4(View view) {
        BottomDialog bottomDialog = new BottomDialog(this);
        bottomDialog.setCancleOutside(false);
        bottomDialog.show();
    }

    /**
     * 点击返回键不消失
     *
     * @param view
     */
    public void showComment5(View view) {
        BottomDialog bottomDialog = new BottomDialog(this);
        bottomDialog.setCancleBackpress(false);
        bottomDialog.show();
    }
}
