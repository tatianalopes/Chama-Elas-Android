package com.tcal.chamaelas.model;

public class Professional {

    private String mName;
    private String mSpecialty;
    private float mRating;

    public  Professional(String name, String specialty, float rating) {
        mName = name;
        mSpecialty = specialty;
        mRating = rating;
    }

    public String getName() {
        return mName;
    }

    public String getSpecialty() {
        return mSpecialty;
    }

    public float getRating() {
        return mRating;
    }
}
