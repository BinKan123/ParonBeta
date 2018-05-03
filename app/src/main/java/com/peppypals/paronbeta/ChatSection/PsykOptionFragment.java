package com.peppypals.paronbeta.ChatSection;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.peppypals.paronbeta.R;

public class PsykOptionFragment extends Fragment {

    private static final String CHOSEN_BG = "#dfedcd";
    public PsykOptionFragment () {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_psyk_option, container, false);

        ImageView priceImg1 = (ImageView) view.findViewById(R.id.priceImg1);
        priceImg1.setColorFilter(Color.parseColor(CHOSEN_BG));

        ImageView check1 = (ImageView) view.findViewById(R.id.check1);
        check1.setVisibility(view.getVisibility());

        


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
