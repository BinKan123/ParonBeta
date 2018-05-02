package com.peppypals.paronbeta.MainTabs;

/**
 * Created by kanbi on 06/04/2018.
 */

public class kidsPostModel {
    private int age;
    private String name;
    private String answer;

    public kidsPostModel() {

    }

    public kidsPostModel(int age, String name, String answer) {
        this.age = age;
        this.name = name;
        this.answer = answer;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
