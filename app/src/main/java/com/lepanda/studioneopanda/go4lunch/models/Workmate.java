package com.lepanda.studioneopanda.go4lunch.models;

import android.graphics.Bitmap;

public class Workmate {

    private String text;
    private Bitmap bitmap;

    public Workmate(){
        //empty constructor needed for Firebase
    }

    public Workmate(String title, Bitmap bitmap) {
        this.text = title;
        this.bitmap = bitmap;
    }

    public String getText() {
        return text;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
