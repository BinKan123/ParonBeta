package com.peppypals.paronbeta.ChatSection;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.peppypals.paronbeta.R;


public class ChatSectionActivity extends AppCompatActivity {
    FragmentTransaction transaction;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new StartChatFragment()).commit();}






        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content,new StartChatFragment());
        transaction.addToBackStack(null);
        transaction.commit();





    }



}
