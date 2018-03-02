package com.peppypals.paronbeta;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends FragmentActivity {

    private ViewPager viewPager;
    private com.peppypals.paronbeta.IntroSlides.IntroPageAdapter pageAdapter;
    private Button dot1,dot2,dot3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpView();

    }

    private void setUpView(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pageAdapter = new com.peppypals.paronbeta.IntroSlides.IntroPageAdapter(getApplicationContext(),getSupportFragmentManager());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0);

    }

}
