package com.peppypals.paronbeta.StartHere;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.peppypals.paronbeta.EnterKidInfo.ActivityToFragment;
import com.peppypals.paronbeta.R;

public class StartDiscoverFragment extends Fragment {

    public StartDiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_start_home, container, false);

        Button startBtn = (Button) view.findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityToFragment.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
