package com.peppypals.paronbeta.ChatSection;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.peppypals.paronbeta.EnterKidInfo.EnterKidNameFragment;
import com.peppypals.paronbeta.MainTabs.MainTabsActivity;
import com.peppypals.paronbeta.R;

public class DeclaimFragment extends Fragment {

    public DeclaimFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_declaim, container, false);

        TextView declaimText1 = (TextView) view.findViewById(R.id.declaim1);
        declaimText1.setText(Html.fromHtml(getString(R.string.chat_declaim1)));

        TextView declaimText2 = (TextView) view.findViewById(R.id.declaimText2);
        declaimText2.setText(Html.fromHtml(getString(R.string.chat_declaim2)));

        Button backChatBtn = (Button) view.findViewById(R.id.backChatBtn);
        backChatBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Fragment fragment = new StartChatFragment();
                                             FragmentManager fragmentManager = getFragmentManager();
                                             fragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit();
                                         }
                                     }
        );

        Button nextChatBtn = (Button) view.findViewById(R.id.nextchatBtn);
        nextChatBtn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Fragment fragment = new ChooseExpertFragment();
                                               FragmentManager fragmentManager = getFragmentManager();
                                               fragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit();
                                           }
                                       }
        );

        return view;
    }


}
