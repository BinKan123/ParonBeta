package com.peppypals.paronbeta.MainTabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.peppypals.paronbeta.EmailSentActivity;
import com.peppypals.paronbeta.LoginActivity;
import com.peppypals.paronbeta.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiscoverFragment extends Fragment {
    private static final String TAG = "DiscoverFragment";

    private RecyclerView verticalRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DiscoverVerticalAdapter verticalAdapter;
    private CollectionReference categoryRef;
    ArrayList<discoverModel> allData;

    public DiscoverFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View view = inflater.inflate(R.layout.fragment_discover, container, false);

        //search bar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));


        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.toolbartop);

        //nested Lists
        categoryRef = FirebaseFirestore.getInstance().collection("categories");

        verticalRecyclerView = (RecyclerView) view.findViewById(R.id.discoverRecyclerview);
        verticalRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        verticalRecyclerView.setLayoutManager(layoutManager);
        verticalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        loadAllData();

       // verticalAdapter = new DiscoverVerticalAdapter(allData);
       // verticalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        Button logout = (Button) view.findViewById(R.id.logOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

//    //searchView
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//        inflater.inflate(R.menu.menu_toolbar, menu);
//        MenuItem item = menu.findItem(R.id.action_search);
//        final SearchView searchView = (SearchView) item.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchView.clearFocus();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                verticalAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//        super.onCreateOptionsMenu(menu,inflater);
//    }


    //load All Data
    public void loadAllData() {
        allData = new ArrayList<>();

        categoryRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final DocumentSnapshot documentMain : task.getResult()) {
                                Log.d(TAG, documentMain.getId() + " => " + documentMain.getData());

                                final discoverModel allCategory = new discoverModel();
                                final ArrayList<adviceModel> singleCategory = new ArrayList<adviceModel>();
                                categoryRef.document(documentMain.getId()).collection("tips")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    for (DocumentSnapshot document : task.getResult()) {
                                                        if (document.exists()) {
                                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                                        //form array list for questions
                                                            adviceModel advice = document.toObject(adviceModel.class);
                                                            singleCategory.add(advice);

                                                        } else {
                                                          Log.d(TAG, "no document has tips ", task.getException());
                                                        }
                                                    }

                                                    if(singleCategory.size()> 0){
                                                        allCategory.setHorizontalArrayList(singleCategory);
                                                        String categoryName = (String) documentMain.getData().get("name");
                                                        allCategory.setCategoryTitle(categoryName);
                                                        allData.add(allCategory);

                                                        verticalAdapter = new DiscoverVerticalAdapter( allData);

                                                        verticalRecyclerView.setAdapter(verticalAdapter);
                                                        verticalAdapter.notifyDataSetChanged();
                                                    }

                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }

                                            }
                                        });
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
