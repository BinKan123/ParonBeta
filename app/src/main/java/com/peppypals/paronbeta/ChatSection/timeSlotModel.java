package com.peppypals.paronbeta.ChatSection;

/**
 * Created by kanbi on 05/05/2018.
 */

public class timeSlotModel {
    private int id;
    private String time;
    private String today;
    private String tomo;
    private String tomoafter;

    public timeSlotModel() {

    }

    public timeSlotModel(int id, String time, String today, String tomo, String tomoafter) {
        this.id = id;
        this.time = time;
        this.today = today;
        this.tomo = tomo;
        this.tomoafter = tomoafter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getTomo() {
        return tomo;
    }

    public void setTomo(String tomo) {
        this.tomo = tomo;
    }

    public String getTomoafter() {
        return tomoafter;
    }

    public void setTomoafter(String tomoafter) {
        this.tomoafter = tomoafter;
    }
}
