package com.peppypals.paronbeta;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.peppypals.paronbeta.CategoryList.CategoryActivity;
import com.peppypals.paronbeta.MainTabs.MainTabsActivity;

import java.util.HashMap;
import java.util.Map;

public class InfoActivity extends AppCompatActivity {

    private static final String TAG = "InfoActivity";

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String uid;
    private CollectionReference childrenRef;

    //facebook login variables
    private String fbNameValue;
    private String fbEmailValue;
    private Map<String, Object> fbUser;

    //google login variables
    private String gNameValue;
    private String gMailValue;
    private Map<String, Object> gUser;

    //email login variables
    private String firstNameValue;
    private String lastNameValue;
    private String emailValue;
    private String passwordValue;
    private Map<String, Object> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        childrenRef = FirebaseFirestore.getInstance().collection("users").document(uid).collection("children");

        childrenRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if(document.exists()) {
                                    Intent intent = new Intent(getBaseContext(), MainTabsActivity.class);
                                    startActivity(intent);
                                } else {
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        TextView importantText = (TextView) findViewById(R.id.importantText);
        importantText.setText(Html.fromHtml(getString(R.string.importantInfo)));

        //set first word to pink
        SpannableString spannableString = new SpannableString(Html.fromHtml(getString(R.string.importantInfo)));
        ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));
        spannableString.setSpan(foregroundSpan, 0,8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        importantText.setText(spannableString);

        //receiving login info.
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            //receiving facebook login info.
            fbNameValue = extras.getString("fbName");
            fbEmailValue = extras.getString("fbEmail");

            //receiving google login info.
            gNameValue = extras.getString("gName");
            gMailValue = extras.getString("gMail");

            //receiving email login info.
            firstNameValue = extras.getString("firstName");
            lastNameValue = extras.getString("lastName");
            emailValue = extras.getString("email");
            passwordValue = extras.getString("password");
            //The key argument here must match that used in the other activity
        }

        //facebook user login dataset
        fbUser = new HashMap<>();
        fbUser.put("name", fbNameValue);
        fbUser.put("email", fbEmailValue);

        //google user login dataset
        gUser = new HashMap<>();
        gUser.put("name", gNameValue);
        gUser.put("email", gMailValue);

        //email user login dataset
        user = new HashMap<>();
        user.put("name", firstNameValue+" "+lastNameValue);
        user.put("email", emailValue);
        user.put("password", passwordValue);

        //add login info. to firestore:
        //facebook
        if (fbUser.entrySet().iterator().next().getValue() != null) {
            addUserFirestore(uid, fbUser);
        }

        //google
        if (gUser.entrySet().iterator().next().getValue() != null) {
            addUserFirestore(uid, gUser);
        }

        //email
        if (user.entrySet().iterator().next().getValue() != null) {
            addUserFirestore(uid, user);
        }

        Button understandBtn = (Button)findViewById(R.id.understandBtn);
        understandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, CategoryActivity.class);
                startActivity(intent);
                }
                            });
        }

    private void addUserFirestore(String uid, Map<String, Object> user) {
        firebaseFirestore.collection("users")
                .document(uid)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "user added ");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


}
