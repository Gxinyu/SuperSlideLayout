package com.example.superslidelayout.sample.basis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.superslidelayout.R;


public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ImmersiveTheme);
        setContentView(R.layout.activity_test);
    }
}
