package com.example.superslidelayout.sample.basis;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.example.superslidelayout.R;
import com.example.superslidelayout.adapter.RecyclerAdapter;
import com.example.superslidelayout.base.BaseActivity;
import com.example.superslidelayout.helper.DataHelper;
import com.example.superslidelayout.helper.ListBean;
import java.util.List;


public class RecyclerActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private RecyclerView mRecyclerView;
    private List<ListBean> bitmapList;
    private RecyclerAdapter recyclerAdapter;
    private Switch switchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        switchButton = findViewById(R.id.switchButton);
        mRecyclerView = findViewById(R.id.recyclerView);
        //获取数据
        bitmapList = DataHelper.getBitmapList(this);
        recyclerAdapter = new RecyclerAdapter(this, bitmapList);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(recyclerAdapter);
        //设置监听
        switchButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        }else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        mRecyclerView.setAdapter(recyclerAdapter);
    }
}
