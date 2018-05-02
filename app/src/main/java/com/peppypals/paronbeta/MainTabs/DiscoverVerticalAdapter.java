package com.peppypals.paronbeta.MainTabs;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peppypals.paronbeta.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kanbi on 19/04/2018.
 */

public class DiscoverVerticalAdapter extends RecyclerView.Adapter <DiscoverVerticalAdapter.ViewHolder>{

    //adapter
    private  ArrayList<discoverModel> discoverData;
    private static RecyclerView horizontalList;


    public DiscoverVerticalAdapter(ArrayList<discoverModel> discoverData) {
        this.discoverData = discoverData;

    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryName;

        private AdviceAdapter horizontalAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();

            categoryName = itemView.findViewById(R.id.categoryName);
            horizontalList = (RecyclerView) itemView.findViewById(R.id.catItemRecyclerview);
            horizontalList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            horizontalAdapter = new AdviceAdapter();
            horizontalList.setAdapter(horizontalAdapter);

        }
    }

    @Override
    public DiscoverVerticalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DiscoverVerticalAdapter.ViewHolder holder, final int i) {
        final discoverModel item = discoverData.get(i);
        holder.categoryName.setText(item.getCategoryTitle());
        holder.horizontalAdapter.setData(item.getHorizontalArrayList());



       /* //add random color to imageview background
        final Context context = holder.itemView.getContext();
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        holder.adviceBar.setColorFilter(randomAndroidColor);
       */;
    }

    @Override
    public int getItemCount() {
        return discoverData.size();
    }

}

