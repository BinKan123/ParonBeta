package com.peppypals.paronbeta.ChatSection;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.peppypals.paronbeta.EnterKidInfo.KidData;
import com.peppypals.paronbeta.EnterKidInfo.ShowKidInfoFragment;
import com.peppypals.paronbeta.R;

import java.util.HashMap;
import java.util.Map;

public class ChooseTimeSlotFragment extends Fragment {

    private static final String TAG = "ChooseTimeSlotFragment";

    private String chosenExpert;
    private String chosenPrice;

    private static final String CHOSEN_PSYK = "Psykolog";
    private static final String CHOSEN_PEDA = "Pedagog";
    private static final String CHOSEN_ARROW = "#6d6a6a";
    private static final String NOT_CHOSEN_ARROW = "#DBD6D6";
    private static final String BOOKED_OFF = "#A9A9A9";
    private static final String TODAY_SV = "Idag";
    private static final String TODAY_EN = "today";
    private static final String TOMO_SV = "Imorgon";
    private static final String TOMO_EN = "tomo";
    private String tomoAfter_SV;
    private static final String TOMOAFTER_EN = "tomoafter";
    private static final String CHOSEN_BG = "#dfedcd";
    private static final String NOT_CHOSEN_BG = "#f6f1ea";

    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore firestoreDB;
    private CollectionReference bookingRef;
    private CollectionReference bookingStatusRef;
    private static final String MAIN_DOCID = "hf7jUBD2HBRUkLTnMqBE";
    private FirebaseAuth firebaseAuth;
    private String uid;
    private CollectionReference timeSlotRef;
    private Query query;
    private boolean addTime = false;

