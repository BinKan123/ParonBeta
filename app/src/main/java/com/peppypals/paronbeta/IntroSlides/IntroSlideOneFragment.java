package com.peppypals.paronbeta.IntroSlides;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.peppypals.paronbeta.EmailLoginActivity;
import com.peppypals.paronbeta.LoginActivity;
import com.peppypals.paronbeta.R;


public class IntroSlideOneFragment extends Fragment {


    public static Fragment newInstance(Context context) {
        IntroSlideOneFragment introSlideOneFragment = new IntroSlideOneFragment();
        return introSlideOneFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_intro_slide_one, container, false);

        TextView firstIntroText = (TextView) view.findViewById(R.id.intro1Text);
        firstIntroText.setText(Html.fromHtml(getString(R.string.first_intro_text)));

        TextView loginText = (TextView) view.findViewById(R.id.loginText);

        SpannableString spannableString =
                new SpannableString(getString(R.string.login_text));

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(getContext(), EmailLoginActivity.class);
                startActivity(intent);
            }
        };
        spannableString.setSpan(clickableSpan, 25, spannableString.length(), 0);
        loginText.setMovementMethod(LinkMovementMethod.getInstance());
        loginText.setText(spannableString);

        Button createAccBtn = (Button) view.findViewById(R.id.createAccBtn);
        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), LoginActivity.class);
                getActivity().startActivity(intent);
            }
        }
        );

        return view;
    }


}


