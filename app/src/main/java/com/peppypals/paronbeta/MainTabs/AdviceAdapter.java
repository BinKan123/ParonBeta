package com.peppypals.paronbeta.MainTabs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.peppypals.paronbeta.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kanbi on 04/04/2018.
 */

public class AdviceAdapter extends RecyclerView.Adapter <AdviceAdapter.ViewHolder>{

    //adapter
    private final ArrayList<adviceModel> adviceData;

    public AdviceAdapter(ArrayList<adviceModel> adviceData) {
        this.adviceData = adviceData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView adviceBar;
        private TextView adviceQuestion;
        private TextView numTips;

        public ViewHolder(View itemView) {
            super(itemView);

            adviceBar = itemView.findViewById(R.id.adviceBar);
            adviceQuestion = itemView.findViewById(R.id.adviceQuestion);
            numTips = itemView.findViewById(R.id.numTipsText);

        }
    }

    @Override
    public AdviceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.advice_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdviceAdapter.ViewHolder holder, final int i) {
        final adviceModel item = adviceData.get(i);

        holder.adviceQuestion.setText(item.getQuestion());

        //add random color to imageview background
        final Context context = holder.itemView.getContext();
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        holder.adviceBar.setColorFilter(randomAndroidColor);

        holder.adviceBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return adviceData.size();
    }

}
