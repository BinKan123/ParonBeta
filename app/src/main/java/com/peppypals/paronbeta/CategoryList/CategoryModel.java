package com.peppypals.paronbeta.CategoryList;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kanbi on 17/03/2018.
 */

public class CategoryModel {

   // private String id;
    private String name;
    private String color;

    public CategoryModel(){

    }

    public CategoryModel(String name, String color) {

        this.name = name;
        this.color = color;
    }





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

  /*  public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("color", this.color);
        result.put("name", this.title);

        return result;
    }*/
}
