package com.peppypals.paronbeta.MainTabs;

/**
 * Created by kanbi on 06/04/2018.
 */

public class expertAdviceModel {

    private String title;
    private String details;

    public expertAdviceModel() {

    }

    public expertAdviceModel(String title, String details) {
        this.title = title;
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
