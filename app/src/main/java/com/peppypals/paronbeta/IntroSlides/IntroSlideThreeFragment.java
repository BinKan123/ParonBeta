package com.peppypals.paronbeta.IntroSlides;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.peppypals.paronbeta.EmailLoginActivity;
import com.peppypals.paronbeta.LoginActivity;
import com.peppypals.paronbeta.R;

public class IntroSlideThreeFragment extends Fragment {

    public static Fragment newInstance(Context context) {
        IntroSlideThreeFragment introSlideThreeFragment = new IntroSlideThreeFragment();

        return introSlideThreeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_intro_slide_three, container, false);

        TextView thirdIntroText = (TextView) view.findViewById(R.id.intro3Text);
        thirdIntroText.setText(Html.fromHtml(getString(R.string.third_intro_text)));

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

        Button startBtn = (Button) view.findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
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
