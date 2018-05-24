package com.peppypals.paronbeta.MainTabs;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.peppypals.paronbeta.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by kanbi on 04/04/2018.
 */

public class AdviceAdapter extends RecyclerView.Adapter <AdviceAdapter.ViewHolder>{

    //interface
    private ButtonClickListner onClicklistener;
    public interface ButtonClickListner  {
        void btnClick(adviceModel itemClicked);
    }

    private List<adviceModel> adviceData;

    public AdviceAdapter(List<adviceModel> adviceData, ButtonClickListner onClicklistener) {
        this.adviceData = adviceData;
        this.onClicklistener = onClicklistener;
    }

    public AdviceAdapter(List<adviceModel> adviceData) {
        this.adviceData = adviceData;
    }

    public void setData(List<adviceModel> data,ButtonClickListner onClicklistener) {
        if (adviceData!= data) {
            adviceData = data;
            notifyDataSetChanged();
        }
        this.onClicklistener = onClicklistener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView adviceBar;
        private TextView adviceQuestion;
        private TextView numTips;

        public ViewHolder(View itemView) {
            super(itemView);

            adviceBar = itemView.findViewById(R.id.adviceBar);
            adviceQuestion = itemView.findViewById(R.id.categoryName);
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
        holder.numTips.setText(item.getAdviceNum()+ " Tips");

        //add random color to imageview background
        final Context context = holder.itemView.getContext();
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        holder.adviceBar.setColorFilter(randomAndroidColor);

        holder.adviceBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClicklistener!=null)
                {
                    onClicklistener.btnClick(item);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return adviceData.size();
    }

}
