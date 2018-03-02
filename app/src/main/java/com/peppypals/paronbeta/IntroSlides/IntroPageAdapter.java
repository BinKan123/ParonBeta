package com.peppypals.paronbeta.IntroSlides;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by kanbi on 26/02/2018.
 */

public class IntroPageAdapter extends FragmentPagerAdapter {

    private Context context;
    public static int SLIDES_NUM=3;
    public IntroPageAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        context=context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new Fragment();
        switch(position){
            case 0:
                f= IntroSlideOneFragment.newInstance(context);
                break;
            case 1:
                f= IntroSlideTwoFragment.newInstance(context);
                break;
            case 2:
                f= IntroSlideThreeFragment.newInstance(context);
                break;
        }
        return f;
    }
    @Override
    public int getCount() {
        return SLIDES_NUM;
    }
}
