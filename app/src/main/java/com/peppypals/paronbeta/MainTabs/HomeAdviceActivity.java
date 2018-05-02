package com.peppypals.paronbeta.MainTabs;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.peppypals.paronbeta.R;

import java.util.ArrayList;
import java.util.Random;

public class HomeAdviceActivity extends AppCompatActivity {

    private static final String TAG = "HomeAdviceActivity";

    private FirebaseFirestore firestoreDB;
    private CollectionReference categoryRef;
    private CollectionReference answerRef;
    private CollectionReference expertAdviceRef;
    private CollectionReference kidsAnswerRef;
    private CollectionReference parentsAnswerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_advice);

        firestoreDB = FirebaseFirestore.getInstance();

        //RECEIVE DATA VIA INTENT
        Intent intent = getIntent();
        String dailyQuestion = intent.getStringExtra("adviceQuestion");
        final String questionId = intent.getStringExtra("questionID");

        //SET DATA TO TEXTVIEWS
        TextView dailyQuestionText = (TextView) findViewById(R.id.extraToughText);
        dailyQuestionText.setText(dailyQuestion);

        categoryRef = firestoreDB.collection("categories");

        categoryRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    final ArrayList<expertAdviceModel> expertAdviceList = new ArrayList<>();
                    final ArrayList<kidsPostModel> kidsWordsList = new ArrayList<>();
                    final ArrayList<parentsPostModel> parentsWordsList = new ArrayList<>();

                    for (DocumentSnapshot document : task.getResult()) {
                        final String docId = document.getId();

                        if (categoryRef.document(docId).collection("tips") == null){
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }else {
                            //expertAdvice
                            answerRef = categoryRef.document(docId)
                                    .collection("tips");

                            expertAdviceRef = answerRef.document(questionId).collection("expertAdvice");
                            expertAdviceRef.get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    if (document.exists()) {
                                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                                        expertAdviceModel expertAdvice = document.toObject(expertAdviceModel.class);
                                                        expertAdviceList.add(expertAdvice);
                                                        Log.d(TAG, "onSuccess: " + expertAdviceList);
                                                    } else {
                                                        Log.d(TAG, "no document has tips ", task.getException());
                                                    }

                                                    int adviceCount = expertAdviceList.size();
                                                    int randomNumber = new Random().nextInt(adviceCount);
                                                    expertAdviceModel randomAdvice = expertAdviceList.get(randomNumber);

                                                    TextView expertTitle = (TextView) findViewById(R.id.adviceTitle);
                                                    expertTitle.setText(randomAdvice.getTitle());

                                                    TextView expertDetails = (TextView) findViewById(R.id.extraDetails);
                                                    expertDetails.setText(randomAdvice.getDetails());


                                                }

                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });

                           //kidsWords
                            kidsAnswerRef = answerRef.document(questionId).collection("kidsWords");
                            kidsAnswerRef.get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                                    kidsPostModel kidsPost = document.toObject(kidsPostModel.class);
                                                    kidsWordsList.add(kidsPost);

                                                    int adviceCount = kidsWordsList.size();
                                                    int randomNumber = new Random().nextInt(adviceCount);
                                                    kidsPostModel randomAdvice = kidsWordsList.get(randomNumber);

                                                    TextView kidsSaid = (TextView) findViewById(R.id.kidsText);
                                                    kidsSaid.setText(randomAdvice.getAnswer());

                                                }
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });

                            //parentsWords
                             parentsAnswerRef = answerRef.document(questionId).collection("parentsWords");
                             parentsAnswerRef.get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                                    parentsPostModel parentsPost = document.toObject(parentsPostModel.class);
                                                    parentsWordsList.add(parentsPost);

                                                    int adviceCount = parentsWordsList.size();
                                                    int randomNumber = new Random().nextInt(adviceCount);
                                                    parentsPostModel randomAdvice = parentsWordsList.get(randomNumber);

                                                    TextView parentsSaid = (TextView) findViewById(R.id.parentsText);
                                                    parentsSaid.setText(randomAdvice.getAnswer());

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
    }


    //try to create function for getting random advice for all 3 types, but couldn't figure out how to handle name of model.class
   /* public ArrayList getRandomAnswer(CollectionReference answerRef, String docId, String questionId, String resourceName, final ArrayList dataSet, final ArrayList<dataSet> adviceList, ArrayList randomAdvice) {

        answerRef = categoryRef.document(docId)
                .collection("tips").document(questionId).collection(resourceName);
        answerRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    if (dataSet == expertAdviceModel) {
                                        expertAdviceModel expertAdvice = document.toObject(expertAdviceModel.class);
                                        adviceList.add(expertAdvice);
                                    } else if (dataSet == kidsPostModel) {
                                        kidsPostModel kidsPost = document.toObject(kidsPostModel.class);
                                        adviceList.add(kidsPost);
                                    }else{
                                        parentsPostModel parentsPost = document.toObject(parentsPostModel.class);
                                        adviceList.add(parentsPost);
                                    }
                                    Log.d(TAG, "onSuccess: " + adviceList);
                                } else {
                                    Log.d(TAG, "no document has tips ", task.getException());
                                }

                                int adviceCount = adviceList.size();
                                int randomNumber = new Random().nextInt(adviceCount);
                                dataSet randomAdvice = adviceList.get(randomNumber);

                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        return randomAdvice;

    }*/

}
