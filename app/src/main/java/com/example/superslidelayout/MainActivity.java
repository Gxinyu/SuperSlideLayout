package com.example.superslidelayout;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.example.superslidelayout.sample.fragment.BasisFragment;
import com.example.superslidelayout.sample.fragment.SpecialFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Fragment[] fragments;
    private int fragmentIndex;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bnv);
        initFragment();
    }

    private void initFragment() {
        fragments = new Fragment[]{new BasisFragment(), new SpecialFragment()};
        fragmentIndex = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.mainview, fragments[0]).show(fragments[0]).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.id1: {
                if (fragmentIndex != 0) {
                    switchFragment(fragmentIndex, 0);
                    fragmentIndex = 0;
                }
                return true;
            }
            case R.id.id2: {
                if (fragmentIndex != 1) {
                    switchFragment(fragmentIndex, 1);
                    fragmentIndex = 1;
                }
                return true;
            }
        }

        return false;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if (fragments[index].isAdded() == false) {
            transaction.add(R.id.mainview, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

}
