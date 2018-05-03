package com.peppypals.paronbeta.CategoryList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.peppypals.paronbeta.R;
import com.peppypals.paronbeta.StartHere.StartHereActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CategoryActivity extends AppCompatActivity {

    private static final String TAG = "CategoryActivity";
    private static final int NUM_COL = 2;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore firestoreDB;
    private FirebaseAuth firebaseAuth;
    private CollectionReference favRef;
    private String uid;
    private ListenerRegistration firestoreListener;
    private GridLayoutManager gridLayoutManager;
    private Query query;
    private boolean addFav = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);

        TextView categoryItem = (TextView) findViewById(R.id.categoryTitle);
        categoryItem.setText(Html.fromHtml(getString(R.string.related_tips)));

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();

        recyclerView=(RecyclerView) findViewById(R.id.caterecyclerview);
        gridLayoutManager = new GridLayoutManager(CategoryActivity.this, NUM_COL);
        recyclerView.setHasFixedSize(true);

        loadCategoryList();

        final Button doneBtn = (Button) findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CategoryActivity.this,StartHereActivity.class);
                startActivity(i);
            }
        });
    }

    private void loadCategoryList(){
        query = firestoreDB.collection("categories");

        FirestoreRecyclerOptions<CategoryModel> response = new FirestoreRecyclerOptions.Builder<CategoryModel>()
                .setQuery(query, CategoryModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<CategoryModel, CategoryViewholder>(response) {
            @Override
            protected void onBindViewHolder(final CategoryViewholder holder, final int position, final CategoryModel model) {
                holder.categoryTitle.setText(model.getName());

                final String docId = getSnapshots().getSnapshot(position).getId();

                //keep the color on according to status of buttons
                holder.setFavBtn(holder,docId, model);

                //adding to fav
                holder.categoryColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFav = true;
                        favRef.document().addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                @Nullable FirebaseFirestoreException e) {
                                if (addFav) {
                                    favRef.document(docId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            if (documentSnapshot.exists()){
                                                Log.e(TAG, "Exits");

                                                holder.categoryColor.setColorFilter(getResources().getColor(R.color.dot_inactive), PorterDuff.Mode.SRC_IN);

                                                favRef.document(docId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getBaseContext(), "removed from favorite", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }else{
                                                Log.e(TAG,"Not Exits");
                                                holder.categoryColor.setColorFilter(Color.parseColor("#" + model.getColor() ));

                                                Map<String, Object> favoriteCategories =  new HashMap<>();
                                                favoriteCategories.put("name", model.getName());
                                                favoriteCategories.put("color", model.getColor());

                                                favRef.document(docId)
                                                        .set(favoriteCategories)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d(TAG, "fav added ");
                                                                Toast.makeText(getBaseContext(), "Added as Favorite",
                                                                        Toast.LENGTH_LONG).show();

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error adding document", e);
                                                                Toast.makeText(getBaseContext(), "Failed to add as favorite" + e,
                                                                        Toast.LENGTH_LONG).show();
                                                            }
                                                        });

                                                addFav = false;
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
            public CategoryViewholder onCreateViewHolder( ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.category_item, parent, false);

                return new CategoryViewholder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
                Toast.makeText(getBaseContext(),  e.getMessage() ,
                        Toast.LENGTH_SHORT).show();
            }
        };

        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        }

    private class CategoryViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView categoryTitle;
        private ImageView categoryColor;

        private CategoryViewholder(View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryName);
            categoryColor = itemView.findViewById(R.id.adviceBar);
            itemView.setOnClickListener(this);

            favRef = FirebaseFirestore.getInstance().collection("users").document(uid).collection("favCategories");

        }

        @Override
        public void onClick(View v) {

        }

        public void setFavBtn(final CategoryViewholder holder, final String docId, final CategoryModel model){
            favRef.document().addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {

                    favRef.document(docId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()){
                                Log.e(TAG, "Exits");
                                holder.categoryColor.setColorFilter(Color.parseColor("#" + model.getColor() ));
                            }else{
                                Log.e(TAG,"Not Exits");
                                holder.categoryColor.setColorFilter(getResources().getColor(R.color.dot_inactive), PorterDuff.Mode.SRC_IN);

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
