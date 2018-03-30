package com.peppypals.paronbeta.EnterKidInfo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.peppypals.paronbeta.LoginActivity;
import com.peppypals.paronbeta.R;

import static com.peppypals.paronbeta.Utils.HideKeyboard.hideSoftKeyboard;


public class EnterKidNameFragment extends Fragment {

    private EditText nameInput;

    public EnterKidNameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_enter_kid_name, container, false);

        nameInput = (EditText) view.findViewById(R.id.kidNameInput);

        Button nameDoneBtn = (Button) view.findViewById(R.id.nameDoneBtn);
        nameDoneBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             //send name to ShowKidInfoFragment
                                             final String name = nameInput.getText().toString().toLowerCase().trim();
                                             if (TextUtils.isEmpty(name)){
                                                 Toast.makeText(getActivity(), "Vänligen skriva in namnet", Toast.LENGTH_SHORT).show();

                                             }else{
                                             Bundle arguments = new Bundle();
                                             arguments.putString("kidName", String.valueOf(name));
                                                 EnterKidAgeFragment goToFragment = new EnterKidAgeFragment();
                                                 goToFragment.setArguments(arguments);
                                                 FragmentManager fragmentManager = getFragmentManager();
                                                 fragmentManager.beginTransaction().replace(android.R.id.content, goToFragment).commit();
                                                 }

                                         }
                                     }
        );

        //dismiss keyboard
        dismissKeyboard(view.findViewById(R.id.parent));

        return view;
    }

    public void dismissKeyboard(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }
    }
}
