package com.peppypals.paronbeta.EnterKidInfo;

import android.support.annotation.Nullable;

/**
 * Created by kanbi on 28/03/2018.
 */

public class KidData {
    private String name;
    private String birthday;

    public KidData() {

    }

    public KidData(@Nullable String name, @Nullable String birthday) {
        this.name = name;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
