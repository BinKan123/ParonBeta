package com.peppypals.paronbeta.MainTabs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.peppypals.paronbeta.ChatSection.ChatSectionActivity;
import com.peppypals.paronbeta.EnterKidInfo.ActivityToFragment;
import com.peppypals.paronbeta.R;

public class ContactFragment extends Fragment {

    public ContactFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_contact, container, false);

        TextView extraDetailsText = (TextView) view.findViewById(R.id.extraDetails);
        extraDetailsText.setText(Html.fromHtml(getString(R.string.extra_details_Info)));

        Button chatBtn = (Button) view.findViewById(R.id.chatBtn);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatSectionActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


}
