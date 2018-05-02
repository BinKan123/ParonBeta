package com.peppypals.paronbeta.MainTabs;

/**
 * Created by kanbi on 04/04/2018.
 */

public class adviceModel {
    private String minAge;
    private String maxAge;
    private String question;
    private String questionId;
    private String adviceNum;


    public adviceModel() {

    }

    public adviceModel(String minAge, String maxAge, String question,String questionId,String adviceNum) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.question = question;
        this.questionId = questionId;
        this.adviceNum= adviceNum;
    }

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAdviceNum() {
        return adviceNum;
    }

    public void setAdviceNum(String adviceNum) {
        this.adviceNum = adviceNum;
    }
}
