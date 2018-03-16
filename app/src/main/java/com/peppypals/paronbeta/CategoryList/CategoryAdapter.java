package com.peppypals.paronbeta.CategoryList;

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

/**
 * Created by kanbi on 14/03/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter <CategoryAdapter.ViewHolder>{

    //interface
    private ButtonClickListner onClicklistener;
    public interface ButtonClickListner  {
        void btnClick(String itemClicked);
    }

    //adapter
    private ArrayList<String>  categoryData;


    public CategoryAdapter(ArrayList<String> categoryData, ButtonClickListner onClicklistener) {
        this.categoryData = categoryData;
        this.onClicklistener = onClicklistener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryDescp;
        public Button categoryBtn;


        public ViewHolder(View itemView) {
            super(itemView);

            categoryDescp=(TextView) itemView.findViewById(R.id.categoryText);

            categoryBtn=(Button) itemView.findViewById(R.id.categoryBtn);

        }
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, final int i) {

        holder.categoryDescp.setText(categoryData.get(i));

        holder.categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClicklistener!=null)
                {
                    onClicklistener.btnClick(categoryData.get(i));

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
