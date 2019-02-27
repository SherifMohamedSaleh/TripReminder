package com.example.trip.models;

import java.io.Serializable;

/**
 * Created by Toka on 2019-02-18.
 */
public class Note implements Serializable {
    private String name;
    private boolean isChecked;

    public Note() {
    }

    public Note(String name, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
