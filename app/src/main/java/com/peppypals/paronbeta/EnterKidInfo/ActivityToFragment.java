package com.peppypals.paronbeta.EnterKidInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by kanbi on 22/03/2018.
 */

public class ActivityToFragment extends FragmentActivity {
    FragmentTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
            .add(android.R.id.content, new AddKidFragment()).commit();}


        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content,new AddKidFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
   /* @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStackImmediate();
        }
        else{
            super.onBackPressed();
        }
    }*/

   /* public void addFragmentOnTop(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }*/
}
