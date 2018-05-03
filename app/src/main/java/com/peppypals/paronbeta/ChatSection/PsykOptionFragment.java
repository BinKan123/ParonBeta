package com.peppypals.paronbeta.ChatSection;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.peppypals.paronbeta.R;

public class PsykOptionFragment extends Fragment {

    private static final String CHOSEN_BG = "#dfedcd";
    private static final String NOT_CHOSEN_BG = "#f6f1ea";

    private ImageView priceImg1;
    private ImageView check1;
    private TextView timePrice1;
    private ImageView timer1;

    private ImageView priceImg2;
    private ImageView check2;
    private TextView timePrice2;
    private ImageView timer2;

    private ImageView priceImg3;
    private ImageView check3;
    private TextView timePrice3;
    private ImageView timer3;

    private ImageView pedaOption;

    public PsykOptionFragment () {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_psyk_option, container, false);

        priceImg1 = (ImageView) view.findViewById(R.id.priceImg1pe);
        check1 = (ImageView) view.findViewById(R.id.check1);
        timePrice1 = (TextView) view.findViewById(R.id.timeprice2pe);
        timer1 = (ImageView) view.findViewById(R.id.timer1);

        option1On(view);

        priceImg2 = (ImageView) view.findViewById(R.id.priceImg2pe);
        check2 = (ImageView) view.findViewById(R.id.check2);
        timePrice2 = (TextView) view.findViewById(R.id.timeprice1pe);
        timer2 = (ImageView) view.findViewById(R.id.timer2);

        priceImg3 = (ImageView) view.findViewById(R.id.priceImg3pe);
        check3 = (ImageView) view.findViewById(R.id.check3);
        timePrice3 = (TextView) view.findViewById(R.id.timeprice3);
        timer3 = (ImageView) view.findViewById(R.id.timer3);

        priceImg1.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              option1On(view);
                                              option2Off(view);
                                              option3Off(view);

                                          }
                                      }
        );

        priceImg2.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             option2On(view);
                                             option1Off(view);
                                             option3Off(view);

                                         }
                                     }
        );

        priceImg3.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             option3On(view);
                                             option1Off(view);
                                             option2Off(view);

                                         }
                                     }
        );

        Button backDeclaim = (Button) view.findViewById(R.id.backDeclBtn);
        backDeclaim.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Fragment fragment = new DeclaimFragment();
                                              FragmentManager fragmentManager = getFragmentManager();
                                              fragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit();
                                          }
                                      }
        );

        pedaOption = (ImageView) view.findViewById(R.id.pedagogOption);
        pedaOption.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Fragment fragment = new PedaOptionFragment();
                                              FragmentManager fragmentManager = getFragmentManager();
                                              fragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit();
                                          }
                                      }
        );
        return view;
    }


    //switch control for option1
    private void option1On(View view){
        priceImg1.setColorFilter(Color.parseColor(CHOSEN_BG));

        timer1.setImageResource(R.drawable.chosen_corner);

        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        timePrice1.setTypeface(boldTypeface);

        check1.setVisibility(view.getVisibility());

    }

    private void option1Off(View view){
        priceImg1.setColorFilter(Color.parseColor(NOT_CHOSEN_BG));

        timer1.setImageResource(R.drawable.corner);

        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.NORMAL);
        timePrice1.setTypeface(boldTypeface);

        check1.setVisibility(view.INVISIBLE);

    }

    //switch control for option2
    private void option2On(View view){
        priceImg2.setColorFilter(Color.parseColor(CHOSEN_BG));

        timer2.setImageResource(R.drawable.onhalf);

        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        timePrice2.setTypeface(boldTypeface);

        check2.setVisibility(view.getVisibility());

    }

    private void option2Off(View view){
        priceImg2.setColorFilter(Color.parseColor(NOT_CHOSEN_BG));

        timer2.setImageResource(R.drawable.half);

        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.NORMAL);
        timePrice2.setTypeface(boldTypeface);

        check2.setVisibility(view.INVISIBLE);

    }

    //switch control for option3
    private void option3On(View view){
        priceImg3.setColorFilter(Color.parseColor(CHOSEN_BG));

        timer3.setImageResource(R.drawable.onthird);

        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        timePrice3.setTypeface(boldTypeface);

        check3.setVisibility(view.getVisibility());

    }

    private void option3Off(View view){
        priceImg3.setColorFilter(Color.parseColor(NOT_CHOSEN_BG));

        timer3.setImageResource(R.drawable.third);

        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.NORMAL);
        timePrice3.setTypeface(boldTypeface);

        check3.setVisibility(view.INVISIBLE);

    }
}
