package com.peppypals.paronbeta.EnterKidInfo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.peppypals.paronbeta.R;

import java.util.HashMap;
import java.util.Map;

import static com.peppypals.paronbeta.Utils.HideKeyboard.hideSoftKeyboard;


public class EnterKidAgeFragment extends Fragment {

    private static final int EARLY_YEAR = 2;
    private static final String YEAR_TEXT = " år";
    private static final String TAG = "EnterKidAgeFragment";
    private Button ageDoneBtn;
    private TextView kidAge;

    private FirebaseFirestore firestoreDB;
    private FirebaseAuth firebaseAuth;
    private CollectionReference kidInfoRef;
    private String uid;

    private String name;
    public String birthday;
    private Map<String, Object> kidInfo;

    public EnterKidAgeFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_enter_kid_age, container, false);

        kidAge = (TextView) view.findViewById(R.id.kidAgeResult);

        final DatePicker datePicker = view.findViewById(R.id.datePicker);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR)-EARLY_YEAR, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                int age = calendar.get(Calendar.YEAR) -datePicker.getYear() ;
                if (calendar.get(Calendar.MONTH) > datePicker.getMonth() ){
                    kidAge.setText(String.valueOf(age)+ YEAR_TEXT);
                }else if (calendar.get(Calendar.MONTH) == datePicker.getMonth() && calendar.get(Calendar.DAY_OF_MONTH) >= datePicker.getDayOfMonth()){
                    kidAge.setText(String.valueOf(age)+ YEAR_TEXT);
                }else{
                    kidAge.setText(String.valueOf(age-1)+ YEAR_TEXT);
                }
                birthday = datePicker.getDayOfMonth() + " / " + (datePicker.getMonth()+1) + " / " + datePicker.getYear();

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        kidInfoRef = FirebaseFirestore.getInstance().collection("users").document(uid).collection("children");
        Bundle bundle = this.getArguments();
        name = bundle.getString("kidName", name);

        ageDoneBtn = (Button) view.findViewById(R.id.ageDoneBtn);
        ageDoneBtn.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                              //send info

                                              if (TextUtils.isEmpty(kidAge.getText())){
                                                  Toast.makeText(getActivity(), "Vänligen välja barnets fördelsedag", Toast.LENGTH_SHORT).show();

                                              }else {

                                                  Bundle arguments = new Bundle();
                                                  arguments.putString("kidName", String.valueOf(name));
                                                  arguments.putString("birthYear", String.valueOf(datePicker.getYear()));
                                                  arguments.putString("birthMonth", String.valueOf(datePicker.getMonth() + 1));
                                                  arguments.putString("birthDate", String.valueOf(datePicker.getDayOfMonth()));


                                                  ShowKidInfoFragment fragment = new ShowKidInfoFragment();
                                                  fragment.setArguments(arguments);
                                                  FragmentManager fragmentManager = getFragmentManager();
                                                  fragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit();
                                              }

                                          }
                                      }
        );

        return view;
    }


}
