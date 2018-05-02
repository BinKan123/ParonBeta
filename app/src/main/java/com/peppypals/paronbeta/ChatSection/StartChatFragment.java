package com.peppypals.paronbeta.ChatSection;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.peppypals.paronbeta.R;


public class StartChatFragment extends Fragment {

    public StartChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_start_chat, container, false);

        Button startChatBtn = (Button) view.findViewById(R.id.startChatBtn);
        startChatBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Fragment fragment = new DeclaimFragment();
                                             FragmentManager fragmentManager = getFragmentManager();
                                             fragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit();
                                         }
                                     }
        );

        return view;
    }


}