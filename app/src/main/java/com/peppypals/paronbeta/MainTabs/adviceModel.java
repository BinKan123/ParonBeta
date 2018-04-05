package com.peppypals.paronbeta.MainTabs;

/**
 * Created by kanbi on 04/04/2018.
 */

public class adviceModel {
    private int minAge;
    private int maxAge;
    private String question;

    public adviceModel() {

    }

    public adviceModel(int minAge, int maxAge, String question) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.question = question;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
