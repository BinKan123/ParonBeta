package com.peppypals.paronbeta.ChatSection;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.peppypals.paronbeta.EnterKidInfo.EnterKidNameFragment;
import com.peppypals.paronbeta.R;

public class ChooseExpertFragment extends Fragment {

    public ChooseExpertFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_choose_expert, container, false);

        Button backDelaimBtn = (Button) view.findViewById(R.id.backDeclaimBtn);
        backDelaimBtn.setOnClickListener(new View.OnClickListener() {
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
