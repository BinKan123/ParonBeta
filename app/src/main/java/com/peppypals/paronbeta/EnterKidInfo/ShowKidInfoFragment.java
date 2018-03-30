package com.peppypals.paronbeta.EnterKidInfo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.peppypals.paronbeta.CategoryList.CategoryActivity;
import com.peppypals.paronbeta.CategoryList.CategoryModel;
import com.peppypals.paronbeta.R;

import java.util.HashMap;
import java.util.Map;


public class ShowKidInfoFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore firestoreDB;
    private FirebaseAuth firebaseAuth;
    private CollectionReference childrenRef;
    private String uid;
    private Query query;

    private String name;
    private String birthYear;
    private String birthMonth;
    private String birthDate;
    private String formatBirthMonth;
    private String formatBirthDate;

    private String birthday;
    private Map<String, Object> kidInfo;

    private static final String YEAR_TEXT = " år";

    public ShowKidInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_show_kid_info, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.kidrecyclerview);
        //recyclerView.setHasFixedSize(true);

        Bundle bundle = this.getArguments();
        name = bundle.getString("kidName", name);
        birthYear = bundle.getString("birthYear", birthYear);
        birthMonth = bundle.getString("birthMonth", birthMonth);
        birthDate = bundle.getString("birthDate", birthDate);
        if(birthMonth.length()==1){
            birthMonth = "0" + birthMonth;
        }

        if(birthDate.length()==1){
            birthDate = "0" + birthDate;
        }

        birthday = birthDate + " / " + birthMonth + " / " + birthYear;

        kidInfo = new HashMap<>();
        kidInfo.put("name", name);
        kidInfo.put("birthday", birthday);

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        childrenRef = FirebaseFirestore.getInstance().collection("users").document(uid).collection("children");

        childrenRef.document()
                .set(kidInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "kid info added ");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log.w(TAG, "Error adding document", e);
                    }
                });
        loadKidInfoList();

        Button addKidInfoBtn = (Button) view.findViewById(R.id.addKidBtn);
        addKidInfoBtn.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Fragment fragment = new EnterKidNameFragment();
                                                  FragmentManager fragmentManager = getFragmentManager();
                                                  fragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit();
                                              }
                                          }
        );

        Button confirmInfoBtn = (Button) view.findViewById(R.id.confirmBtn);
        confirmInfoBtn.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Fragment fragment = new EnterKidNameFragment();
                                                  FragmentManager fragmentManager = getFragmentManager();
                                                  fragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit();
                                              }
                                          }
        );

        return view;
    }

    private void loadKidInfoList() {
        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();

        query = firestoreDB.collection("users").document(uid).collection("children");

        FirestoreRecyclerOptions<KidData> response = new FirestoreRecyclerOptions.Builder<KidData>()
                .setQuery(query, KidData.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<KidData,KidInfoViewholder>(response) {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void onBindViewHolder(KidInfoViewholder holder, int position, KidData model) {
                final String docId = getSnapshots().getSnapshot(position).getId();

                //set name with first letter capitalized
                String nameInput = model.getName();
                String output = nameInput.substring(0, 1).toUpperCase() + nameInput.substring(1);

                //calculate age
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                String birthdayInput = model.getBirthday();
                int birthDate = Integer.parseInt(birthdayInput.substring(0, 2));
                int birthMonth = Integer.parseInt(birthdayInput.substring(5, 7));
                String testYear = birthdayInput.substring(10);
                int birthYear = Integer.parseInt(birthdayInput.substring(10));

                int age = calendar.get(Calendar.YEAR) - birthYear ;
                if (calendar.get(Calendar.MONTH) > birthMonth ){
                    holder.nameAge.setText(output+ ", " + String.valueOf(age)+ YEAR_TEXT);
                }else if (
                        calendar.get(Calendar.MONTH) == birthMonth && calendar.get(Calendar.DAY_OF_MONTH) >= birthDate){
                    holder.nameAge.setText(output+ ", " + String.valueOf(age)+ YEAR_TEXT);
                }else{
                    holder.nameAge.setText(output+ ", " + String.valueOf(age-1)+ YEAR_TEXT);
                }

              //  holder.nameAge.setText(model.getName()+", " + model.getBirthday());

                holder.removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        childrenRef.document(docId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Kid info. removed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                });
            }

            @Override
            public KidInfoViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.kidinfo_item, parent, false);

                return new KidInfoViewholder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
                Toast.makeText(getActivity(), e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private class KidInfoViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameAge;
        private Button removeBtn;

        private KidInfoViewholder(View itemView) {
            super(itemView);

            nameAge = (TextView) itemView.findViewById(R.id.nameAgeText);
            removeBtn = (Button) itemView.findViewById(R.id.removeInfoBtn);
            itemView.setOnClickListener(this);
            childrenRef = firestoreDB.collection("users").document(uid).collection("children");
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
