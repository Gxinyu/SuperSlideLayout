package com.example.superslidelayout.sample.basis;

import android.os.Bundle;
import android.widget.ListView;
import com.example.superslidelayout.R;
import com.example.superslidelayout.adapter.ListViewAdapter;
import com.example.superslidelayout.base.BaseActivity;
import com.example.superslidelayout.helper.DataHelper;
import com.example.superslidelayout.helper.ListBean;

import java.util.List;


public class ListViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        ListView listView = findViewById(R.id.listView);
        List<ListBean> bitmapList = DataHelper.getBitmapList(this);
        ListViewAdapter listAdapter = new ListViewAdapter(this, bitmapList);
        listView.setAdapter(listAdapter);
    }
}
