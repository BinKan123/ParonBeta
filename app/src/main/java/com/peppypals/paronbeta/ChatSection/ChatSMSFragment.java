package com.peppypals.paronbeta.ChatSection;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peppypals.paronbeta.R;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;

import static com.google.common.collect.TreeRangeMap.create;


public class ChatSMSFragment extends Fragment {

    public ChatSMSFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
    final View view = inflater.inflate(R.layout.fragment_chat_sm, container, false);

        UseTwillioSMS();

        return view;
    }

    private void UseTwillioSMS(){
        final String ACCOUNT_SID = "ACbea00286cc300637d83e3ab66bfd126b";
        final String AUTH_TOKEN = "f501bcdedd09b48c129c9005ad41fdb0";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+46707122145"),   // To number
                new com.twilio.type.PhoneNumber("+46769449169"),
                "This is the ship that made the Kessel Run in fourteen parsecs?")
                .create();


        System.out.println(message.getSid());
    }

}
