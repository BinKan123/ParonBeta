package com.peppypals.paronbeta.StartHere;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.peppypals.paronbeta.MainTabs.ContactFragment;
import com.peppypals.paronbeta.MainTabs.DiscoverFragment;
import com.peppypals.paronbeta.MainTabs.HomeFragment;
import com.peppypals.paronbeta.MainTabs.MyPageFragment;
import com.peppypals.paronbeta.MainTabs.NavPageAdapter;
import com.peppypals.paronbeta.R;

public class StartHereActivity extends AppCompatActivity {

        private NavPageAdapter navPageAdapter;
        private ViewPager mViewPager;
        private TabLayout tabLayout;
        public int[] tabIcons = {
                R.drawable.home,
                R.drawable.search,
                R.drawable.my,
                R.drawable.chat
        };


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_tabs);

            //use viewpager to create navigationbar

            mViewPager = (ViewPager) findViewById(R.id.container);
            tabLayout = (TabLayout) findViewById(R.id.tabs);

            navPageAdapter = new NavPageAdapter(getSupportFragmentManager());

            setupViewPager(mViewPager);

            tabLayout.setupWithViewPager(mViewPager);

            createTabIcons();

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    mViewPager.setCurrentItem(tab.getPosition());
                    tab.getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)  ;
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    tab.getIcon().setColorFilter(getResources().getColor(R.color.bg_on_btn),PorterDuff.Mode.SRC_IN)  ;
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }

            });


        }

        //viewpager
    private void setupViewPager(ViewPager viewPager){
        NavPageAdapter adapter=new NavPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new StartHomeFragment(),"Hem");
        adapter.addFragment(new StartDiscoverFragment(),"Utforska");
        adapter.addFragment(new StartMyPageFragment(),"Min sida");
        adapter.addFragment(new StartContactFragment(),"Kontakt");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
    }

    private void createTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }



}
