package com.peppypals.paronbeta.MainTabs;

import android.support.v7.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by kanbi on 19/04/2018.
 */

public class discoverModel extends ArrayList<adviceModel> {
    private String categoryTitle;
    private ArrayList<adviceModel> horizontalArrayList;

    public discoverModel() {

    }

    public discoverModel(String categoryTitle,ArrayList<adviceModel> horizontalArrayList) {
        this.categoryTitle = categoryTitle;
        this.horizontalArrayList =  horizontalArrayList;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public ArrayList<adviceModel> getHorizontalArrayList() {
        return horizontalArrayList;
    }

    public void setHorizontalArrayList(ArrayList<adviceModel> horizontalArrayList) {
        this.horizontalArrayList = horizontalArrayList;
    }

}
