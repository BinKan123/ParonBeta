package com.peppypals.paronbeta.MainTabs;

/**
 * Created by kanbi on 06/04/2018.
 */

public class parentsPostModel {
    private int age;
    private int like;
    private String name;
    private String answer;

    public parentsPostModel() {

    }

    public parentsPostModel(int age, int like, String name, String answer) {
        this.age = age;
        this.like = like;
        this.name = name;
        this.answer = answer;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