    private TextView dayText;
    private String dayText_eng;
    public ChooseTimeSlotFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_choose_time_slot, container, false);

        Bundle bundle = this.getArguments();
        chosenExpert = bundle.getString("chosenExpert", chosenExpert);
        chosenPrice = bundle.getString("chosenPrice", chosenPrice);

        TextView expertName = (TextView) view.findViewById(R.id.expertName);
        TextView showPrice = (TextView) view.findViewById(R.id.timePriceText);
        expertName.setText(chosenExpert);
        showPrice.setText(chosenPrice.substring(0, 6));

        final ImageView expertIcon = (ImageView) view.findViewById(R.id.expertIcon);
        if (chosenExpert.equals(CHOSEN_PSYK)) {
            expertIcon.setImageResource(R.drawable.psypic);
        } else {
            expertIcon.setImageResource(R.drawable.pedagog);
        }

        dayText = (TextView) view.findViewById(R.id.dayText);
        dayText.setText(TODAY_SV);

        recyclerView=(RecyclerView) view.findViewById(R.id.timerecyclerview);


        //to match text-of-day in database
        if (dayText.getText().equals(TODAY_SV)){
            dayText_eng = TODAY_EN;
        }else if (dayText.getText().equals(TOMO_SV)){
            dayText_eng = TOMO_EN;
        }else{
            dayText_eng = TOMOAFTER_EN;
        }
        loadAvailableTime(dayText_eng);

        //set arrow status according to text-of-day
        final ImageView dayBefore = (ImageView) view.findViewById(R.id.dayBeforeBtn);
        dayBefore.setColorFilter(Color.parseColor(NOT_CHOSEN_ARROW));
        dayBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dayText.getText().equals(TOMO_SV)){
                    dayText.setText(TODAY_SV);
                    dayBefore.setColorFilter(Color.parseColor(NOT_CHOSEN_ARROW));
                    loadAvailableTime(TODAY_EN);

                    adapter.startListening();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        ImageView dayAfter = (ImageView) view.findViewById(R.id.dayAfterBtn);
        dayAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dayText.getText().equals(TODAY_SV)){
                    dayText.setText(TOMO_SV);
                    dayBefore.setColorFilter(Color.parseColor(CHOSEN_ARROW));
                    loadAvailableTime(TOMO_EN);
                    adapter.startListening();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        final FragmentManager fragmentManager = getFragmentManager();
        Button backChoose = (Button) view.findViewById(R.id.backChooseBtn);
        backChoose.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              if (chosenExpert.equals(CHOSEN_PSYK)) {
                                                  Fragment fragment = new PsykOptionFragment();
                                                  fragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit();
                                              } else {
                                                  Fragment fragment = new PedaOptionFragment();
                                                  fragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit();
                                              }
                                          }
                                      }
        );

        Button timeChosenBtn = (Button) view.findViewById(R.id.nextchatBtn);
        timeChosenBtn.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 Fragment fragment = new BillingFragment();
                                             fragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit();

                                             }
                                         }
        );

        return view;
    }

    private void loadAvailableTime(final String dayOfBooking) {
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        firestoreDB = FirebaseFirestore.getInstance();

        query = firestoreDB.collection("bookExpert").document(MAIN_DOCID).collection(chosenExpert).orderBy("id");
                //.whereEqualTo(dayOfBooking,"on");

        FirestoreRecyclerOptions<timeSlotModel> response = new FirestoreRecyclerOptions.Builder<timeSlotModel>()
                .setQuery(query, timeSlotModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<timeSlotModel,TimeSlotViewholder>(response) {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void onBindViewHolder(final TimeSlotViewholder holder, int position, final timeSlotModel model) {
                final String docId = getSnapshots().getSnapshot(position).getId();

                holder.timeSlot.setText(model.getTime());

                //to make sure only chosen time on chosen day should be highlighted
                bookingRef. whereEqualTo("date", dayOfBooking)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        holder.timeChosenBtn(holder,docId);
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });


                //keep the color on according to status of buttons
                // holder.timeChosenBtn(holder,docId, model);

                holder.timeBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //add chosen-time to user-data

                        addTime = true;
                        bookingRef.document().addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                @Nullable FirebaseFirestoreException e) {
                                if (addTime) {
                                    bookingRef.document(docId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            if (documentSnapshot.exists()){
                                                Log.e(TAG, "Exits");

                                                holder.timeChosenBtn(holder,docId);

                                                bookingRef.document(docId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getActivity(), "removed as chosen-time",
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                            }else{
                                                Log.e(TAG,"Not Exits");
                                                holder.timeChosenBtn(holder,docId);

                                                Map<String, Object> bookingInfo =  new HashMap<>();
                                                bookingInfo.put("chosenExpert", chosenExpert);
                                                bookingInfo.put("timeSlot", model.getTime());
                                                bookingInfo.put("date", dayOfBooking);

                                                bookingRef.document(docId)
                                                        .set(bookingInfo)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d(TAG, "fav added ");
                                                                Toast.makeText(getActivity(), "Successfully chosen",
                                                                        Toast.LENGTH_LONG).show();

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error adding document", e);
                                                                Toast.makeText(getActivity(), "Failed to add booking" + e,
                                                                        Toast.LENGTH_LONG).show();
                                                            }
                                                        });

                                                addTime = false;
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public TimeSlotViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.timeslot_item, parent, false);

                return new TimeSlotViewholder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private class TimeSlotViewholder  extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView timeSlot;
        private ImageView timeBar;

        private TimeSlotViewholder(View itemView) {
            super(itemView);

            timeSlot = (TextView) itemView.findViewById(R.id.timeItem);
            timeBar = (ImageView) itemView.findViewById(R.id.timebg);

            itemView.setOnClickListener(this);

            bookingRef = FirebaseFirestore.getInstance().collection("users").document(uid).collection("bookedExpert");
            bookingStatusRef = FirebaseFirestore.getInstance().collection("bookExpert").document(MAIN_DOCID).collection(chosenExpert);
        }

        @Override
        public void onClick(View v) {

        }

        public void timeChosenBtn(final TimeSlotViewholder holder, final String docId){
            bookingRef.document().addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {

                    bookingRef.document(docId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()){
                                Log.e(TAG, "Exits");
                                Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                                holder.timeSlot.setTypeface(boldTypeface);

                                holder.timeBar.setColorFilter(Color.parseColor(CHOSEN_BG));
                            }else{
                                Log.e(TAG,"Not Exits");

                                Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.NORMAL);
                                holder.timeSlot.setTypeface(boldTypeface);

                                holder.timeBar.setColorFilter(Color.parseColor(NOT_CHOSEN_BG ));

                            }
                        }
                    });
                }
            });
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
