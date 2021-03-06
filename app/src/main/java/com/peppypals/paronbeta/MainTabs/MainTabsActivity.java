package com.peppypals.paronbeta.MainTabs;

import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.peppypals.paronbeta.CategoryList.CategoryActivity;
import com.peppypals.paronbeta.EnterKidInfo.ActivityToFragment;
import com.peppypals.paronbeta.LoginActivity;
import com.peppypals.paronbeta.R;
import com.peppypals.paronbeta.SplashActivity;

public class MainTabsActivity extends AppCompatActivity {
    private static final String TAG = "MainTabsActivity";

    private BottomNavigationView bottomNavigation;
    private android.support.v4.app.FragmentManager fragmentManager;

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
//
//        //use viewpager to create navigationbar
//
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
                tab.getIcon().setColorFilter(Color.GRAY,PorterDuff.Mode.SRC_IN)  ;
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


    }

//    //viewpager
    private void setupViewPager(ViewPager viewPager){
        NavPageAdapter adapter=new NavPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(),"");
        adapter.addFragment(new DiscoverFragment(),"");
        adapter.addFragment(new MyPageFragment(),"");
        adapter.addFragment(new ContactFragment(),"");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
    }

    private void createTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);

        return;
    }

        //  use FragmentTransaction to make navigationbar
        //Navigation Fragment

        /*bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigation.inflateMenu(R.menu.menu_main);
        fragmentManager = getSupportFragmentManager();
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment fragment = null;
                switch (id) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.search:
                        fragment = new DiscoverFragment();
                        break;
                    case R.id.my:
                        fragment = new MyPageFragment();
                        break;
                    case R.id.chat:
                        fragment = new ContactFragment();
                        break;

                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.replace(R.id.fragment_container, new HomeFragment()).commit();
                return true;
            }
        });

        //to ensure new StartFragment() shows up by default
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new HomeFragment()).commit();*/
        //  }


}
