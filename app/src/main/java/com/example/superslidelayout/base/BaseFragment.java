package com.example.superslidelayout.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author gexinyu
 */

public abstract class BaseFragment extends Fragment {

    protected View rootView;
    public FragmentActivity mActivity;
    public boolean mHaveLoadData; // 表示是否已经请求过数据
    public boolean mIsVisibleToUser = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 如果还没有加载过数据 && 用户切换到了这个fragment
        // 那就开始加载数据
        if (!mHaveLoadData && isVisibleToUser) {
            mIsVisibleToUser = isVisibleToUser;
            mHaveLoadData = true;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (FragmentActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //传值
        Bundle arguments = getArguments();
        getArgument(arguments);
        //绑定view
        View rootView = inflater.inflate(createLayout(), null);
        initView(rootView);
        initListener();
        fineLoadData(mIsVisibleToUser);
        return rootView;
    }


    /**************************************************************
     *  自定义的回调方法，子类可根据需求重写
     *************************************************************/

    /**
     * 绑定控件
     *
     * @param id
     * @param <T>
     * @return
     */
    public final <T extends View> T findViewById(@IdRes int id) {
        if (id == -1) {
            return null;
        }
        return rootView.findViewById(id);
    }

    /**************************************************************
     *  子类需要复写的方法
     *************************************************************/
    protected abstract void getArgument(Bundle arguments);

    protected abstract @LayoutRes
    int createLayout();

    protected abstract void initView(View rootView);

    protected abstract void initListener();

    protected abstract void fineLoadData(boolean isVisible);

}
