package com.peppypals.paronbeta.MainTabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.peppypals.paronbeta.LoginActivity;
import com.peppypals.paronbeta.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HomeFragment extends Fragment implements AdviceAdapter.ButtonClickListner {

    private static final String TAG = "HomeFragment";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdviceAdapter adapter;
    private FirebaseFirestore rootRef;
    private CollectionReference factRef;
    private CollectionReference categoryRef;
    private CollectionReference adviceRef;
    private Button toProff;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        rootRef = FirebaseFirestore.getInstance();
        categoryRef = rootRef.collection("categories");
        factRef = rootRef.collection("randomFacts");

        toProff = (Button) view.findViewById(R.id.writeUs);
        toProff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ContactFragment fragment = new ContactFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.adviceRecyclerview);
        recyclerView.setHasFixedSize(true);

        //get daily advice + show recommended tips
        categoryRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    final ArrayList<adviceModel> adviceList = new ArrayList<>();

                    for (DocumentSnapshot document : task.getResult()) {
                        final String docId = document.getId();

                        if (categoryRef.document(docId).collection("tips")== null){
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }else {
                            adviceRef = categoryRef.document(docId).collection("tips" );
                            adviceRef.get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    if (document.exists()) {
                                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                                        String question = (String) document.getData().get("question");

                                                        //form array list for questions
                                                        adviceModel advice = document.toObject(adviceModel.class);
                                                        adviceList.add(advice);
                                                    } else {
                                                        Log.d(TAG, "no document has tips ", task.getException());
                                                    }

                                                    int adviceCount = adviceList.size();
                                                    int randomNumber = new Random().nextInt(adviceCount);
                                                    final adviceModel randomAdvice = adviceList.get(randomNumber);

                                                    TextView dailyQuestionText = (TextView)view.findViewById(R.id.dailyQuestion);
                                                    dailyQuestionText.setText(randomAdvice.getQuestion());

                                                    Button moreInfo = (Button)view.findViewById(R.id.readMoreBtn);
                                                    moreInfo.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(getActivity(), HomeAdviceActivity.class);

                                                            intent.putExtra("adviceQuestion", randomAdvice.getQuestion());
                                                            intent.putExtra("questionID", randomAdvice.getQuestionId());

                                                            getActivity().startActivity(intent);

                                                        }
                                                    });

                                                    //update on daily basis
                                                    /*final Calendar calendar = Calendar.getInstance();
                                                    calendar.setTimeInMillis(System.currentTimeMillis());
                                                    int currentDate = calendar.get(Calendar.DAY_OF_MONTH);

                                                    SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
                                                    int lastDate = sharedPreferences.getInt("lastTimeDate", 0);
                                                    if(lastDate != currentDate){
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putInt("lastTimeDate", currentDate);
                                                        editor.commit();
                                                    }*/

                                                    //add data to recommend list
                                                    layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
                                                    adapter = new AdviceAdapter(adviceList,HomeFragment.this);
                                                    recyclerView.setLayoutManager(layoutManager);
                                                    recyclerView.setAdapter(adapter);
                                                }

                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        });

        //get random fact
        factRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> factList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        String fact = document.getData().toString();
                        factList.add(fact);
                    }

                    int factsCount = factList.size();
                    int randomNumber = new Random().nextInt(factsCount);
                    String randomFact = factList.get(randomNumber);

                    TextView factText = (TextView)view.findViewById(R.id.factText);
                    factText.setText(randomFact.replaceAll("[{}]","").substring(5));;
                }
            }
        });

        return view;
    }

    @Override
    public void btnClick(adviceModel itemClicked) {

        Intent intent = new Intent(getActivity(), HomeAdviceActivity.class);

        intent.putExtra("adviceQuestion", itemClicked.getQuestion());
        intent.putExtra("questionID", itemClicked.getQuestionId());


        getActivity().startActivity(intent);

    }
}
