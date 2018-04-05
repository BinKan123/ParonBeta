package com.peppypals.paronbeta.MainTabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanbi on 30/03/2018.
 */

public class NavPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mfragmentList=new ArrayList<>();
    private final List<String> mFragmentTitleList=new ArrayList<>();

    public void addFragment(Fragment fragment,String title){
        mfragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public NavPageAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public CharSequence getPageTitle(int position){
        return mFragmentTitleList.get(position);
    }


    @Override
    public Fragment getItem(int position) {
        return mfragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mfragmentList.size();
    }


}

