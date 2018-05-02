package com.peppypals.paronbeta.ChatSection;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peppypals.paronbeta.R;

public class ShortWordsFragment extends Fragment {

    public ShortWordsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_short_words, container, false);
//
//        Button addKidBtn = (Button) view.findViewById(R.id.addKidBtn);
//        addKidBtn.setOnClickListener(new View.OnClickListener() {
//                                         @Override
//                                         public void onClick(View v) {
//                                             Fragment fragment = new EnterKidNameFragment();
//                                             FragmentManager fragmentManager = getFragmentManager();
//                                             fragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit();
//                                         }
//                                     }
//        );
        return view;
    }
}
